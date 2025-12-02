package sanguine.model;

/**
 * Represents a type of cell that is displayed on the board. A cell is either null, PawnCell,
 * or ValueCell. PawnCells have 1-3 pawns and ValueCells have 1-5 as the value of the card
 * placed on it. Each cell is either owned by no one, the blue player, or the red player.
 */
public interface Cell {
  /**
   * Gets either the number of pawns or value of card of a cell.
   *
   * @return the numerical value of a cell
   */
  int getValue();

  /**
   * Gets the player who has ownership of a cell.
   *
   * @return the player of a cell
   */
  Player getPlayer();
}
