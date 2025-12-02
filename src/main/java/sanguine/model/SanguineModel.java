package sanguine.model;

/**
 * Represents the model interface of Sanguine that only contains mutator methods.
 */
public interface SanguineModel extends ReadonlySanguineModel {

  /**
   * Allows the user to play a card from their deck on the board to influence the cells.
   *
   * @param card a given type of card to play
   * @param row the specified row of the board
   * @param col the specified column of the board
   */
  void playCard(Card card, int row, int col);

  /**
   * Switches the current player and draws a card into their hand.
   */
  void switchPlayer();

  /**
   * Starts the Sanguine game with the given width and height of the board.
   *
   * @param rows the width of the board
   * @param cols the height of the board
   * @param handSize the hand size for each player. Cannot exceed 1/3 the size of the deck.
   * @param shuffled if true, the deck of influence cards is shuffled
   */
  void startGame(int rows, int cols, int handSize, boolean shuffled);

  /**
   * Current player passes their turn without playing a card.
   * Game ends if both players pass consecutively.
   */
  void pass();
}
