package sanguine.controller;

import java.util.ArrayList;
import java.util.List;
import sanguine.model.Board;
import sanguine.model.Card;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguineModel;
import sanguine.view.ModelListener;

/**
 * A mock implementation of the SanguineModel interface for testing purposes.
 * Allows controlling the game state and tracking interactions.
 */
public class MockSanguineModel implements SanguineModel {
  private final Board board;
  private final Player currentPlayer;
  private List<Card> currentPlayerHand;
  private final List<String> log;

  /**
   * Creates a MockSanguineModel with the given board, current player, and hand.
   *
   * @param board the mock board
   * @param currentPlayer the player whose turn it is
   * @param currentPlayerHand the list of cards in the current player's hand
   */
  public MockSanguineModel(Board board, Player currentPlayer, 
      List<Card> currentPlayerHand) {
    this.board = board;
    this.currentPlayer = currentPlayer;
    this.currentPlayerHand = currentPlayerHand;
    this.log = new ArrayList<>();
  }
  
  /**
   * Updates the current player's hand.
   *
   * @param hand the new list of cards
   */
  public void setHand(List<Card> hand) {
    this.currentPlayerHand = hand;
  }

  @Override
  public Board getBoard() {
    log.add("getBoard");
    return board;
  }

  @Override
  public Player getCurrentPlayer() {
    log.add("getCurrentPlayer");
    return currentPlayer;
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    log.add("getPlayerHand:" + player);
    // Only return hand if requested for current player
    if (player == currentPlayer) {
      return currentPlayerHand;
    }
    return new ArrayList<>();
  }

  @Override
  public Cell getCell(int row, int col) {
    log.add("getCell:" + row + "," + col);
    return null;
  }

  @Override
  public int getTotalScore(Player player) {
    log.add("getTotalScore:" + player.toString());
    return 0;
  }

  @Override
  public int getRowScore(int row, Player player) {
    log.add("getRowScore:" + row + "," + player.toString());
    return 0;
  }

  @Override
  public Player getCellOwner(int row, int col) {
    log.add("cellOwner:" + row + "," + col);
    return null;
  }

  @Override
  public String getCellContents(int row, int col) {
    log.add("getCellContents:" + row + "," + col);
    return "";
  }

  @Override
  public int getNumRows() {
    log.add("getNumRows");
    return 0;
  }

  @Override
  public int getNumCols() {
    log.add("getNumCols");
    return 0;
  }

  @Override
  public void checkValidMove(Cell currentCell, Card card) {
    log.add("checkValidMove");
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
  public boolean gameOver() {
    log.add("gameOver");
    return false;
  }

  @Override
  public Player getWinner() {
    log.add("getWinner");
    return null;
  }

  @Override
  public void playCard(Card card, int row, int col) {
    log.add("playCard:" + row + "," + col);
  }

  @Override
  public void switchPlayer() {
    log.add("switchPlayer");
  }

  @Override
  public void startGame(int rows, int cols, int handSize, boolean shuffle) {
    log.add("startGame:" + rows + "," + cols + "," + handSize + "," + shuffle);
  }

  @Override
  public void pass() {
    log.add("pass");
  }

  @Override
  public void setListener(ModelListener listener) {
    log.add("setListener:" + listener);
  }

  @Override
  public List<Card> getDeck(Player player) {
    log.add("getDeck:" + player);
    return new ArrayList<>();
  }
}
