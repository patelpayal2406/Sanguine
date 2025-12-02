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
 * Tests for the MinimaxStrategy class.
 * Verifies that the strategy minimizes the opponent's best response.
 */
public class MinimaxStrategyTest {

  // Tests that the strategy chooses the move that leaves the opponent with the
  // fewest/weakest options.
  @Test
  public void testChoosesMoveWithFewestOpponentOptions() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    Card card2 = new InfluenceCard(Player.RED, "Card2", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1, card2);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED),
        new PawnCell(2, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    Strategy opponentStrategy = new FillFirstStrategy();
    Strategy strategy = new MinimaxStrategy(opponentStrategy);
    List<Move> moves = strategy.generateMoves(model);

    assertEquals(1, moves.size());
  }

  // Tests that the strategy uses the provided opponent strategy to evaluate potential outcomes.
  @Test
  public void testUsesOpponentStrategyToEvaluate() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED),
        new PawnCell(2, Player.RED),
        new PawnCell(2, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    Strategy opponentStrategy = new MaximizeRowScoreStrategy();
    Strategy strategy = new MinimaxStrategy(opponentStrategy);
    List<Move> moves = strategy.generateMoves(model);

    assertEquals(1, moves.size());
  }

  // Tests that the strategy returns an empty list if no valid moves exist.
  @Test
  public void testReturnsEmptyWhenNoValidMoves() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 3, 5, influences);
    List<Card> hand = Arrays.asList(card1);

    List<List<Cell>> cells = new ArrayList<>();
    cells.add(Arrays.asList(
        new PawnCell(2, Player.RED),
        new PawnCell(2, Player.RED)
    ));

    MockBoard board = new MockBoard(cells);
    MockSanguineModel model = new MockSanguineModel(board, Player.RED, hand);

    Strategy opponentStrategy = new FillFirstStrategy();
    Strategy strategy = new MinimaxStrategy(opponentStrategy);
    List<Move> moves = strategy.generateMoves(model);

    assertTrue(moves.isEmpty());
  }

  // Tests that the Minimax strategy can be part of a CompositeStrategy.
  @Test
  public void testCanBeComposedWithOtherStrategies() {
    List<Coordinate> influences = Arrays.asList(new Coordinate(1, 0));
    Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences);
    List<Card> hand = Arrays.asList(card1);

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

    Strategy opponentStrategy = new ControlTheBoardStrategy();
    Strategy minimaxStrategy = new MinimaxStrategy(opponentStrategy);
    Strategy compositeStrategy = new CompositeStrategy(
        minimaxStrategy,
        new FillFirstStrategy()
    );
    
    List<Move> moves = compositeStrategy.generateMoves(model);

    assertEquals(1, moves.size());
  }

  // Tests that the strategy only considers moves on cells owned by the current player.
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

    Strategy opponentStrategy = new FillFirstStrategy();
    Strategy strategy = new MinimaxStrategy(opponentStrategy);
    List<Move> moves = strategy.generateMoves(model);

    assertEquals(1, moves.size());
    assertEquals(0, moves.get(0).getRow());
    assertEquals(2, moves.get(0).getCol());
  }
}
