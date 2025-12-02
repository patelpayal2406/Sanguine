package sanguine.view;

import javax.swing.JPanel;
import sanguine.model.Player;

/**
 * Represents a pub-sub panel for displaying Board and Hand Panels.
 */
public interface PublisherPanel {
  /**
   * Sets a specific listener to each of the panels.
   *
   * @param listener a mouse click feature listen
   */
  void setListener(FeatureListener listener);

  /**
   * Updates the panel dimensions based on the initialized parameters of the game.
   *
   * @param rows the number of rows on the board
   * @param cols the number of columns on the board
   * @param handSize the number of InfluenceCards available for the player to look at
   */
  void updateDimensions(int rows, int cols, int handSize);

  /**
   * Gets the number of columns on the board which is the logical width of the panel.
   *
   * @return the calculated, scaled width of a panel
   */
  int getLogicalWidth();

  /**
   * Gets the number of rows on the board which is the logical height of the panel.
   *
   * @return the calculated, scaled height of a panel
   */
  int getLogicalHeight();

  /**
   * Obtains the JPanel object of the panel.
   *
   * @return a JPanel object
   */
  JPanel getJpanel();

  /**
   * Gets the player of the frame that houses the specific panels.
   *
   * @return the player of the frame
   */
  Player getPlayer();

  /**
   * Highlights a cell if a mouse was clicked on it. Deselected if clicked on again.
   *
   * @param row the row of the clicked cell
   * @param col the column of the clicked cell
   * @param player the player who clicked the mouse
   * @param selected true if cell is intended to be selected/highlighted, false for deselection
   */
  void highlightCell(int row, int col, Player player, boolean selected);

  /**
   * Highlights a card if a mouse was clicked on it. Deselected if clicked on again.
   *
   * @param cardIndex the index of the clicked card
   * @param player the player who clicked the mouse
   * @param selected true if card is intended to be selected/highlighted, false for deselection
   */
  void highlightCard(int cardIndex, Player player, boolean selected);
}
