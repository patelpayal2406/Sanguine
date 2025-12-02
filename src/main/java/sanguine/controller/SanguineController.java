package sanguine.controller;


/**
 * Represents the controller that connects user input with the model and displays game states
 * through the view.
 */
public interface SanguineController {
  /**
   * Initializes the model and board with the given input parameters.
   *
   * @param rows the number of rows on the board
   * @param cols the number of columns on the board
   * @param handSize the number of cards player has access to at a time
   * @param shuffle true if the deck of InfluenceCards should be shuffled
   */
  void playGame(int rows, int cols, int handSize, boolean shuffle);

  void checkGameOver();
}
