package sanguine.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sanguine.model.SanguineModel;

/**
 * A strategy that combines multiple strategies to find the best move.
 * It applies strategies in sequence, filtering the list of possible moves.
 */
public class CompositeStrategy implements Strategy {
  private final List<Strategy> strategies;

  /**
   * Constructs the composite strategy by adding combined strategies as a list.
   *
   * @param strategies possible player strategies
   */
  public CompositeStrategy(Strategy... strategies) {
    this.strategies = Arrays.asList(strategies);
  }

  @Override
  public List<Move> generateMoves(SanguineModel model) {
    List<Move> candidates = null;

    // Chain strategies
    for (Strategy strategy : strategies) {
      List<Move> moves = strategy.generateMoves(model);
      
      if (candidates == null) {
        // First strategy sets the candidates
        candidates = moves;
      } else {
        // Subsequent strategies filter existing candidates
        candidates = filterMoves(candidates, moves);
      }

      if (candidates.isEmpty()) {
        return new ArrayList<>();
      }

      // Return if narrowed down to one move
      if (candidates.size() == 1) {
        return candidates;
      }
    }

    // Return first candidate if multiple remain
    if (candidates != null && !candidates.isEmpty()) {
      return List.of(candidates.get(0));
    }
    return new ArrayList<>();
  }

  // Filters the moves to only include the allowed moves
  private List<Move> filterMoves(List<Move> candidates, List<Move> allowed) {
    List<Move> filtered = new ArrayList<>();
    for (Move candidate : candidates) {
      for (Move allowedMove : allowed) {
        if (movesEqual(candidate, allowedMove)) {
          filtered.add(candidate);
          break;
        }
      }
    }
    return filtered;
  }

  private boolean movesEqual(Move m1, Move m2) {
    return m1.equals(m2);
  }
}
