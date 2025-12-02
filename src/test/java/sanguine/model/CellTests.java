package sanguine.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for Cell implementations (PawnCell and ValueCell).
 */
public class CellTests {

  @Test
  public void testPawnCellCreation() {
    Cell cell = new PawnCell(1, Player.RED);
    assertEquals(1, cell.getValue());
    assertEquals(Player.RED, cell.getPlayer());
  }
  
  @Test
  public void testValueCellCreation() {
    Cell cell = new ValueCell(5, Player.RED);
    assertEquals(5, cell.getValue());
    assertEquals(Player.RED, cell.getPlayer());
  }

  @Test
  public void testDifferentCellTypesEqualValues() {
    Cell pawnCell = new PawnCell(2, Player.RED);
    Cell valueCell = new ValueCell(2, Player.RED);
    
    assertTrue(pawnCell instanceof PawnCell);
    assertTrue(valueCell instanceof ValueCell);
    assertFalse(pawnCell instanceof ValueCell);
    assertFalse(valueCell instanceof PawnCell);
    assertEquals(pawnCell.getValue(), valueCell.getValue());
    assertEquals(pawnCell.getPlayer(), valueCell.getPlayer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPawnCellWithZeroPawns() {
    new PawnCell(0, Player.RED);
  }

  @Test
  public void testPawnCellWithMaxPawns() {
    Cell cell = new PawnCell(3, Player.BLUE);
    assertEquals(3, cell.getValue());
    assertEquals(Player.BLUE, cell.getPlayer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPawnCellWithNegativePawns() {
    new PawnCell(-1, Player.RED);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPawnCellWithTooManyPawns() {
    new PawnCell(4, Player.RED);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPawnCellWithNullPlayer() {
    new PawnCell(1, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueCellWithZeroValue() {
    new ValueCell(0, Player.RED);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueCellWithNegativeValue() {
    new ValueCell(-1, Player.RED);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueCellWithNullPlayer() {
    new ValueCell(1, null);
  }
}

