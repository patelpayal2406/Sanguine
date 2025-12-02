package sanguine.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the Coordinate class.
 */
public class CoordinateTests {

  @Test
  public void testCoordinateCreation() {
    Coordinate c = new Coordinate(1, 2);
    assertEquals(1, c.getX());
    assertEquals(2, c.getY());
  }
  
  @Test
  public void testMultipleCoordinates() {
    final Coordinate c1 = new Coordinate(1, 0);
    final Coordinate c2 = new Coordinate(0, 1);
    final Coordinate c3 = new Coordinate(-1, 0);
    final Coordinate c4 = new Coordinate(0, -1);
    
    assertEquals(1, c1.getX());
    assertEquals(0, c1.getY());
    assertEquals(0, c2.getX());
    assertEquals(1, c2.getY());
    assertEquals(-1, c3.getX());
    assertEquals(0, c3.getY());
    assertEquals(0, c4.getX());
    assertEquals(-1, c4.getY());
  }
}

