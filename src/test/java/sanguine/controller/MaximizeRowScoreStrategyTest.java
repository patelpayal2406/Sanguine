package sanguine.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import sanguine.model.Card;
import sanguine.model.Coordinate;
import sanguine.model.InfluenceCard;
import sanguine.model.PawnCell;
import sanguine.model.Player;

/**
 * Tests for the MaximizeRowScoreStrategy class.
 * Verifies that the strategy selects moves that maximize row scores where appropriate.
 */
public class MaximizeRowScoreStrategyTest {
  private MaximizeRowScoreStrategy strategy;
  private MockSanguineModel model;
  private MockBoard board;

  /**
   * Initializing maximize row score strategy.
   */
  @Before
  public void setUp() {
    strategy = new MaximizeRowScoreStrategy();
    board = new MockBoard(3, 3);
    model = new MockSanguineModel(board, Player.RED, new ArrayList<>());
  }

  // Tests that the strategy skips rows that are already won by the player.
  @Test
  public void testSkipsWinningRow() {
    board.setScore(0, Player.RED, 5);
    board.setScore(0, Player.BLUE, 2);
    board.setCell(0, 0, new PawnCell(1, Player.RED)); // Valid spot
    
    model.setHand(List.of(createCard(Player.RED, 1, 1)));

    List<Move> moves = strategy.generateMoves(model);
    assertTrue(moves.isEmpty());
  }
  
  // Tests that the strategy skips rows where the player is tied or behind
  // if no move can improve the situation to a win.
  @Test
  public void testSkipsEqualRowIfNoWinningMove() {
    board.setScore(0, Player.RED, 2);
    board.setScore(0, Player.BLUE, 2);
    board.setCell(0, 0, new PawnCell(1, Player.RED));
    
    board.setScore(0, Player.RED, 0);
    board.setScore(0, Player.BLUE, 10);
    model.setHand(List.of(createCard(Player.RED, 1, 1)));
    
    List<Move> moves = strategy.generateMoves(model);
    assertTrue(moves.isEmpty());
  }

  // Tests that the strategy finds a move that turns a losing row into a winning row.
  @Test
  public void testWinsLosingRow() {
    board.setScore(0, Player.RED, 2);
    board.setScore(0, Player.BLUE, 5);
    board.setCell(0, 0, new PawnCell(1, Player.RED));
    
    Card winningCard = createCard(Player.RED, 1, 4);
    model.setHand(List.of(winningCard));

    List<Move> moves = strategy.generateMoves(model);
    assertEquals(1, moves.size());
    Move move = moves.get(0);
    assertEquals(0, move.getRow());
    assertEquals(0, move.getCol());
    assertEquals(winningCard, move.getCard());
  }

  // Tests that the strategy finds a move that breaks a tie to win a row.
  @Test
  public void testWinsTiedRow() {
    board.setScore(0, Player.RED, 3);
    board.setScore(0, Player.BLUE, 3);
    board.setCell(0, 0, new PawnCell(1, Player.RED));
    
    Card winningCard = createCard(Player.RED, 1, 1);
    model.setHand(List.of(winningCard));

    List<Move> moves = strategy.generateMoves(model);
    assertEquals(1, moves.size());
    assertEquals(winningCard, moves.get(0).getCard());
  }
  
  // Tests that if multiple cards can win a row, all are suggested.
  @Test
  public void testPicksAllBestCards() {
    board.setScore(0, Player.RED, 0);
    board.setScore(0, Player.BLUE, 0);
    board.setCell(0, 0, new PawnCell(1, Player.RED));
    
    Card card1 = createCard(Player.RED, 1, 2);
    Card card2 = createCard(Player.RED, 1, 3);
    model.setHand(List.of(card1, card2));
    
    List<Move> moves = strategy.generateMoves(model);
    assertEquals(2, moves.size());
    assertEquals(card1, moves.get(0).getCard());
    assertEquals(card2, moves.get(1).getCard());
  }
  
  // Tests that if a card can win a row in multiple spots, all spots are suggested.
  @Test
  public void testPicksAllBestLocations() {
    board.setScore(0, Player.RED, 0);
    board.setScore(0, Player.BLUE, 0);
    board.setCell(0, 0, new PawnCell(1, Player.RED));
    board.setCell(0, 1, new PawnCell(1, Player.RED));
    
    Card card1 = createCard(Player.RED, 1, 2);
    model.setHand(List.of(card1)); 
    
    List<Move> moves = strategy.generateMoves(model);
    assertEquals(2, moves.size());
    assertEquals(0, moves.get(0).getCol());
    assertEquals(1, moves.get(1).getCol());
  }
  
  // Tests that the strategy moves to the next row if the current one is already won.
  @Test
  public void testMovesToNextRow() {
    board.setScore(0, Player.RED, 5);
    board.setScore(0, Player.BLUE, 0);
    
    board.setScore(1, Player.RED, 0);
    board.setScore(1, Player.BLUE, 2);
    board.setCell(1, 0, new PawnCell(1, Player.RED));
    
    Card card = createCard(Player.RED, 1, 3);
    model.setHand(List.of(card));
    
    List<Move> moves = strategy.generateMoves(model);
    assertEquals(1, moves.size());
    assertEquals(1, moves.get(0).getRow());
  }
  
  // Tests that the strategy respects card costs when checking validity.
  @Test
  public void testChecksCost() {
    board.setScore(0, Player.RED, 0);
    board.setScore(0, Player.BLUE, 5);
    
    board.setCell(0, 0, new PawnCell(1, Player.RED));
    
    Card card = createCard(Player.RED, 2, 10);
    model.setHand(List.of(card));
    
    List<Move> moves = strategy.generateMoves(model);
    assertTrue(moves.isEmpty());
  }

  private Card createCard(Player p, int cost, int value) {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    return new InfluenceCard(p, "Test", cost, value, influences);
  }
}
