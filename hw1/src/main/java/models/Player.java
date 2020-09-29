package models;

public class Player {

  private char type;
  private int id;
  
  /**
   * Constructor for Player with id of either 1 or 2.
   * Throws if attempting to create a Player numbered something other than 1 or 2.
   * type is initialized to '\u0000' since this is set later through setType().
   * @param id int 1 or 2 depending on if it's the first or second player
   * @param type char 'X' or 'O' depending on what token the player will be using
   */
  public Player(int id, char type) {
    if (id != 1 && id != 2) {
      throw new RuntimeException("Id for Player can only be set to 1 or 2");
    }
    if (type != 'X' && type != 'O') {
      throw new RuntimeException("Set type must be either 'X' or 'O'");
    }
    
    this.type = type;
    this.id = id;
  }

  /**
   * Get the type of the Player - X or O.
   * @return char Type representing if this player is using 'X' or 'O'
   */
  public char getType() {
    return this.type;
  }
  
  /**
   * Get the id of the Player - 1 or 2.
   * @return int 1 or 2 depending on if this is the first or the second player
   */
  public int getId() {
    return this.id;
  }

}
