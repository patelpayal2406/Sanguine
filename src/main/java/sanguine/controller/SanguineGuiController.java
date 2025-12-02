package sanguine.controller;

import sanguine.model.Player;
import sanguine.model.SanguineModel;
import sanguine.view.BoardPanel;
import sanguine.view.FeatureListener;
import sanguine.view.HandPanel;
import sanguine.view.PublisherPanel;
import sanguine.view.SanguineView;

/**
 * Controller class that takes the user inputs and sends and receives data
 * from the model and the view.
 */
public class SanguineGuiController implements SanguineController, FeatureListener {
  private final SanguineView playerView;
  private final SanguineModel model;
  private boolean cellWasSelected;
  private boolean cardWasSelected;
  private int selectedCellRow;
  private int selectedCellCol;
  private int selectedCardIndex;

  /**
   * Initializes the panels for the board and card hands and ensures there are no selections yet.
   *
   * @param playerView the view frame for a specified player
   * @param model the Sanguine model
   */
  public SanguineGuiController(SanguineView playerView, SanguineModel model) {
    if (playerView == null || model == null) {
      throw new IllegalArgumentException("View and model cannot be null");
    }
    this.playerView = playerView;
    //adds the ability for feature listening to the frame
    this.playerView.setListener(this);
    this.model = model;
    this.cellWasSelected = false;
    this.cardWasSelected = false;
    this.selectedCellRow = -1;
    this.selectedCellCol = -1;
    this.selectedCardIndex = -1;
  }

  @Override
  public void playGame(int rows, int cols, int handSize, boolean shuffle) {
    //calls the model so game can be initialized and methods can be called
    try {
      this.model.startGame(rows, cols, handSize, shuffle);
    } catch (IllegalStateException e) {
      // game already started
    }
    //adjusts the size of the frame and panels based on the user inputs
    this.playerView.refreshSize(rows, cols, handSize);
    //makes the frame visible as soon as it is adjusted
    this.playerView.makeVisible();
  }

  @Override
  public void checkGameOver() {
    if (model.gameOver()) {
      this.playerView.endGame();
      this.playerView.showWinner(model.getWinner());
    }
  }

  @Override
  public void selectBoardCell(int row, int col, Player player) {
    if (player != model.getCurrentPlayer()) {
      throw new IllegalArgumentException("Player is not the current player");
    }
    PublisherPanel boardPanel = this.playerView.getBoardPanel();
    boolean isSameCell = this.selectedCellRow == row
            && this.selectedCellCol == col;
    //cell is deselected/unhighlighted if the given cell and the selected cell are the same
    // and the cell was already selected/highlighted
    if (isSameCell && cellWasSelected) {
      this.selectedCellRow = -1;
      this.selectedCellCol = -1;
      this.cellWasSelected = false;
      boardPanel.highlightCell(row, col, player, false);
    } else {
      //if the cell is not already selected, the cell will now be selected/highlighted
      this.cellWasSelected = true;
      this.selectedCellRow = row;
      this.selectedCellCol = col;
      boardPanel.highlightCell(row, col, player, true);
    }
  }

  @Override
  public void selectCard(int cardIndex, Player player) {
    if (player != model.getCurrentPlayer()) {
      throw new IllegalArgumentException("Player is not the current player");
    }
    PublisherPanel handPanel = this.playerView.getHandPanel();
    boolean isSameCard = this.selectedCardIndex == cardIndex;
    //card is deselected/unhighlighted if the card at the given index and the selected card
    //are the same and the card was already selected/highlighted
    if (isSameCard && cardWasSelected) {
      this.selectedCardIndex = -1;
      this.cardWasSelected = false;
      handPanel.highlightCard(cardIndex, player, false);
    } else {
      //if the card is not already selected, the card will now be selected/highlighted
      this.cardWasSelected = true;
      this.selectedCardIndex = cardIndex;
      handPanel.highlightCard(cardIndex, player, true);
    }
  }

  @Override
  public void confirmMove(Player player) {
    if (!cellWasSelected || !cardWasSelected) {
      System.out.println("Please select both a cell and card");
      return;
    }
    model.playCard(model.getPlayerHand(player).get(this.selectedCardIndex),
            this.selectedCellRow, this.selectedCellCol);
    playerView.refresh();
    SanguineView red = playerView;
    red.getBoardPanel().highlightCell(selectedCellRow, selectedCellCol, player, false);
    red.getHandPanel().highlightCard(selectedCardIndex, player, false);
    cellWasSelected = false;
    cardWasSelected = false;
    selectedCardIndex = -1;
    selectedCellRow = -1;
    selectedCellCol = -1;
    this.checkGameOver();
  }

  @Override
  public void passTurn(Player player) {
    model.pass();
    this.playerView.refresh();
    this.checkGameOver();
  }
}
