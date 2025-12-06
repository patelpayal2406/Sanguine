package sanguine.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sanguine.model.Board;
import sanguine.model.Card;
import sanguine.model.Cell;
import sanguine.model.Player;

/**
 * A mock implementation of the Board interface for testing purposes.
 * Allows setting up specific board states and tracking method calls.
 */
public class MockBoard implements Board {
  private final List<List<Cell>> cells;
  private final List<String> log;
  private final Map<String, Integer> forcedScores;

  /**
   * Creates a MockBoard with the given grid of cells.
   *
   * @param cells the 2D list of cells representing the board
   */
  public MockBoard(List<List<Cell>> cells) {
    this.cells = cells;
    this.log = new ArrayList<>();
    this.forcedScores = new HashMap<>();
  }

  /**
   * Creates a MockBoard with the specified dimensions, initialized with null cells.
   *
   * @param rows number of rows
   * @param cols number of columns
   */
  public MockBoard(int rows, int cols) {
    this.cells = new ArrayList<>();
    for (int r = 0; r < rows; r++) {
      List<Cell> row = new ArrayList<>();
      for (int c = 0; c < cols; c++) {
        row.add(null);
      }
      this.cells.add(row);
    }
    this.log = new ArrayList<>();
    this.forcedScores = new HashMap<>();
  }

  /**
   * Sets a specific cell at the given coordinates.
   *
   * @param row the row index
   * @param col the column index
   * @param cell the cell to place
   */
  public void setCell(int row, int col, Cell cell) {
    if (row >= 0 && row < cells.size() && col >= 0 && col < cells.get(row).size()) {
      cells.get(row).set(col, cell);
    }
  }

  /**
   * Forces a specific score for a row and player, overriding calculation.
   *
   * @param row the row index
   * @param player the player
   * @param score the score to return
   */
  public void setScore(int row, Player player, int score) {
    forcedScores.put(row + player.toString(), score);
  }

  @Override
  public List<List<Cell>> getCells() {
    log.add("getCells");
    return cells;
  }

  @Override
  public void checkValidMove(Cell currentCell, Card card) {
    log.add("checkValidMove");
  }

  @Override
  public Cell getCell(int row, int col) {
    log.add("getCell:" + row + "," + col);
    if (row < 0 || row >= cells.size() || col < 0 || col >= cells.get(row).size()) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    return cells.get(row).get(col);
  }

  @Override
  public int getRowScore(int row, Player player) {
    log.add("getRowScore:" + row + "," + player);
    
    if (forcedScores.containsKey(row + player.toString())) {
      return forcedScores.get(row + player.toString());
    }

    int score = 0;
    for (Cell cell : cells.get(row)) {
      if (cell != null && cell.getPlayer() == player) {
        score += cell.getValue();
      }
    }
    return score;
  }

  @Override
  public int getTotalScore(Player player) {
    log.add("getTotalScore:" + player);

    int score = 0;

    for (String name : forcedScores.keySet()) {
      if (name.equals(player.toString())) {
        score += forcedScores.get(name);
      }
    }

    return score;
  }

  /**
   * Returns the log of method calls.
   *
   * @return list of logged actions
   */
  public List<String> getLog() {
    return new ArrayList<>(log);
  }

  /**
   * Clears the interaction log.
   */
  public void clearLog() {
    log.clear();
  }

  @Override
  public String getBoard() {
    log.add("getBoard");
    return "";
  }

  @Override
  public void playCard(Card card, int row, int col) {
    log.add("playCard:" + row + "," + col);
  }
}
