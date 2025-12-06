package sanguine.controller;

import sanguine.model.Player;
import sanguine.model.SanguineModel;
import sanguine.view.FeatureListener;
import sanguine.view.ModelListener;
import sanguine.view.PublisherPanel;
import sanguine.view.SanguineView;

/**
 * Controller class that takes the user inputs and sends and receives data
 * from the model and the view.
 */
public class SanguineGuiController implements SanguineController, FeatureListener, ModelListener {
  private final SanguineView playerView;
  private final SanguineModel model;
  private boolean cellWasSelected;
  private boolean cardWasSelected;
  private int selectedCellRow;
  private int selectedCellCol;
  private int selectedCardIndex;
  private final Player controllerPlayer;

  /**
   * Initializes the panels for the board and card hands and ensures there are no selections yet.
   *
   * @param playerView the view frame for a specified player
   * @param model the Sanguine model
   * @param player the player of this controller
   */
  public SanguineGuiController(SanguineView playerView, SanguineModel model, Player player) {
    if (playerView == null || model == null) {
      throw new IllegalArgumentException("View and model cannot be null");
    }
    this.playerView = playerView;
    //adds the ability for feature listening to the frame
    this.playerView.setListener(this);
    this.model = model;
    //adds the ability for listening to the model
    this.model.setListener(this);
    this.controllerPlayer = player;
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
    //shows the first player's turn
    if (Player.RED == this.controllerPlayer) {
      this.playerView.showTurn(Player.RED);
    }
  }

  @Override
  public void checkGameOver() {
    if (model.gameOver()) {
      this.gameOver(this.model.getWinner());
    }
  }

  @Override
  public void selectBoardCell(int row, int col, Player player) {
    if (player != model.getCurrentPlayer()) {
      String message = "Player " + player + " is not currently playing";
      this.playerView.showError(message);
      throw new IllegalStateException(message);
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
    this.playerView.refresh();
  }

  @Override
  public void selectCard(int cardIndex, Player player) {
    if (player != model.getCurrentPlayer()) {
      String message = "Player " + player + " is not currently playing";
      this.playerView.showError(message);
      throw new IllegalStateException(message);
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
    this.playerView.refresh();
  }

  @Override
  public void confirmMove(Player player) {
    if (!cellWasSelected || !cardWasSelected) {
      this.playerView.showError("Please select both a cell and card");
      return;
    }
    try {
      model.playCard(model.getPlayerHand(player).get(this.selectedCardIndex),
              this.selectedCellRow, this.selectedCellCol);
    } catch (IllegalArgumentException e) {
      this.playerView.showError(e.getMessage());
    }
    this.playerView.getBoardPanel().highlightCell(selectedCellRow, selectedCellCol, player, false);
    this.playerView.getHandPanel().highlightCard(selectedCardIndex, player, false);
    cellWasSelected = false;
    cardWasSelected = false;
    selectedCardIndex = -1;
    selectedCellRow = -1;
    selectedCellCol = -1;
    this.checkGameOver();
  }

  @Override
  public void passTurn(Player player) {
    this.model.pass();
    this.playerView.refresh();
    this.checkGameOver();
  }

  @Override
  public void turnChanged(Player player) {
    this.playerView.refresh();
    if (player == this.controllerPlayer) {
      this.playerView.showTurn(player);
    }
  }

  @Override
  public void gameOver(Player winner) {
    this.playerView.endGame(winner);
  }

  @Override
  public void errorOccurrence(String reason) {
    this.playerView.showError(reason);
  }

  @Override
  public void turnPassed(Player player) {
    this.playerView.refresh();
    if (player == this.controllerPlayer) {
      this.playerView.showPass(player);
    }
  }
}
