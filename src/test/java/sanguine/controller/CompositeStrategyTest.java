package sanguine.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import sanguine.model.Card;
import sanguine.model.Cell;
import sanguine.model.Coordinate;
import sanguine.model.InfluenceCard;
import sanguine.model.PawnCell;
import sanguine.model.Player;

/**
 * Tests for the CompositeStrategy class.
 * Verifies that multiple strategies can be chained together effectively.
 */
public class CompositeStrategyTest {

  // Tests that the composite strategy correctly combines two strategies.
  // In this case, maximizing row score and then controlling the board.
  @Test
  public void testCombinesMaximizeAndControl() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.BLUE),
        new PawnCell(1, Player.RED),
        new PawnCell(1, Player.RED)
    ));
    cells.add(Arrays.asList(
        new PawnCell(2, Player.BLUE),
        new PawnCell(1, Player.RED),
        new PawnCell(1, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    Strategy strategy = new CompositeStrategy(
        new MaximizeRowScoreStrategy(),
        new ControlTheBoardStrategy()
    );
    List<Move> moves = strategy.generateMoves(model);

    assertEquals(1, moves.size());
    assertEquals(0, moves.get(0).getRow());
  }

  // Tests that the second strategy in the chain is used to break ties
  // when the first strategy returns multiple options.
  @Test
  public void testUsesSecondStrategyToBreakTies() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    Card card2 = new InfluenceCard(Player.RED, "Card2", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1, card2);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(3, Player.BLUE),
        new PawnCell(1, Player.RED),
        new PawnCell(1, Player.RED),
        new PawnCell(1, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    Strategy strategyChain = new CompositeStrategy(
        new MaximizeRowScoreStrategy(),
        new FillFirstStrategy()
    );
    List<Move> moves = strategyChain.generateMoves(model);

    assertEquals(1, moves.size());
    Move move = moves.get(0);
    assertEquals(0, move.getRow());
    assertEquals(1, move.getCol());
    assertEquals(card1, move.getCard());
  }

  // Tests that if the first strategy in the chain returns no moves,
  // the composite strategy also returns an empty list.
  @Test
  public void testReturnsEmptyWhenFirstStrategyReturnsEmpty() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(3, Player.BLUE),        
        new PawnCell(1, Player.RED),
        new PawnCell(1, Player.RED)));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    Strategy strategyChain = new CompositeStrategy(
        new MaximizeRowScoreStrategy(),
        new ControlTheBoardStrategy()
    );
    List<Move> moves = strategyChain.generateMoves(model);

    assertTrue(moves.isEmpty());
  }

  // Tests chaining three strategies together.
  @Test
  public void testThreeStrategyComposition() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    Card card2 = new InfluenceCard(Player.RED, "Card2", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1, card2);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED),
        new PawnCell(2, Player.RED)
    ));
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED),
        new PawnCell(2, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    Strategy complexStrategy = new CompositeStrategy(
        new ControlTheBoardStrategy(),
        new FillFirstStrategy()
    );
    List<Move> moves = complexStrategy.generateMoves(model);

    assertEquals(1, moves.size());
    Move move = moves.get(0);
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
    assertEquals(card1, move.getCard());
  }

  // Tests that the composite strategy works correctly with just a single strategy.
  @Test
  public void testSingleStrategyWorks() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    Strategy strategy = new CompositeStrategy(new FillFirstStrategy());
    List<Move> moves = strategy.generateMoves(model);

    assertEquals(1, moves.size());
  }
}
