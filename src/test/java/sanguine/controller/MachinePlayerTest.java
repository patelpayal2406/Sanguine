package sanguine.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sanguine.model.Card;
import sanguine.model.Cell;
import sanguine.model.Coordinate;
import sanguine.model.InfluenceCard;
import sanguine.model.Player;
import sanguine.model.SanguineModel;
import sanguine.view.FeatureListener;

/**
 * Tests for MachinePlayer publishing actions through its listener.
 */
public class MachinePlayerTest {

  private static class MockListener implements FeatureListener {
    private final List<String> log = new ArrayList<>();

    @Override
    public void selectBoardCell(int row, int col, Player player) {
      log.add("selectBoardCell:" + row + "," + col + "," + player);
    }

    @Override
    public void selectCard(int cardIndex, Player player) {
      log.add("selectCard:" + cardIndex + "," + player);
    }

    @Override
    public void confirmMove(Player player) {
      log.add("confirmMove:" + player);
    }

    @Override
    public void passTurn(Player player) {
      log.add("passTurn:" + player);
    }

    List<String> getLog() {
      return log;
    }
  }

  private static class MockModel implements SanguineModel {
    private final Player currentPlayer;
    private final List<Card> hand;
    private final Cell cell;
    private final boolean throwOnCheck;
    private boolean gameOver;

    MockModel(Player currentPlayer, List<Card> hand, Cell cell, boolean throwOnCheck) {
      this.currentPlayer = currentPlayer;
      this.hand = hand;
      this.cell = cell;
      this.throwOnCheck = throwOnCheck;
      this.gameOver = false;
    }

    void setGameOver(boolean over) {
      this.gameOver = over;
    }

    @Override
    public List<Card> getPlayerHand(Player player) {
      return player == currentPlayer ? hand : new ArrayList<>();
    }

    @Override
    public Player getCurrentPlayer() {
      return currentPlayer;
    }

    @Override
    public Cell getCell(int row, int col) {
      return cell;
    }

    @Override
    public void checkValidMove(Cell currentCell, Card card) {
      if (throwOnCheck) {
        throw new IllegalArgumentException("bad move");
      }
    }

    @Override
    public boolean gameOver() {
      return gameOver;
    }

    // Unused methods for this test suite
    @Override public void playCard(Card card, int row, int col) {}
    @Override public void switchPlayer() {}
    @Override public void startGame(int rows, int cols, int handSize, boolean shuffled) {}
    @Override public void pass() {}
    @Override public void setListener(sanguine.view.ModelListener listener) {}
    @Override public sanguine.model.Board getBoard() { return null; }
    @Override public List<Card> getDeck(Player player) { return new ArrayList<>(); }
    @Override public Player getWinner() { return null; }
    @Override public int getTotalScore(Player player) { return 0; }
    @Override public int getRowScore(int row, Player player) { return 0; }
    @Override public Player getCellOwner(int row, int col) { return null; }
    @Override public String getCellContents(int row, int col) { return ""; }
    @Override public int getNumRows() { return 0; }
    @Override public int getNumCols() { return 0; }
  }

  private static class MockStrategy implements Strategy {
    private final List<Move> moves;
    private int calls = 0;

    MockStrategy(List<Move> moves) {
      this.moves = moves;
    }

    int getCalls() {
      return calls;
    }

    @Override
    public List<Move> generateMoves(SanguineModel model) {
      calls++;
      return moves;
    }
  }

  @Test
  public void publishesSelectAndConfirmForValidMove() {
    Card card = new InfluenceCard(Player.RED, "C1", 1, 1, Arrays.asList(new Coordinate(0, 0)));
    Move move = new Move(card, 0, 1);
    MockStrategy strategy = new MockStrategy(Arrays.asList(move));
    MockModel model = new MockModel(Player.RED, Arrays.asList(card), new sanguine.model.PawnCell(3,
        Player.RED), false);
    MockListener listener = new MockListener();

    MachinePlayer machine = new MachinePlayer(Player.RED, strategy);
    machine.setListener(listener);

    machine.takeTurn(model);

    List<String> log = listener.getLog();
    assertEquals(3, log.size());
    assertEquals("selectCard:0,RED", log.get(0));
    assertEquals("selectBoardCell:0,1,RED", log.get(1));
    assertEquals("confirmMove:RED", log.get(2));
  }

  @Test
  public void passesWhenNoMovesGenerated() {
    MockStrategy strategy = new MockStrategy(new ArrayList<>());
    MockModel model = new MockModel(Player.RED, new ArrayList<>(), null, false);
    MockListener listener = new MockListener();

    MachinePlayer machine = new MachinePlayer(Player.RED, strategy);
    machine.setListener(listener);
    machine.takeTurn(model);

    assertEquals(Arrays.asList("passTurn:RED"), listener.getLog());
  }

  @Test
  public void passesWhenAllMovesInvalid() {
    Card card = new InfluenceCard(Player.RED, "C1", 1, 1, Arrays.asList(new Coordinate(0, 0)));
    Move move = new Move(card, 0, 1);
    MockStrategy strategy = new MockStrategy(Arrays.asList(move));
    MockModel model = new MockModel(Player.RED, Arrays.asList(card), new sanguine.model.PawnCell(3,
        Player.RED), true);
    MockListener listener = new MockListener();

    MachinePlayer machine = new MachinePlayer(Player.RED, strategy);
    machine.setListener(listener);
    machine.takeTurn(model);

    assertEquals(Arrays.asList("passTurn:RED"), listener.getLog());
  }

  @Test
  public void skipsTurnWhenNotCurrentPlayer() {
    Card card = new InfluenceCard(Player.RED, "C1", 1, 1, Arrays.asList(new Coordinate(0, 0)));
    Move move = new Move(card, 0, 1);
    MockStrategy strategy = new MockStrategy(Arrays.asList(move));
    MockModel model = new MockModel(Player.BLUE, Arrays.asList(card), new sanguine.model.PawnCell(3,
        Player.BLUE), false);
    MockListener listener = new MockListener();

    MachinePlayer machine = new MachinePlayer(Player.RED, strategy);
    machine.setListener(listener);
    machine.takeTurn(model);

    assertTrue(listener.getLog().isEmpty());
    assertEquals(0, strategy.getCalls());
  }

  @Test
  public void passesWhenCardMissingFromHand() {
    Card card = new InfluenceCard(Player.RED, "C1", 1, 1, Arrays.asList(new Coordinate(0, 0)));
    Move move = new Move(card, 0, 1);
    MockStrategy strategy = new MockStrategy(Arrays.asList(move));
    // Hand does not contain card
    MockModel model = new MockModel(Player.RED, new ArrayList<>(), new sanguine.model.PawnCell(3,
        Player.RED), false);
    MockListener listener = new MockListener();

    MachinePlayer machine = new MachinePlayer(Player.RED, strategy);
    machine.setListener(listener);
    machine.takeTurn(model);

    assertEquals(Arrays.asList("passTurn:RED"), listener.getLog());
  }
}

