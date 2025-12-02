package sanguine.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import sanguine.model.Cell;
import sanguine.model.PawnCell;
import sanguine.model.Player;
import sanguine.model.ReadonlySanguineModel;
import sanguine.model.ValueCell;

/**
 * JPanel that displays the board and row scores of the game.
 */
public class BoardPanel extends AbstractPanel {

  private int selectedCellRow;
  private int selectedCellCol;
  private boolean cellIsSelected;

  /**
   * Initializes model, player, and unselected cells for panel.
   *
   * @param model viewable only model
   * @param player either red or blue
   */
  public BoardPanel(ReadonlySanguineModel model, Player player) {
    super(model, player);
    this.selectedCellRow = -1;
    this.selectedCellCol = -1;
    this.cellIsSelected = false;
  }

  @Override
  public void highlightCell(int row, int col, Player player, boolean selected) {
    if (player != this.player) {
      return;
    }
    //marks down the position of the cell if it is selected
    if (selected) {
      this.selectedCellRow = row;
      this.selectedCellCol = col;
      this.cellIsSelected = true;
    } else {
      //resets fields when cell is deselected
      this.cellIsSelected = false;
      this.selectedCellRow = -1;
      this.selectedCellCol = -1;
    }
    this.repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    if (logicalWidth == 0 || logicalHeight == 0) {
      return;
    }
    //cells are 100 by 100 pixels
    int cellSize = Math.min(this.getWidth() / this.logicalWidth, this.getHeight()
            / this.logicalHeight);
    //centers the board on the panel
    int xcoordOffset = (this.getWidth() - cellSize * this.logicalWidth) / 2;
    int ycoordOffset = (this.getHeight() - cellSize * this.logicalHeight) / 2;
    //draws each row of the board
    List<List<Cell>> cells = this.model.getBoard().getCells();
    for (int row = 0; row < this.logicalHeight; row++) {
      this.drawRow(g2d, row, xcoordOffset, ycoordOffset, cellSize, cells);
    }
    //draws grid lines to separate each cell
    g2d.setColor(Color.BLACK);
    for (int col = 0; col <= this.logicalWidth; col++) {
      int x = xcoordOffset + col * cellSize;
      g2d.drawLine(x, ycoordOffset, x, ycoordOffset + cellSize * this.logicalHeight);
    }
    for (int row = 0; row <= this.logicalHeight; row++) {
      int y = ycoordOffset + row * cellSize;
      g2d.drawLine(xcoordOffset, y, xcoordOffset + cellSize * this.logicalWidth, y);
    }
    g2d.dispose();
  }

  private void drawRow(Graphics2D g2d, int row, int xcoordOffset, int ycoordOffset,
                       int cellSize, List<List<Cell>> cells) {
    for (int col = 0; col < this.logicalWidth; col++) {
      int x = xcoordOffset + col * cellSize;
      int y = ycoordOffset + row * cellSize;
      if (col == 0 || col == this.logicalWidth - 1) {
        //shows the row scores for each player on the border of the board
        this.drawScores(g2d, row, col, x, y, cellSize);
      } else {
        int boardCol = col - 1;
        if (row < cells.size() && row < cells.get(row).size()) {
          Cell cell = cells.get(row).get(boardCol);
          if (cell instanceof PawnCell) {
            //draws the number of pawns on this cell and shows who owns it
            this.drawPawnCell(g2d, row, col, x, y, cell, cellSize);
          } else if (cell instanceof ValueCell) {
            //draws the value of the card placed on this cell and shows who owns it
            Color player = cell.getPlayer() == Player.RED ? new Color(235, 50, 50)
                    : new Color(50, 95, 230);
            g2d.setColor(player);
            g2d.fillRect(x, y, cellSize, cellSize);
            g2d.setColor(Color.BLACK);
            g2d.drawString("" + cell.getValue(), x + 50, y + 50);
          } else {
            //if a cell is selected, it becomes highlighted
            if (cellIsSelected && row == selectedCellRow && col - 1 == selectedCellCol) {
              g2d.setColor(Color.CYAN);
            } else {
              g2d.setColor(Color.GRAY);
            }
            //the cell is empty
            g2d.fillRect(x, y, cellSize, cellSize);
          }
        }
      }
    }
  }

  private void drawScores(Graphics2D g2d, int row, int col, int x, int y, int cellSize) {
    if (col == 0) {
      //for red player
      g2d.setColor(Color.WHITE);
      g2d.fillRect(x, y, cellSize, cellSize);
      g2d.setColor(Color.BLACK);
      g2d.drawString(this.model.getBoard().getRowScore(row, Player.RED) + "", x + 50, y + 50);
    } else if (col == this.logicalWidth - 1) {
      //for blue player
      g2d.setColor(Color.WHITE);
      g2d.fillRect(x, y, cellSize, cellSize);
      g2d.setColor(Color.BLACK);
      g2d.drawString(this.model.getBoard().getRowScore(row, Player.BLUE) + "", x + 50, y + 50);
    }
  }

  private void drawPawnCell(Graphics2D g2d, int row, int col,
                            int x, int y, Cell cell, int cellSize) {
    //if the cell is selected, the background is highlighted
    if (cellIsSelected && row == selectedCellRow && col - 1 == selectedCellCol) {
      g2d.setColor(Color.CYAN);
    } else {
      g2d.setColor(Color.GRAY);
    }
    g2d.fillRect(x, y, cellSize, cellSize);
    Color player = cell.getPlayer() == Player.RED ? new Color(235, 50, 50)
            : new Color(50, 95, 230);
    g2d.setColor(player);
    //centers the pawns on the cell depending on how many are there
    switch (cell.getValue()) {
      case 1:
        g2d.fillOval(x + 40, y + 40, 10, 10);
        break;
      case 2:
        g2d.fillOval(x + 20, y + 40, 10, 10);
        g2d.fillOval(x + 60, y + 40, 10, 10);
        break;
      case 3:
        g2d.fillOval(x + 40, y + 30, 10, 10);
        g2d.fillOval(x + 20, y + 50, 10, 10);
        g2d.fillOval(x + 60, y + 50, 10, 10);
        break;
      default:
    }
  }

  @Override
  public void updateDimensions(int rows, int cols, int handSize) {
    //each cell is 100 * 100 pixels
    //the panel width accounts for the score columns
    this.logicalWidth = cols + 2;
    this.logicalHeight = rows;
    Dimension dimension = new Dimension(this.logicalWidth * 100, this.logicalHeight * 100);
    this.setPreferredSize(dimension);
    this.revalidate();
  }
}
