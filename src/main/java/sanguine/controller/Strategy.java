package sanguine.controller;

import java.util.List;
import sanguine.model.SanguineModel;

/**
 * Represents the interface for all possible strategies players can consider when playing Sanguine.
 */
public interface Strategy {

  /**
   * General method for creating moves. Does not mutate the model.
   *
   * @param model the model to observe for making moves
   * @return List of possible moves
   */
  List<Move> generateMoves(SanguineModel model);
}
