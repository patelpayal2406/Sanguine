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
 * A strategy that maximizes the row score for the current player.
 */
public class MaximizeRowScoreStrategy implements Strategy {

  /**
   * Generates a list of moves that maximize the row score for the current player.
   *
   * @param model the model to observe for making moves
   * @return List of moves that maximize the row score for the current player
   */
  @Override
  public List<Move> generateMoves(SanguineModel model) {
    Player currentPlayer = model.getCurrentPlayer();
    Player opponent = (currentPlayer == Player.RED) ? Player.BLUE : Player.RED;
    List<Card> hand = model.getPlayerHand(currentPlayer);
    Board board = model.getBoard();
    List<List<Cell>> cells = board.getCells();
    
    List<Move> bestMoves = new ArrayList<>();

    // Visit rows top-down
    for (int row = 0; row < cells.size(); row++) {
      int currentScore = board.getRowScore(row, currentPlayer);
      int opponentScore = board.getRowScore(row, opponent);
      
      // Only consider rows where we are behind or tied
      if (currentScore <= opponentScore) {
        // Try each card
        for (Card card : hand) {
          // Try each column
          for (int col = 0; col < cells.get(row).size(); col++) {
            Cell cell = board.getCell(row, col);
            // Check if move is valid
            if ((cell instanceof PawnCell) 
                && cell.getPlayer() == currentPlayer
                && card.getCost() <= cell.getValue()) {
              
              int potentialScore = currentScore + card.getValue();
              if (potentialScore > opponentScore) {
                bestMoves.add(new Move(card, row, col));
              }
            }
          }
        }
      }
    }

    return bestMoves;
  }
}
