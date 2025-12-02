package sanguine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a board of column by row cells. Players can place a card on the board
 * and see their row scores which is the sum of their value cells. The board is represented with a
 * list of lists of cells where each inner list is a row of cells.
 */
public class InfluenceBoard implements Board {
  private final List<List<Cell>> cells;

  /**
   * Initializes a board with the provided rows and cols.
   * The first column is initialized with red pawns and the last column with blue pawns.
   *
   * @param rows Number of rows on the board. Must be greater than 0.
   * @param cols Number of cols on the board. Must be greater than 1 and odd.
   * @throws IllegalArgumentException If arguments are invalid.
   */
  public InfluenceBoard(int rows, int cols) throws IllegalArgumentException {
    if (rows <= 0 || cols <= 1 || cols % 2 != 1) {
      throw new IllegalArgumentException("Invalid rows and cols.");
    }
    //adds a blank cell for each row and column on the board
    this.cells = new ArrayList<>(new ArrayList<>());
    for (int row = 0; row < rows; row++) {
      this.cells.add(new ArrayList<>());
      for (int col = 0; col < cols; col++) {
        this.cells.get(row).add(null);
      }
    }
    //initializes the board to set up pawns on certain cells
    this.initializePawns(rows, cols);
  }

  private void initializePawns(int rows, int cols) {
    for (int row = 0; row < rows; row++) {
      //adds pawns to the first column on each player's side
      this.setCell(row, 0, new PawnCell(1, Player.RED));
      this.setCell(row, cols - 1, new PawnCell(1, Player.BLUE));
    }
  }

  @Override
  public String getBoard() {
    StringBuilder sb = new StringBuilder();
    for (int row = 0; row < this.cells.size(); row++) {
      //shows this row's calculated score for the red player's value cells
      sb.append(this.getRowScore(row, Player.RED)).append(" ");
      for (int col = 0; col < this.cells.get(row).size(); col++) {
        if (this.getCell(row, col) instanceof PawnCell) {
          //displays the number of pawns on this cell
          sb.append(this.getCell(row, col).getValue());
        } else if (this.getCell(row, col) instanceof ValueCell) {
          //displays the owner of the card that was placed on this cell
          sb.append(this.getCell(row, col).getPlayer().toString().charAt(0));
        } else {
          //represents an empty cell
          sb.append("_");
        }
      }
      //shows this row's calculated score for the blue player's value cells
      sb.append(" ").append(this.getRowScore(row, Player.BLUE)).append("\n");
    }
    return sb.toString();
  }

  @Override
  public int getRowScore(int row, Player player) {
    int sum = 0;
    for (int col = 0; col < this.cells.get(row).size(); col++) {
      Cell currentCell = this.getCell(row, col);
      if (currentCell instanceof ValueCell
              && currentCell.getPlayer() == player) {
        //sums all the values of each value cell for a given row and player
        sum += currentCell.getValue();
      }
    }
    return sum;
  }

  @Override
  public int getTotalScore(Player player) {
    int redTotal = 0;
    int blueTotal = 0;

    for (int row = 0; row < this.cells.size(); row++) {
      //finds the row specific score for each player
      int redRowScore = this.getRowScore(row, Player.RED);
      int blueRowScore = this.getRowScore(row, Player.BLUE);
      //adds each row's score for each player
      if (redRowScore > blueRowScore) {
        redTotal += redRowScore;
      } else if (blueRowScore > redRowScore) {
        blueTotal += blueRowScore;
      }
    }
    return player == Player.RED ? redTotal : blueTotal;
  }

  @Override
  public void playCard(Card card, int row, int col) throws IllegalArgumentException {
    Cell currentCell = this.getCell(row, col);
    // Check if cell is a pawn cell
    if (!(currentCell instanceof PawnCell)) {
      throw new IllegalArgumentException("Invalid cell. Must be a pawn cell.");
    }
    // Check if cell is owned by player
    if (!(currentCell.getPlayer() == card.getPlayer())) {
      throw new IllegalArgumentException("Invalid cell. Cell not owned by current player.");
    }
    // Check if cell has enough pawns for cost
    if (currentCell.getValue() < card.getCost()) {
      throw new IllegalArgumentException("Invalid cell. Not enough pawns for card cost.");
    }
    // Get influences for card
    List<Coordinate> influences = card.getInfluence();
    // Loop through influences, using row and col as absolute position
    for (Coordinate influence : influences) {
      // Change cell for each influence position
      this.influenceCell(card.getPlayer(), row + influence.getY(), col + influence.getX());
    }

    // Update the cell to reflect the card's value
    this.setCell(row, col, new ValueCell(card.getValue(), card.getPlayer()));
  }

  private void influenceCell(Player player, int row, int col) throws IllegalArgumentException {
    //does nothing if arguments are invalid
    if (row < 0 || row >= this.cells.size()
            || col < 0 || col >= this.cells.get(0).size()) {
      return;
    }

    Cell c = getCell(row, col);
    if (c instanceof PawnCell) {
      if (c.getPlayer() == player) {
        //adds a pawn to an influenced cell if this player owns it
        // but number of pawns cannot exceed 3
        this.setCell(row, col, new PawnCell(Math.min(c.getValue() + 1, 3), player));
      } else {
        //player takes ownership of the pawns in the influenced cell if they don't already own them
        this.setCell(row, col, new PawnCell(c.getValue(), player));
      }
    } else if (c == null) {
      //creates a single pawn PawnCell if the influenced cell was empty
      this.setCell(row, col, new PawnCell(1, player));
    }
  }

  private void setCell(int row, int col, Cell cell) {
    this.checkValidCell(row, col);
    this.cells.get(row).set(col, cell);
  }

  @Override
  public Cell getCell(int row, int col) {
    this.checkValidCell(row, col);
    return this.cells.get(row).get(col);
  }

  @Override
  public List<List<Cell>> getCells() {
    List<List<Cell>> copy = new ArrayList<>();
    for (List<Cell> row : this.cells) {
      List<Cell> copiedRow = new ArrayList<>(row);
      copy.add(copiedRow);
    }
    return copy;
  }

  private void checkValidCell(int row, int col) throws IllegalArgumentException {
    if (row < 0 || row >= this.cells.size()
            || col < 0 || col >= this.cells.get(0).size()) {
      throw new IllegalArgumentException("Invalid row or column.");
    }
  }
}
