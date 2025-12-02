package sanguine.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Tests for the InfluenceCard class.
 */
public class InfluenceCardTests {

  @Test
  public void testValidCardCreation() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    influences.add(new Coordinate(1, 0));
    
    Card card = new InfluenceCard(Player.RED, "Security", 1, 2, influences);
    
    assertEquals(Player.RED, card.getPlayer());
    assertEquals("Security", card.getName());
    assertEquals(1, card.getCost());
    assertEquals(2, card.getValue());
    assertEquals(2, card.getInfluence().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPlayer() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    new InfluenceCard(null, "Card", 1, 1, influences);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullName() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    new InfluenceCard(Player.RED, null, 1, 1, influences);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyName() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    new InfluenceCard(Player.RED, "", 1, 1, influences);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroCost() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    new InfluenceCard(Player.RED, "Card", 0, 1, influences);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeCost() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    new InfluenceCard(Player.RED, "Card", -1, 1, influences);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTooHighCost() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    new InfluenceCard(Player.RED, "Card", 4, 1, influences);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroValue() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    new InfluenceCard(Player.RED, "Card", 1, 0, influences);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeValue() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    new InfluenceCard(Player.RED, "Card", 1, -1, influences);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullInfluences() {
    new InfluenceCard(Player.RED, "Card", 1, 1, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyInfluences() {
    new InfluenceCard(Player.RED, "Card", 1, 1, new ArrayList<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInfluenceWithNullCoordinate() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(1, 0));
    influences.add(null);
    new InfluenceCard(Player.RED, "Card", 1, 1, influences);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInfluenceAtCenter() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(0, 0));
    new InfluenceCard(Player.RED, "Card", 1, 1, influences);
  }

  @Test
  public void testBluePlayerMirrorsInfluence() {
    List<Coordinate> influences = new ArrayList<>();
    influences.add(new Coordinate(0, 1));
    influences.add(new Coordinate(1, 0));
    influences.add(new Coordinate(-1, 1));
    
    Card redCard = new InfluenceCard(Player.RED, "Card", 1, 1, influences);
    Card blueCard = new InfluenceCard(Player.BLUE, "Card", 1, 1, influences);
    
    List<Coordinate> redInfluences = redCard.getInfluence();
    List<Coordinate> blueInfluences = blueCard.getInfluence();
    
    assertEquals(3, redInfluences.size());
    assertEquals(3, blueInfluences.size());
    
    // Blue mirrors X coordinates
    assertEquals(0, redInfluences.get(0).getX());
    assertEquals(1, redInfluences.get(0).getY());
    assertEquals(0, blueInfluences.get(0).getX());
    assertEquals(1, blueInfluences.get(0).getY());
    
    assertEquals(1, redInfluences.get(1).getX());
    assertEquals(0, redInfluences.get(1).getY());
    assertEquals(-1, blueInfluences.get(1).getX());
    assertEquals(0, blueInfluences.get(1).getY());
  }
}
