package sanguine.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import sanguine.model.Card;
import sanguine.model.Cell;
import sanguine.model.Coordinate;
import sanguine.model.InfluenceCard;
import sanguine.model.PawnCell;
import sanguine.model.Player;

/**
 * Tests for the FillFirstStrategy class.
 * Verifies that the strategy simply finds any valid move.
 */
public class FillFirstStrategyTest {
  private Strategy strategy;

  /**
   * Initializing fill first strategy.
   */
  @Before
  public void setUp() {
    strategy = new FillFirstStrategy();
  }

  // Tests that the strategy returns all valid moves available.
  @Test
  public void testReturnsAllValidMoves() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    Card card2 = new InfluenceCard(Player.RED, "Card2", 2, 2, influences);
    List<Card> hand = Arrays.asList(card1, card2);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(1, Player.RED),
        new PawnCell(2, Player.RED),
        new PawnCell(3, Player.RED)
    ));
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED),
        new PawnCell(3, Player.RED),
        new PawnCell(3, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    List<Move> moves = strategy.generateMoves(model);

    assertEquals(11, moves.size());
  }

  // Tests that the strategy identifies all valid locations for a single card.
  @Test
  public void testReturnsAllValidLocationsForCards() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 3, 3, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(1, Player.RED),
        new PawnCell(2, Player.RED),
        new PawnCell(3, Player.RED)
    ));
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED),
        new PawnCell(3, Player.RED),
        new PawnCell(3, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    List<Move> moves = strategy.generateMoves(model);

    assertEquals(3, moves.size());
  }

  // Tests that the strategy returns an empty list when no valid moves exist.
  @Test
  public void testReturnsNoMovesWhenNoValidMoves() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 3, 5, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(1, Player.RED),
        new PawnCell(2, Player.RED),
        new PawnCell(2, Player.RED)
    ));
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED),
        new PawnCell(2, Player.RED),
        new PawnCell(1, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    List<Move> moves = strategy.generateMoves(model);

    assertTrue(moves.isEmpty());
  }

  // Tests that the strategy returns all possible locations and logs interactions correctly.
  @Test
  public void testReturnsAllPossibleLocations() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 2, 2, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(1, Player.RED),
        new PawnCell(2, Player.RED),
        new PawnCell(3, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    board.clearLog();
    List<Move> moves = strategy.generateMoves(model);

    assertEquals(2, moves.size());
    assertEquals(0, moves.get(0).getRow());
    assertEquals(1, moves.get(0).getCol());
    assertEquals(0, moves.get(1).getRow());
    assertEquals(2, moves.get(1).getCol());

    List<String> log = board.getLog();
    assertTrue(log.contains("getCells"));
    assertTrue(log.contains("getCell:0,1"));
    assertTrue(log.contains("getCell:0,2"));
  }

  // Tests that the strategy iterates through cards in the hand in order.
  @Test
  public void testTriesCardsInOrderAndReturnsAll() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 3, 5, influences);
    Card card2 = new InfluenceCard(Player.RED, "Card2", 2, 2, influences);
    List<Card> hand = Arrays.asList(card1, card2);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED),
        new PawnCell(2, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    List<Move> moves = strategy.generateMoves(model);

    assertEquals(2, moves.size());
    assertEquals(card2, moves.get(0).getCard());
    assertEquals(card2, moves.get(1).getCard());
  }

  // Tests that the strategy correctly skips cells owned by the opponent.
  @Test
  public void testSkipsOpponentCells() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(3, Player.BLUE),
        new PawnCell(2, Player.BLUE),
        new PawnCell(2, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    List<Move> moves = strategy.generateMoves(model);

    assertEquals(1, moves.size());
    assertEquals(0, moves.get(0).getRow());
    assertEquals(2, moves.get(0).getCol());
  }

  // Tests that no moves are generated if all available cells belong to the opponent.
  @Test
  public void testNoMovesWhenOnlyOpponentCells() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(3, Player.BLUE),
        new PawnCell(2, Player.BLUE),
        new PawnCell(3, Player.BLUE)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    List<Move> moves = strategy.generateMoves(model);

    assertTrue(moves.isEmpty());
  }
}
