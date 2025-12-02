package sanguine.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the InfluenceBoard class.
 */
public class InfluenceBoardTests {
  private Board board;

  /**
   * Initializes a board of 3 rows and 5 columns.
   */
  @Before
  public void setUp() {
    board = new InfluenceBoard(3, 5);
  }

  @Test
  public void testValidBoardCreation() {
    Board b = new InfluenceBoard(3, 5);
    assertNotNull(b);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRowsZero() {
    new InfluenceBoard(0, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRowsNegative() {
    new InfluenceBoard(-1, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColumnsZero() {
    new InfluenceBoard(3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColumnsOne() {
    new InfluenceBoard(3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColumnsEven() {
    new InfluenceBoard(3, 4);
  }

  @Test
  public void testValidOddColumns() {
    new InfluenceBoard(5, 7);
  }

  @Test
  public void testGetCellInitialPawns() {
    assertNotNull(board.getCell(0, 0));
    assertTrue(board.getCell(0, 0) instanceof PawnCell);
    assertEquals(Player.RED, board.getCell(0, 0).getPlayer());
    
    assertNotNull(board.getCell(0, 4));
    assertTrue(board.getCell(0, 4) instanceof PawnCell);
    assertEquals(Player.BLUE, board.getCell(0, 4).getPlayer());
    
    assertNull(board.getCell(1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCellInvalidRowNegative() {
    board.getCell(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCellInvalidRowTooLarge() {
    board.getCell(3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCellInvalidColNegative() {
    board.getCell(0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCellInvalidColTooLarge() {
    board.getCell(0, 5);
  }

  @Test
  public void testGetScoreEmptyBoard() {
    assertEquals(0, board.getRowScore(0, Player.RED));
    assertEquals(0, board.getRowScore(1, Player.BLUE));
  }

  @Test
  public void testPlayCardBasic() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    Card card = new InfluenceCard(Player.RED, "TestCard", 1, 3, influences);
    
    board.playCard(card, 0, 0);
    
    assertTrue(board.getCell(0, 0) instanceof ValueCell);
    assertEquals(3, board.getCell(0, 0).getValue());
    assertEquals(Player.RED, board.getCell(0, 0).getPlayer());
    
    assertTrue(board.getCell(0, 1) instanceof PawnCell);
    assertEquals(1, board.getCell(0, 1).getValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayCardOnEmptyCell() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    Card card = new InfluenceCard(Player.RED, "TestCard", 1, 1, influences);
    board.playCard(card, 1, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayCardInvalidRowCol() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    Card card = new InfluenceCard(Player.RED, "TestCard", 1, 1, influences);
    board.playCard(card, 10, 10);
  }

  @Test
  public void testGetBoardStringEmptyBoard() {
    String boardStr = board.getBoard();
    assertNotNull(boardStr);
    assertTrue(boardStr.contains("_"));
  }

  @Test
  public void testGetScoreWithCards() {
    final List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    final Card card1 = new InfluenceCard(Player.RED, "Card1", 1, 2, influences);
    final Card card2 = new InfluenceCard(Player.BLUE, "Card2", 1, 3, influences);
    
    board.playCard(card1, 0, 0);
    assertEquals(2, board.getRowScore(0, Player.RED));
    assertEquals(0, board.getRowScore(0, Player.BLUE));
    
    board.playCard(card2, 0, 4);
    assertEquals(2, board.getRowScore(0, Player.RED));
    assertEquals(3, board.getRowScore(0, Player.BLUE));
  }
}

