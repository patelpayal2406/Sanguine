package sanguine.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the SanguineModel interface and SanguineGame implementation.
 */
public class SanguineModelTests {
  private SanguineModel game;
  private List<Card> redDeck;
  private List<Card> blueDeck;

  /**
   * Initializes a deck for each player and creates the model with those decks.
   */
  @Before
  public void setUp() {
    redDeck = createSimpleDeck(Player.RED);
    blueDeck = createSimpleDeck(Player.BLUE);
    game = new SanguineGame(redDeck, blueDeck);
  }

  private List<Card> createSimpleDeck(Player player) {
    List<Card> deck = new ArrayList<>();
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    influences.add(new Coordinate(0, 1));

    for (int i = 0; i < 15; i++) {
      deck.add(new InfluenceCard(player, "Card" + i, 1, 1, influences));
    }
    return deck;
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithMismatchedPlayerInRedDeck() {
    List<Card> badRedDeck = new ArrayList<>();
    badRedDeck.add(new InfluenceCard(Player.BLUE, "BadCard", 1, 1,
            List.of(new Coordinate(1, 0))));
    new SanguineGame(badRedDeck, blueDeck);
  }

  @Test
  public void testStartGameWithValidDimensions() {
    game.startGame(3, 5, 5, false);
    assertEquals(Player.RED, game.getCurrentPlayer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameWithInvalidRows() {
    game.startGame(0, 5, 5, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameWithEvenColumns() {
    game.startGame(3, 4, 5, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameWithTooFewColumns() {
    game.startGame(3, 1, 5, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCurrentPlayerBeforeGameStart() {
    game.getCurrentPlayer();
  }

  @Test
  public void testSwitchPlayer() {
    game.startGame(3, 5, 5, false);
    assertEquals(Player.RED, game.getCurrentPlayer());
    game.switchPlayer();
    assertEquals(Player.BLUE, game.getCurrentPlayer());
    game.switchPlayer();
    assertEquals(Player.RED, game.getCurrentPlayer());
  }

  @Test(expected = IllegalStateException.class)
  public void testSwitchPlayerBeforeGameStart() {
    game.switchPlayer();
  }

  @Test
  public void testGameOverReturnsFalse() {
    game.startGame(3, 5, 5, false);
    assertFalse(game.gameOver());
  }

  @Test
  public void testGameOverAfterTwoPasses() {
    game.startGame(3, 5, 5, false);
    assertFalse(game.gameOver());
    game.pass();
    assertFalse(game.gameOver());
    game.pass();
    assertTrue(game.gameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testGameOverBeforeGameStart() {
    game.gameOver();
  }

  @Test
  public void testGetWinnerReturnsNullBeforeGameOver() {
    game.startGame(3, 5, 5, false);
    assertNull(game.getWinner());
  }

  @Test
  public void testGetWinnerAfterTiedGame() {
    game.startGame(3, 5, 5, false);
    game.pass();
    game.pass();
    assertTrue(game.gameOver());
    assertNull(game.getWinner());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetWinnerBeforeGameStart() {
    game.getWinner();
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayCardBeforeGameStart() {
    Card card = redDeck.get(0);
    game.playCard(card, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayCardWrongPlayer() {
    game.startGame(3, 5, 5, false);
    Card blueCard = blueDeck.get(0);
    game.playCard(blueCard, 0, 0);
  }

  @Test
  public void testPassFunctionality() {
    game.startGame(3, 5, 5, false);
    assertEquals(Player.RED, game.getCurrentPlayer());
    game.pass();
    assertEquals(Player.BLUE, game.getCurrentPlayer());
  }

  @Test
  public void testGetBoard() {
    game.startGame(3, 5, 5, false);
    assertNotNull(game.getBoard());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetBoardBeforeGameStart() {
    game.getBoard();
  }

  @Test
  public void testPassResetsAfterPlayCard() {
    game.startGame(3, 5, 5, false);
    game.pass();
    assertFalse(game.gameOver());
    Card card = blueDeck.get(0);
    game.playCard(card, 0, 4);
    game.pass();
    assertFalse(game.gameOver());
    game.pass();
    assertTrue(game.gameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testStartGameTwice() {
    game.startGame(3, 5, 5, false);
    game.startGame(3, 5, 5, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckValidMove() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    influences.add(new Coordinate(0, 1));
    redDeck.add(new InfluenceCard(Player.RED, "Card16", 2, 1, influences));
    game.startGame(3, 5, 5, false);
    game.checkValidMove(game.getCell(0, 0), blueDeck.get(0));
    game.checkValidMove(game.getCell(0, 2), redDeck.get(0));
    game.checkValidMove(game.getCell(0, 0), redDeck.get(15));
  }

  @Test
  public void testGetCell() {
    game.startGame(3, 5, 5, false);
    assertTrue(game.getCell(0, 0).equals(new PawnCell(1, Player.RED)));
    game.playCard(redDeck.get(1), 0, 0);
    assertTrue(game.getCell(0, 0).equals(new ValueCell(1, Player.RED)));
    assertNull(game.getCell(0, 2));
  }

  @Test
  public void testScores() {
    game.startGame(3, 5, 5, false);
    assertEquals(0, game.getRowScore(0, Player.RED));
    game.playCard(redDeck.get(1), 0, 0);
    assertEquals(1, game.getRowScore(0, Player.RED));
    game.playCard(blueDeck.get(1), 0, 4);
    game.playCard(redDeck.get(1), 0, 1);
    assertEquals(2, game.getTotalScore(Player.RED));

  }

  @Test
  public void testGetCellOwner() {
    game.startGame(3, 5, 5, false);
    assertEquals(Player.RED, game.getCellOwner(0, 0));
    game.playCard(redDeck.get(1), 0, 0);
    game.playCard(blueDeck.get(1), 0, 4);
    assertEquals(Player.BLUE, game.getCellOwner(0, 4));
    assertNull(game.getCellOwner(0, 2));
  }

  @Test
  public void testGetCellContents() {
    game.startGame(3, 5, 5, false);
    assertEquals("PawnCell BLUE 1", game.getCellContents(0, 4));
    game.playCard(redDeck.get(1), 0, 0);
    assertEquals("ValueCell RED 1", game.getCellContents(0, 0));
    assertEquals("null cell", game.getCellContents(0, 2));
  }

  @Test
  public void testGetBoardSize() {
    game.startGame(3, 5, 5, false);
    assertEquals(3, game.getNumRows());
    assertEquals(5, game.getNumCols());
  }
}
