package sanguine.controller;

import java.util.List;
import sanguine.model.Card;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguineModel;
import sanguine.view.FeatureListener;

/**
 * Machine player that computes and publishes actions using a strategy.
 */
public class MachinePlayer implements PlayerActions {
  private final Player player;
  private final Strategy strategy;
  private FeatureListener listener;

  /**
   * Creates a machine player for the given enum and strategy.
   *
   * @param player the player identity
   * @param strategy strategy used to generate moves
   */
  public MachinePlayer(Player player, Strategy strategy) {
    if (player == null || strategy == null) {
      throw new IllegalArgumentException("Player and strategy cannot be null");
    }
    this.player = player;
    this.strategy = strategy;
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
    if (listener == null || model == null || model.gameOver()) {
      return;
    }
    if (model.getCurrentPlayer() != this.player) {
      return;
    }
    List<Move> moves = strategy.generateMoves(model);
    if (moves == null || moves.isEmpty()) {
      listener.passTurn(player);
      return;
    }
    for (Move move : moves) {
      if (move == null) {
        continue;
      }
      if (!this.isValidMove(model, move)) {
        continue;
      }
      this.publishMove(model, move);
      return;
    }
    listener.passTurn(player);
  }

  private boolean isValidMove(SanguineModel model, Move move) {
    try {
      Cell targetCell = model.getCell(move.getRow(), move.getCol());
      model.checkValidMove(targetCell, move.getCard());
      return true;
    } catch (IllegalArgumentException | IllegalStateException e) {
      return false;
    }
  }

  private void publishMove(SanguineModel model, Move move) {
    List<Card> hand = model.getPlayerHand(player);
    int cardIndex = hand.indexOf(move.getCard());
    if (cardIndex < 0) {
      listener.passTurn(player);
      return;
    }
    listener.selectCard(cardIndex, player);
    listener.selectBoardCell(move.getRow(), move.getCol(), player);
    listener.confirmMove(player);
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }

  @Override
  public boolean isAutomated() {
    return true;
  }
}

