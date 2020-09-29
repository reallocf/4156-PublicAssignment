package models;

public class Move {

  private Player player;
  private int moveX;
  private int moveY;

  /**
   * Constructor for Move.
   * Throws if either moveX or moveY are not 0, 1, or 2.
   * @param player Player associated with this move
   * @param moveX int Indicates the 0 to 2 X coordinate of the move
   * @param moveY int Indicates the 0 to 2 Y coordinate of the move
   */
  public Move(Player player, int moveX, int moveY) {
    if (moveX < 0 || moveX > 2 || moveY < 0 || moveY > 2) {
      throw new RuntimeException("Attempting to create invalid move");
    }
    
    this.player = player;
    this.moveX = moveX;
    this.moveY = moveY;
  }

  /**
   * Get the player associated with this move.
   * @return Player associated with this move
   */
  public Player getPlayer() {
    return this.player;
  }
  
  /**
   * Get the X-coordinate of the move.
   * @return int X-coordinate of the move, either 0, 1, or 2
   */
  public int getMoveX() {
    return this.moveX;
  }
  
  /**
   * Get the Y-coordinate of the move.
   * @return int Y-coordinate of the move, either 0, 1, or 2
   */
  public int getMoveY() {
    return this.moveY;
  }

}
