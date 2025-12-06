package sanguine.view;

import sanguine.model.Player;

/**
 * Represents a type of listener that publishes model-status notifications to the controller.
 */
public interface ModelListener {
  /**
   * The current player's controller notifies the view when the player turn has changed.
   *
   * @param player the current player
   */
  void turnChanged(Player player);

  /**
   * If the game is over per the model, the controller notifies
   * the view for sending game over notifications.
   *
   * @param winner the player that won or null if no one won
   */
  void gameOver(Player winner);

  /**
   * Notifies the view about an error that has occurred so players are aware of them.
   *
   * @param reason the particular error message
   */
  void errorOccurrence(String reason);

  /**
   * The current player's controller notifies the view when the player has passed their turn.
   *
   * @param player the player that passed their turn
   */
  void turnPassed(Player player);
}


