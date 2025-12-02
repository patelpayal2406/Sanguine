package sanguine.controller;

import java.util.ArrayList;
import java.util.List;
import sanguine.model.Board;
import sanguine.model.Card;
import sanguine.model.Cell;
import sanguine.model.InfluenceCard;
import sanguine.model.PawnCell;
import sanguine.model.Player;
import sanguine.model.SanguineModel;

/**
 * A strategy that controls the board for the current player.
 */
public class ControlTheBoardStrategy implements Strategy {

  /**
   * Generates a list of moves that control the board for the current player.
   *
   * @param model the model to observe for making moves
   * @return List of moves that control the board for the current player
   */
  @Override
  public List<Move> generateMoves(SanguineModel model) {
    Player currentPlayer = model.getCurrentPlayer();
    List<Card> hand = model.getPlayerHand(currentPlayer);
    Board board = model.getBoard();
    List<List<Cell>> cells = board.getCells();

    List<Move> bestMoves = new ArrayList<>();
    int maxOwnership = -1;

    // Iterate over all possible moves
    for (Card card : hand) {
      for (int row = 0; row < cells.size(); row++) {
        for (int col = 0; col < cells.get(row).size(); col++) {
          Cell cell = board.getCell(row, col);
          // Check validity
          if ((cell instanceof PawnCell) 
              && cell.getPlayer() == currentPlayer
              && card.getCost() <= cell.getValue()) {
            
            // Calculate potential ownership
            int ownership = calculateOwnershipAfterMove(model, currentPlayer, row, col, card);
            
            // Keep track of best moves
            if (ownership > maxOwnership) {
              maxOwnership = ownership;
              bestMoves.clear();
              bestMoves.add(new Move(card, row, col));
            } else if (ownership == maxOwnership) {
              bestMoves.add(new Move(card, row, col));
            }
          }
        }
      }
    }

    return bestMoves;
  }

  private int calculateOwnershipAfterMove(SanguineModel model, Player player, int row, int col,
      Card card) {
    int count = 0;
    List<List<Cell>> cells = model.getBoard().getCells();

    for (int r = 0; r < cells.size(); r++) {
      for (int c = 0; c < cells.get(r).size(); c++) {
        Cell cell = model.getBoard().getCell(r, c);
        if (cell instanceof PawnCell) {
          if (r == row && c == col) {
            count++;
          } else if (cell.getPlayer() == player) {
            count++;
          }
        }
      }
    }

    return count;
  }
}
