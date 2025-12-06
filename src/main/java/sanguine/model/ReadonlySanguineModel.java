package sanguine.model;

import java.util.List;

/**
 * Represents the model interface that only contains observer methods.
 */
public interface ReadonlySanguineModel {
  /**
   * Game is over when players pass one after the other.
   *
   * @return true if the game is over
   */
  boolean gameOver();

  /**
   * Returns the player with the highest score. Returns null if the game is tied.
   *
   * @return the player that won the game, or null for a tie
   */
  Player getWinner();

  /**
   * Returns the current player of the game.
   *
   * @return the current player
   */
  Player getCurrentPlayer();

  /**
   * Gets the board interface to access board state.
   *
   * @return the board
   */
  Board getBoard();

  /**
   * Gets the deck of InfluenceCards of the given player.
   *
   * @param player enum player that is either red or blue
   * @return the deck of the player
   */
  List<Card> getDeck(Player player);

  /**
   * Gets the player's hand of cards which they are able to see during the game.
   *
   * @param player enum player that is either red or blue
   * @return the player's card hand
   */
  List<Card> getPlayerHand(Player player);

  /**
   * Checks if the placement of a card on a given board coordinate is valid.
   *
   * @param currentCell the selected cell
   * @param card the selected card
   */
  void checkValidMove(Cell currentCell, Card card);

  /**
   * Gets the cell of a given board coordinate.
   *
   * @param row the row number of the board
   * @param col the column number of the board
   * @return the cell at the given coordinate
   */
  Cell getCell(int row, int col);

  /**
   * Gets the total score for a given player.
   *
   * @param player either a RED or BLUE player
   * @return the total score of a player
   */
  int getTotalScore(Player player);

  /**
   * Gets the score of a player at a given row.
   *
   * @param row the row number of a board
   * @param player either a RED or BLUE player
   * @return the row score of a player
   */
  int getRowScore(int row, Player player);

  /**
   * Returns the player who owns a given cell or null if the cell is null.
   *
   * @param row the row number of the board
   * @param col the column number of the board
   * @return the owner of a given board cell coordinate
   */
  Player getCellOwner(int row, int col);

  /**
   * Returns the contents of a given cell as a String. A PawnCell has a player and number of pawns,
   * a ValueCell has a player and a value from the card placed on it, and a null cell is just null.
   *
   * @param row the row number of the board
   * @param col the column number of the board
   * @return the contents of a given cell coordinate
   */
  String getCellContents(int row, int col);

  /**
   * Returns how many rows are in the board.
   *
   * @return the number of rows in the board
   */
  int getNumRows();

  /**
   * Returns how many columns are in the board.
   *
   * @return the number of columns in the board
   */
  int getNumCols();
}
