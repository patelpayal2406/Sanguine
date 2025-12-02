package sanguine.model;

/**
 * Represents a cell that holds a certain number of pawns.
 */
public class PawnCell implements Cell {
  private final int numPawns; // INVARIANT: must be between 1 and 3
  private final Player player;

  /**
   * Initializes the number of pawns this cell holds and who has ownership.
   *
   * @param numPawns a number of pawns (must be between 1 and 3)
   * @param player a player of a cell (cannot be null)
   * @throws IllegalArgumentException if arguments are invalid
   */
  public PawnCell(int numPawns, Player player) {
    if (numPawns < 1 || numPawns > 3) {
      throw new IllegalArgumentException("Number of pawns must be between 1 and 3");
    }
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    this.numPawns = numPawns;
    this.player = player;
  }

  @Override
  public int getValue() {
    return this.numPawns;
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }
}
