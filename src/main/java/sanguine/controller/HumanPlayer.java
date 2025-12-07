package sanguine.controller;

import sanguine.model.Player;
import sanguine.model.SanguineModel;
import sanguine.view.FeatureListener;

/**
 * Human player that relies on the view to publish actions.
 */
public class HumanPlayer implements PlayerActions {
  private final Player player;
  private FeatureListener listener;

  /**
   * Creates a human player for the given enum.
   *
   * @param player the player identity
   */
  public HumanPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    this.player = player;
  }

  @Override
  public void setListener(FeatureListener listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null");
    }
    this.listener = listener;
  }

  @Override
  public void takeTurn(SanguineModel model) {
    // Human actions come from the view
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }

  @Override
  public boolean isAutomated() {
    return false;
  }
}

