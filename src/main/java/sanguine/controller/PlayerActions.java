package sanguine.controller;

import sanguine.model.Player;
import sanguine.model.SanguineModel;
import sanguine.view.FeatureListener;

/**
 * Publishes player actions so controllers can observe human or machine players.
 */
public interface PlayerActions {

  /**
   * Registers a listener that consumes player actions.
   *
   * @param listener a controller listener
   */
  void setListener(FeatureListener listener);

  /**
   * Called when it is this player's turn so it can publish actions.
   *
   * @param model the game model
   */
  void takeTurn(SanguineModel model);

  /**
   * Returns the enum identity for this player.
   *
   * @return the player enum
   */
  Player getPlayer();

  /**
   * True if this player is automated.
   *
   * @return if the player is automated
   */
  boolean isAutomated();
}

