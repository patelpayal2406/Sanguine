package sanguine.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import sanguine.model.Player;
import sanguine.model.SanguineModel;
import sanguine.view.FeatureListener;

/**
 * Tests for HumanPlayer behaviors.
 */
public class HumanPlayerTest {

  private static class MockFeatureListener implements FeatureListener {
    @Override
    public void selectBoardCell(int row, int col, Player p) {}

    @Override
    public void selectCard(int cardIndex, Player p) {}

    @Override
    public void confirmMove(Player p) {}

    @Override
    public void passTurn(Player p) {}
  }

  private static class MockModel implements SanguineModel {
    private final Player currentPlayer;

    MockModel(Player currentPlayer) {
      this.currentPlayer = currentPlayer;
    }

    @Override
    public void playCard(sanguine.model.Card card, int row, int col) {
    }

    @Override
    public void switchPlayer() {
    }

    @Override
    public void startGame(int rows, int cols, int handSize, boolean shuffled) {
    }

    @Override
    public void pass() {
    }

    @Override
    public void setListener(sanguine.view.ModelListener listener) {
    }

    @Override
    public boolean gameOver() {
      return false;
    }

    @Override
    public Player getWinner() {
      return null;
    }

    @Override
    public Player getCurrentPlayer() {
      return currentPlayer;
    }

    @Override
    public sanguine.model.Board getBoard() {
      return null;
    }

    @Override
    public java.util.List<sanguine.model.Card> getDeck(Player player) {
      return null;
    }

    @Override
    public java.util.List<sanguine.model.Card> getPlayerHand(Player player) {
      return null;
    }

    @Override
    public sanguine.model.Cell getCell(int row, int column) {
      return null;
    }

    @Override
    public void checkValidMove(sanguine.model.Cell currentCell, sanguine.model.Card card) {
    }

    @Override
    public int getTotalScore(Player player) {
      return 0;
    }

    @Override
    public int getRowScore(int row, Player player) {
      return 0;
    }

    @Override
    public Player getCellOwner(int row, int col) {
      return null;
    }

    @Override
    public String getCellContents(int row, int col) {
      return null;
    }

    @Override
    public int getNumRows() {
      return 0;
    }

    @Override
    public int getNumCols() {
      return 0;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectsNullPlayer() {
    new HumanPlayer(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectsNullListener() {
    HumanPlayer player = new HumanPlayer(Player.RED);
    player.setListener(null);
  }

  @Test
  public void exposesPlayerAndIsNotAutomated() {
    HumanPlayer player = new HumanPlayer(Player.BLUE);
    player.setListener(new MockFeatureListener());
    assertEquals(Player.BLUE, player.getPlayer());
    assertFalse(player.isAutomated());
  }

  @Test
  public void takeTurnDoesNothingForHuman() {
    HumanPlayer player = new HumanPlayer(Player.RED);
    player.takeTurn(new MockModel(Player.RED));
    assertFalse(player.isAutomated());
  }
}

