package sanguine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a card that influences the cells of the board. The influence coordinates are mirrored
 * over the y-axis for blue player's cards.
 */
public class InfluenceCard implements Card {
  private final Player player;
  private final String name;
  private final int cost;
  private final int value;
  private final List<Coordinate> influences;

  /**
   * Creates an influence card that is available for user-players in the Sanguine game.
   *
   * @param player Either a red or blue player
   * @param name The type of influence card in a deck
   * @param cost How many pawns are required to use this card
   * @param value The value of the card that is added to the row score
   * @param influences A list of squares that are influenced relative to the card
   * @throws IllegalArgumentException if arguments are invalid
   */
  public InfluenceCard(Player player, String name, int cost,
                       int value, List<Coordinate> influences) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
    if (cost <= 0 || cost > 3) {
      throw new IllegalArgumentException("Cost must be greater than 0 and less than or equal to 3");
    }
    if (value <= 0) {
      throw new IllegalArgumentException("Value must be greater than 0");
    }
    if (influences == null || influences.isEmpty()) {
      throw new IllegalArgumentException("Coordinates cannot be null or empty");
    }
    for (Coordinate coordinate : influences) {
      if (coordinate == null) {
        throw new IllegalArgumentException("Coordinates cannot be null");
      }
      if (coordinate.getX() == 0 && coordinate.getY() == 0) {
        throw new IllegalArgumentException("Coordinates cannot be at the center");
      }
    }
    this.player = player;
    this.name = name;
    this.cost = cost;
    this.value = value;
    //mirrors the influences for the blue card deck
    this.influences = player == Player.RED ? influences : this.mirrorInfluence(influences);
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int getCost() {
    return this.cost;
  }

  @Override
  public int getValue() {
    return this.value;
  }

  @Override
  public List<Coordinate> getInfluence() {
    return this.influences;
  }

  private List<Coordinate> mirrorInfluence(List<Coordinate> influences) {
    List<Coordinate> mirrorInfluences = new ArrayList<>();
    for (Coordinate c : influences) {
      //only flips the influence along the y-axis for blue cards
      mirrorInfluences.add(new Coordinate(c.getX() * -1, c.getY()));
    }
    return mirrorInfluences;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    InfluenceCard that = (InfluenceCard) obj;
    return cost == that.cost
        && value == that.value
        && player == that.player
        && name.equals(that.name)
        && influences.equals(that.influences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player, name, cost, value, influences);
  }
}
