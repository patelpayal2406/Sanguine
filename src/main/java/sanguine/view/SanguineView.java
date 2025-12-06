package sanguine.view;

import sanguine.model.Player;

/**
 * The JFrame interface for a single window of the game.
 */
public interface SanguineView {
  /**
   * Refresh the view to reflect any changes in the game state.
   */
  void refresh();

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();

  /**
   * Sets click listeners to panels for mouse clicks.
   *
   * @param listener feature listener for mouse clicks
   */
  void setListener(FeatureListener listener);

  /**
   * Adjusts the size of the frame and panels based on the parameters of the started game.
   *
   * @param rows the number of rows of the board
   * @param cols the number of columns of the board
   * @param handSize the number of InfluenceCards for the player to look card
   */
  void refreshSize(int rows, int cols, int handSize);

  /**
   * Gets the BoardPanel component of this frame.
   *
   * @return the frame's BoardPanel
   */
  PublisherPanel getBoardPanel();

  /**
   * Gets the HandPanel component of this frame.
   *
   * @return the frame's HandPanel
   */
  PublisherPanel getHandPanel();

  /**
   * Sends a message about the game being over.
   *
   * @param winner the player that won or null if no one won
   */
  void endGame(Player winner);

  /**
   * Is called by the endGame method and shows who won.
   *
   * @param winner the player that won or null if no one won
   */
  void showWinner(Player winner);

  /**
   * Sends a message about which player's turn it is.
   *
   * @param player the current player
   */
  void showTurn(Player player);

  /**
   * Sends a message about any error that occurred.
   *
   * @param reason the error message
   */
  void showError(String reason);

  /**
   * Sends a message about which player passed their turn.
   *
   * @param player the current player
   */
  void showPass(Player player);
}
