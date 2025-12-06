package sanguine.model;

import java.util.List;

/**
 * Represents a board that contains null/empty cells, ValueCells with the value
 * of the card placed on them, and PawnCells with the number of pawns on them.
 */
public interface Board {

  /**
   * Returns the String representation of the board.
   *
   * @return the board as a string
   */
  String getBoard();

  /**
   * Counts the total score for the value cells in a given row for a player.
   *
   * @param row a specific row of a board
   * @param player either a red or blue player
   * @return the row score for a given player
   */
  int getRowScore(int row, Player player);

  /**
   * Counts the sum of all row scores for a given player.
   *
   * @param player enum player either red or blue
   * @return the total score for a player
   */
  int getTotalScore(Player player);

  /**
   * Changes the cells of the board based on given card and the ownership of the cell.
   *
   * @param card a given type of card to play
   * @param row the specified row of the board
   * @param col the specified column of the board
   */
  void playCard(Card card, int row, int col);

  /**
   * Gets the specific type of cell at a given spot of the influence board.
   *
   * @param row a specific row of the board
   * @param col a specific column of the board
   * @return the cell at a given spot on the board
   */
  Cell getCell(int row, int col);

  /**
   * Gets a copy of the cells on the board.
   *
   * @return Copy of cells on the board
   */
  List<List<Cell>> getCells();

  /**
   * Checks if a card placement on a given board coordinate is valid.
   * The cell must be a PawnCell and have enough pawns to cover the cost of the card.
   *
   * @param currentCell the cell that is selected
   * @param card the card that is selected
   */
  void checkValidMove(Cell currentCell, Card card);
}
