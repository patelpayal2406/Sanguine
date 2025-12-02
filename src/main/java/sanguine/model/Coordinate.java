package sanguine.model;

import java.util.Objects;

/**
 * Represents the coordinates of cells influenced by a card.
 */
public class Coordinate {
  private final int posnX;
  private final int posnY;

  /**
   * Initializes the x and y coordinates of an influence.
   *
   * @param valueX the x value of a coordinate
   * @param valueY the y value of a coordinate
   */
  public Coordinate(int valueX, int valueY) {
    this.posnX = valueX;
    this.posnY = valueY;
  }

  public int getX() {
    return posnX;
  }

  public int getY() {
    return posnY;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Coordinate)) {
      return false;
    }
    Coordinate that = (Coordinate) o;
    return posnX == that.posnX && posnY == that.posnY;
  }

  @Override
  public int hashCode() {
    return Objects.hash(posnX, posnY);
  }
}
