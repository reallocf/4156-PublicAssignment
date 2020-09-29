package models;

import java.util.AbstractMap;
import java.util.Map;

public class Message {
  
  public static final Integer VALID = 100;
  public static final Integer NOT_YOUR_TURN = 101;
  public static final Integer ALREADY_OCCUPIED = 102;
  public static final Integer NOT_BEGUN = 103;
  public static final Integer ALREADY_OVER = 104;
  
  private static final Map<Integer, String> CODE_TO_MESSAGE_MAP = Map.ofEntries(
      new AbstractMap.SimpleEntry<Integer, String>(VALID, ""),
      new AbstractMap.SimpleEntry<Integer, String>(NOT_YOUR_TURN, "It is not your turn"),
      new AbstractMap.SimpleEntry<Integer, String>(ALREADY_OCCUPIED, "Space is already occupied"),
      new AbstractMap.SimpleEntry<Integer, String>(NOT_BEGUN, "The game has not begun"),
      new AbstractMap.SimpleEntry<Integer, String>(ALREADY_OVER, "The game has already finished")
      );

  private boolean moveValidity;
  private int code;
  private String message;
  
  /**
   * Constructor for Message.
   * Throws if the passed code is invalid.
   * @param code int Code representing either a valid move or an error message
   */
  public Message(int code) {
    if (!CODE_TO_MESSAGE_MAP.containsKey(code)) {
      throw new RuntimeException("Invalid code in message");
    }
    
    this.moveValidity = code == 100;
    this.code = code;
    this.message = CODE_TO_MESSAGE_MAP.get(code);
  }

  /**
   * Get if the move was valid.
   * @return boolean True if move is valid, false otherwise
   */
  public boolean getMoveValidity() {
    return this.moveValidity;
  }
  
  /**
   * Get the code for the move.
   * @return int Code 100 if valid, error representation otherwise
   */
  public int getCode() {
    return this.code;
  }

  /**
   * Get the message to display to the user if there was an error.
   * @return String Empty message if the move was valid, otherwise an error message
   */
  public String getMessage() {
    return this.message;
  }
}
