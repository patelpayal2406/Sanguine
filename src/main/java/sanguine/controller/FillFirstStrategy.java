package sanguine.controller;

import java.util.ArrayList;
import java.util.List;
import sanguine.model.Board;
import sanguine.model.Card;
import sanguine.model.Cell;
import sanguine.model.PawnCell;
import sanguine.model.Player;
import sanguine.model.SanguineModel;

/**
 * A strategy that picks the first valid move it finds.
 * It iterates through cards and board positions in order.
 */
public class FillFirstStrategy implements Strategy {

  /**
   * Returns all valid moves in order (first valid move first).
   *
   * @param model the model to observe for making moves
   * @return List of all valid moves
   */
  @Override
  public List<Move> generateMoves(SanguineModel model) {
    Player currentPlayer = model.getCurrentPlayer();
    List<Card> hand = model.getPlayerHand(currentPlayer);
    Board board = model.getBoard();
    List<List<Cell>> cells = board.getCells();
    
    List<Move> moves = new ArrayList<>();

    // Choose first card
    for (Card card : hand) {
      // Find spots
      for (int row = 0; row < cells.size(); row++) {
        for (int col = 0; col < cells.get(row).size(); col++) {
          Cell cell = board.getCell(row, col);
          if ((cell instanceof PawnCell) 
              && cell.getPlayer() == currentPlayer
              && card.getCost() <= cell.getValue()) {
            moves.add(new Move(card, row, col));
          }
        }
      }
    }

    return moves;
  }
}
