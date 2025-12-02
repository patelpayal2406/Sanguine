package sanguine.view;

import sanguine.model.Player;

/**
 * Represents a listener for mouse events to select cells and cards in the game.
 */
public interface FeatureListener {
  /**
   * Sends messages to the BoardPanel about the selected cell.
   *
   * @param row the row of the cell
   * @param col the column of the cell
   * @param player the player that selected the cell
   */
  void selectBoardCell(int row, int col, Player player);

  /**
   * Sends messages to the HandPanel about the selected card.
   *
   * @param cardIndex the index of the selected card
   * @param player the player that selected the card
   */
  void selectCard(int cardIndex, Player player);

  void confirmMove(Player player);

  void passTurn(Player player);
}
