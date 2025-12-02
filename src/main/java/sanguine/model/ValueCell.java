package sanguine.model;

/**
 * Represents a cell that has a card placed on it. The value of the card is displayed on this cell.
 */
public class ValueCell implements Cell {
  private final int value;
  private final Player player;

  /**
   * Initializes the value of this cell based on the card that was placed on it
   * and who has ownership.
   *
   * @param value the value of the card placed (must be positive)
   * @param player the player who owns this cell (cannot be null)
   * @throws IllegalArgumentException if arguments are invalid
   */
  public ValueCell(int value, Player player) {
    if (value <= 0) {
      throw new IllegalArgumentException("Value must be positive");
    }
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    this.value = value;
    this.player = player;
  }

  @Override
  public int getValue() {
    return this.value;
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }
}
