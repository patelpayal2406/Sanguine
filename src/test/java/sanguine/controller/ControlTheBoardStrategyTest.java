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
 * Tests for the ControlTheBoardStrategy class.
 * Verifies that the strategy selects moves maximizing board ownership.
 */
public class ControlTheBoardStrategyTest {
  private Strategy strategy;

  /**
   * Initializing control the board strategy.
   */
  @Before
  public void setUp() {
    strategy = new ControlTheBoardStrategy();
  }

  // Tests that the strategy chooses the move that results in the maximum ownership.
  @Test
  public void testChoosesMoveThatGivesMaxOwnership() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(3, Player.RED),
        new PawnCell(2, Player.RED), new PawnCell(1, Player.RED)));
    cells.add(Arrays.asList(
        new PawnCell(3, Player.RED),
        new PawnCell(2, Player.RED),
        new PawnCell(1, Player.RED)));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    List<Move> moves = strategy.generateMoves(model);

    assertEquals(6, moves.size());
    Move move = moves.get(0);
    assertEquals(card1, move.getCard());
  }

  // Tests that when multiple moves yield the same max ownership,
  // the uppermost one is preferred (based on iteration order).
  @Test
  public void testChoosesUppermostWhenTied() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED), new PawnCell(2, Player.RED)));
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED), new PawnCell(2, Player.RED)));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    List<Move> moves = strategy.generateMoves(model);

    assertEquals(4, moves.size());
    Move move = moves.get(0);
    assertEquals(0, move.getRow());
  }

  // Tests that when multiple moves in the same row yield the same max ownership,
  // the leftmost one is preferred.
  @Test
  public void testChoosesLeftmostWhenRowTied() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED), new PawnCell(2, Player.RED), new PawnCell(2, Player.RED)));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    List<Move> moves = strategy.generateMoves(model);

    assertEquals(3, moves.size());
    Move move = moves.get(0);
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
  }

  // Tests that when multiple cards yield the same max ownership at the same spot,
  // the first card in hand is preferred.
  @Test
  public void testChoosesLeftmostCardWhenTied() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 2, 2, influences);
    Card card2 = new InfluenceCard(Player.RED, "Card2", 2, 2, influences);
    List<Card> hand = Arrays.asList(card1, card2);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED), new PawnCell(2, Player.RED)));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    List<Move> moves = strategy.generateMoves(model);

    assertEquals(4, moves.size());
    Move move = moves.get(0);
    assertEquals(card1, move.getCard());
  }

  // Tests that if there are no valid moves, an empty list is returned.
  @Test
  public void testReturnsNoMovesWhenNoValidMoves() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 3, 5, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED),
        new PawnCell(2, Player.RED)));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    List<Move> moves = strategy.generateMoves(model);

    assertTrue(moves.isEmpty());
  }

  // Tests that the strategy only considers cells owned by the current player.
  @Test
  public void testOnlyConsidersCurrentPlayerCells() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.BLUE),
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
}
