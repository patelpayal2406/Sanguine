package sanguine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import sanguine.model.Board;
import sanguine.model.Card;
import sanguine.model.Coordinate;
import sanguine.model.InfluenceBoard;
import sanguine.model.InfluenceCard;
import sanguine.model.PawnCell;
import sanguine.model.Player;
import sanguine.model.SanguineGame;
import sanguine.model.SanguineModel;

/**
 * Integration tests that test multiple components working together.
 */
public class SanguineTests {

  @Test
  public void testCompleteGameSetup() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    influences.add(new Coordinate(1, 0));

    List<Card> redDeck = new ArrayList<>();
    List<Card> blueDeck = new ArrayList<>();

    for (int i = 0; i < 15; i++) {
      redDeck.add(new InfluenceCard(Player.RED, "RedCard" + i, 1, 1, influences));
      blueDeck.add(new InfluenceCard(Player.BLUE, "BlueCard" + i, 1, 1, influences));
    }

    SanguineModel game = new SanguineGame(redDeck, blueDeck);
    game.startGame(3, 5, 5, false);

    assertEquals(Player.RED, game.getCurrentPlayer());
    assertFalse(game.gameOver());
  }

  @Test
  public void testPlayerSwitchingMultipleTimes() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));

    List<Card> redDeck = new ArrayList<>();
    List<Card> blueDeck = new ArrayList<>();

    for (int i = 0; i < 15; i++) {
      redDeck.add(new InfluenceCard(Player.RED, "Card" + i, 1, 1, influences));
      blueDeck.add(new InfluenceCard(Player.BLUE, "Card" + i, 1, 1, influences));
    }

    SanguineModel game = new SanguineGame(redDeck, blueDeck);
    game.startGame(3, 5, 5, false);

    assertEquals(Player.RED, game.getCurrentPlayer());
    for (int i = 0; i < 10; i++) {
      game.switchPlayer();
      if (i % 2 == 0) {
        assertEquals(Player.BLUE, game.getCurrentPlayer());
      } else {
        assertEquals(Player.RED, game.getCurrentPlayer());
      }
    }
  }

  @Test
  public void testDeckWithVariousCostCards() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));

    List<Card> deck = new ArrayList<>();
    deck.add(new InfluenceCard(Player.RED, "Cheap1", 1, 1, influences));
    deck.add(new InfluenceCard(Player.RED, "Cheap2", 1, 2, influences));
    deck.add(new InfluenceCard(Player.RED, "Medium1", 2, 3, influences));
    deck.add(new InfluenceCard(Player.RED, "Medium2", 2, 2, influences));
    deck.add(new InfluenceCard(Player.RED, "Expensive", 3, 5, influences));

    assertEquals(5, deck.size());
    assertEquals(1, deck.get(0).getCost());
    assertEquals(2, deck.get(2).getCost());
    assertEquals(3, deck.get(4).getCost());
  }

  @Test
  public void testScoresOnEmptyBoard() {
    Board board = new InfluenceBoard(3, 5);
    
    for (int row = 0; row < 3; row++) {
      assertEquals(0, board.getRowScore(row, Player.RED));
      assertEquals(0, board.getRowScore(row, Player.BLUE));
    }
  }

  @Test
  public void testWinnerCalculation() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));

    List<Card> redDeck = new ArrayList<>();
    List<Card> blueDeck = new ArrayList<>();

    for (int i = 0; i < 15; i++) {
      redDeck.add(new InfluenceCard(Player.RED, "RedCard" + i, 1, 2, influences));
      blueDeck.add(new InfluenceCard(Player.BLUE, "BlueCard" + i, 1, 1, influences));
    }

    SanguineModel game = new SanguineGame(redDeck, blueDeck);
    game.startGame(3, 5, 5, false);

    game.playCard(redDeck.get(0), 0, 0);
    game.playCard(blueDeck.get(0), 0, 4);
    
    game.pass();
    game.pass();

    assertTrue(game.gameOver());
    assertEquals(Player.RED, game.getWinner());
  }

  @Test
  public void testInfluenceCappedAtThreePawns() {
    final Board board = new InfluenceBoard(3, 5);
    List<Coordinate> influences1 = new ArrayList<>();
    influences1.add(new Coordinate(0, 1));
    influences1.add(new Coordinate(1, 0));
    
    List<Coordinate> influences2 = new ArrayList<>();
    influences2.add(new Coordinate(-1, 1));
    
    List<Coordinate> influences3 = new ArrayList<>();
    influences3.add(new Coordinate(0, -1));

    final Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 1, influences1);
    final Card card2 = new InfluenceCard(Player.RED, "Card2", 1, 1, influences2);
    final Card card3 = new InfluenceCard(Player.RED, "Card3", 1, 1, influences3);

    board.playCard(card1, 0, 0);
    assertTrue(board.getCell(1, 0) instanceof PawnCell);
    assertEquals(2, board.getCell(1, 0).getValue());

    board.playCard(card2, 0, 1);
    assertTrue(board.getCell(1, 0) instanceof PawnCell);
    assertEquals(3, board.getCell(1, 0).getValue());

    board.playCard(card3, 2, 0);
    assertTrue(board.getCell(1, 0) instanceof PawnCell);
    assertEquals(3, board.getCell(1, 0).getValue());
  }
}
