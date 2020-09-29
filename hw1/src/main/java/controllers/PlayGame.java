package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import java.util.regex.Pattern;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.eclipse.jetty.websocket.api.Session;

class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;
  
  // Probably should create a separate GameBoardManager class but this is good enough for now
  private static GameBoard[] gameBoardManager = new GameBoard[] { null };
  
  private static Gson gson = new Gson();

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });

    // Initialize a new game
    app.get("/newgame", ctx -> {
      gameBoardManager[0] = new GameBoard();
      ctx.redirect("/tictactoe.html");
    });
    
    // Player 1 selects either X or O by passing in body "type=X" or "type=O"
    app.post("/startgame", ctx -> {
      validateActiveGameBoard();
      
      GameBoard gameBoard = gameBoardManager[0];
      
      String request = ctx.body();

      if (!Pattern.matches("type=[XO]", request)) {
        throw new RuntimeException("Improperly formatted input to startgame endpoint");
      }

      gameBoard.setP1(new Player(1, request.charAt(5)));

      gameBoardManager[0] = gameBoard;
      ctx.result(gson.toJson(gameBoard));
    });
    
    app.get("/joingame", ctx -> {
      validateActiveGameBoard();
      validateActivePlayer1();
      
      GameBoard gameBoard = gameBoardManager[0];
      
      char p2Type;
      
      if (gameBoard.getP1().getType() == 'X') {
        p2Type = 'O';
      } else {
        p2Type = 'X';
      }
      
      gameBoard.setP2(new Player(2, p2Type));
      
      gameBoard.startGame();

      gameBoardManager[0] = gameBoard;
      sendGameBoardToAllPlayers(gson.toJson(gameBoard));
      ctx.redirect("/tictactoe.html?p=2");
    });

    app.post("/move/:playerId", ctx -> {
      validateActiveGameBoard();
      validateActivePlayer1();
      validateActivePlayer2();
      
      GameBoard gameBoard = gameBoardManager[0];
      
      String request = ctx.body();
      
      if (!Pattern.matches("x=[012]&y=[012]", request)) {
        throw new RuntimeException("Improperly formatted input to move endpoint");
      }
      
      int playerId = Integer.parseInt(ctx.pathParam("playerId"));
      
      Player player = gameBoard.getPlayerById(playerId);
      int moveX = Character.getNumericValue(request.charAt(2));
      int moveY = Character.getNumericValue(request.charAt(6));
      
      Move move = new Move(player, moveX, moveY);
      
      Message message = gameBoard.attemptMove(move);
      gameBoardManager[0] = gameBoard;
      sendGameBoardToAllPlayers(gson.toJson(gameBoard));
      ctx.result(gson.toJson(message));
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }
  
  /**
   *  Throws an exception if the user hits an endpoint before initializing a game.
   */
  private static void validateActiveGameBoard() {
    if (gameBoardManager[0] == null) {
      throw new RuntimeException("Trying to use GameBoard before starting game");
    }
  }
  
  /**
   *  Throws an exception if the user hits an endpoint before Player1 has been initialized.
   */
  private static void validateActivePlayer1() {
    if (gameBoardManager[0].getP1() == null) {
      throw new RuntimeException("Trying to use Player1 before Player1 has been initialized");
    }
  }
  
  /**
   *  Throws an exception if the user hits an endpoint before Player2 has been initialized.
   */
  private static void validateActivePlayer2() {
    if (gameBoardManager[0].getP2() == null) {
      throw new RuntimeException("Trying to use Player2 before Player2 has been initialized");
    }
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
