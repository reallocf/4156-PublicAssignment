package models;

public class GameBoard {
  
  private static final char[][] EMPTY_BOARD_STATE = new char[][] {
      new char[] {'\u0000', '\u0000', '\u0000'},
      new char[] {'\u0000', '\u0000', '\u0000'},
      new char[] {'\u0000', '\u0000', '\u0000'}
  };

  private Player p1;
  private Player p2;
  private boolean gameStarted;
  private int turn;
  private char[][] boardState;
  private int winner;
  private boolean isDraw;
  
  /**
   * Default constructor for GameBoard.
   * Initializes private member variables to starting states.
   * Start p1 and p2 as null - will be set by callers later.
   */
  public GameBoard() {
    this.p1 = null;
    this.p2 = null;
    this.gameStarted = false;
    this.turn = 1;
    this.boardState = EMPTY_BOARD_STATE;
    this.winner = 0;
    this.isDraw = false;
  }
  
  /**
   * Get the appropriate player based on id.
   * Throws if id isn't 1 or 2 since these are the only valid player ids.
   * 
   * @param id int 1 or 2 indicating Player1 or Player2
   * @return Player associated with the passed id
   */
  public Player getPlayerById(int id) {
    switch (id) {
      case 1:
        return this.p1;
      case 2:
        return this.p2;
      default:
        throw new RuntimeException("Trying to get player by invalid id");
    }
  }
  
  /**
   * Indicate that the game has started.
   */
  public void startGame() {
    this.gameStarted = true;
  }
  
  /**
   * Validate the passed move and return with either a VALID message after updating internal state
   * appropriately or an error message.
   * @param move Move representing the current action being taken
   * @return Message with either error information or VALID
   */
  public Message attemptMove(Move move) {
    if (this.gameStarted == false) {
      return new Message(Message.NOT_BEGUN);
    } else if (this.winner != 0 || this.isDraw) {
      return new Message(Message.ALREADY_OVER);
    } else if (move.getPlayer().getId() != this.turn) {
      return new Message(Message.NOT_YOUR_TURN);
    } else if (!(this.boardState[move.getMoveX()][move.getMoveY()] == '\u0000')) {
      return new Message(Message.ALREADY_OCCUPIED);
    } else {
      this.boardState[move.getMoveX()][move.getMoveY()] = move.getPlayer().getType();
      
      if (inWinState()) {
        this.winner = this.turn;
      } else if (inDrawState()) {
        this.isDraw = true;
      } else {
        this.turn = (this.turn == 1) ? 2 : 1;
      }
      
      return new Message(Message.VALID);
    }
  }

  /**
   * Determine if a player has won.
   * @return True if the board currently represents a winning condition, false otherwise
   */
  private boolean inWinState() {
    // Check verticals
    for (int y = 0; y < 3; y++) {
      if (checkAllSameType(
          this.boardState[0][y],
          this.boardState[1][y],
          this.boardState[2][y]
      )) {
        return true;
      }
    }
    // Check horizontals
    for (int x = 0; x < 3; x++) {
      if (checkAllSameType(
          this.boardState[x][0],
          this.boardState[x][1],
          this.boardState[x][2]
      )) {
        return true;
      }
    }
    // Check diagonals
    if (checkAllSameType(
        this.boardState[0][0],
        this.boardState[1][1],
        this.boardState[2][2]
    )) {
      return true;
    }
    if (checkAllSameType(
        this.boardState[2][0],
        this.boardState[1][1],
        this.boardState[0][2]
    )) {
      return true;
    }
    
    return false;
  }

  /**
   * Checks three chars to see if they're all 'X' or all 'O'.
   * @param firstType char first to compare
   * @param secondType char second to compare
   * @param thirdType char thirst to compare
   * @return boolean True if all three are the same type, false otherwise
   */
  private boolean checkAllSameType(char firstType, char secondType, char thirdType) {
    return firstType == secondType
        && secondType == thirdType
        && (firstType == 'X' || firstType == 'O');
  }
  
  /**
   * Determine if the players have reached a draw.
   * @return boolean True if the board currently represents a draw condition, false otherwise
   */
  private boolean inDrawState() {
    if (!inWinState()) { // Call inWinState() again to decouple function ordering
      for (int x = 0; x < 3; x++) {
        for (int y = 0; y < 3; y++) {
          if (this.boardState[x][y] == '\u0000') {
            return false;
          }
        }
      }
    } else {
      return false;      
    }
    return true;
  }
  
  /**
   * Get first player.
   * @return Player First player
   */
  public Player getP1() {
    return this.p1;
  }
  
  /**
   * Throws if player's id is not 1.
   * Throws if setting p1 would set it to the same value as p2 if p2 is already set.
   * @param p1 Player to set as first player
   */
  public void setP1(Player p1) {
    if (p1.getId() != 1) {
      throw new RuntimeException("Attempting to set somebody who is not player 1 to p1");
    }
    
    if (this.p2 != null && this.p2.getType() == p1.getType()) {
      throw new RuntimeException("Attempting to set p1 type to the same as p2");
    }
    
    this.p1 = p1;
  }
  
  /**
   * Get second player.
   * @return Player Second player
   */
  public Player getP2() {
    return this.p2;
  }
  
  /**
   * Throws if Player's id is not 2.
   * Throws if setting p2 would set it to the same value as p1 if p1 is already set.
   * 
   * @param p2 Player to set as second player
   */
  public void setP2(Player p2) {
    if (p2.getId() != 2) {
      throw new RuntimeException("Attempting to set somebody who is not player 2 to p2");
    }
    
    if (this.p1 != null && this.p1.getType() == p2.getType()) {
      throw new RuntimeException("Attempting to set p2 type to the same as p1");
    }
    
    this.p2 = p2;
  }
  
  /**
   * Get if the game has started.
   * @return boolean Whether or not the game has started yet
   */
  public boolean getGameStarted() {
    return this.gameStarted;
  }
  
  /**
   * Get whose turn it is.
   * @return int If 1 then it's Player1's turn, if 2 then it's Player2's turn
   */
  public int getTurn() {
    return this.turn;
  }
  
  /**
   * Get the current board state.
   * @return char[][] The current board state, full of the appropriate X's and O's
   */
  public char[][] getBoardState() {
    return this.boardState;
  }
  
  /**
   * Get the winner if somebody has won.
   * @return int The value of the winner: 0 if nobody has won or the id of the winner (1 or 2)
   */
  public int getWinner() {
    return this.winner;
  }
  
  /**
   * Get if the game has ended in a draw.
   * @return boolean If the game has ended in a draw
   */
  public boolean getIsDraw() {
    return this.isDraw;
  }

}
