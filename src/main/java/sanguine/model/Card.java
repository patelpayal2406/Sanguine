package sanguine.model;

import java.util.List;

/**
 * Represents a card with a name, cost, value, and influence list. The cost is the number of pawns
 * needs to place a card on a cell. The value is added to the row score when the card is placed. The
 * list of coordinates demonstrates the relative position of influences from where the card was
 * placed on the board.
 */
public interface Card {
  /**
   * Gets which player the card is for.
   *
   * @return the player of the card
   */
  Player getPlayer();

  /**
   * Gets the name of the influence card.
   *
   * @return the type of influence card
   */
  String getName();

  /**
   * Gets the number of pawns required to play the card.
   *
   * @return the cost of the card
   */
  int getCost();

  /**
   * Gets the total value of the card that will be displayed on the board.
   *
   * @return the value of the card
   */
  int getValue();

  /**
   * Gets the list of coordinates representing which cells the card influences.
   *
   * @return the coordinates for spreading influence
   */
  List<Coordinate> getInfluence();
}
