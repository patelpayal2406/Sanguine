package sanguine.model;

import java.util.List;

/**
 * Represents the model interface that only contains observer methods.
 */
public interface ReadonlySanguineModel {
  /**
   * Game is over when players pass one after the other.
   *
   * @return true if the game is over
   */
  boolean gameOver();

  /**
   * Returns the player with the highest score. Returns null if the game is tied.
   *
   * @return the player that won the game, or null for a tie
   */
  Player getWinner();

  /**
   * Returns the current player of the game.
   *
   * @return the current player
   */
  Player getCurrentPlayer();

  /**
   * Gets the board interface to access board state.
   *
   * @return the board
   */
  Board getBoard();

  /**
   * Gets the deck of InfluenceCards of the given player.
   *
   * @param player enum player that is either red or blue
   * @return the deck of the player
   */
  List<Card> getDeck(Player player);

  /**
   * Gets the player's hand of cards which they are able to see during the game.
   *
   * @param player enum player that is either red or blue
   * @return the player's card hand
   */
  List<Card> getPlayerHand(Player player);
}
