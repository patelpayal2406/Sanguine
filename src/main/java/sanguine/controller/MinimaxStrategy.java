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
 * A minimax strategy that chooses moves to minimize the opponent's best response.
 * This strategy assumes the opponent will use a given strategy to choose their moves,
 * and picks the move that leaves the opponent with the weakest options.
 */
public class MinimaxStrategy implements Strategy {
  private final Strategy opponentStrategy;

  /**
   * Creates a minimax strategy that assumes the opponent uses the given strategy.
   *
   * @param opponentStrategy the strategy to assume the opponent will use
   */
  public MinimaxStrategy(Strategy opponentStrategy) {
    this.opponentStrategy = opponentStrategy;
  }

  @Override
  public List<Move> generateMoves(SanguineModel model) {
    Player currentPlayer = model.getCurrentPlayer();
    List<Card> hand = model.getPlayerHand(currentPlayer);
    Board board = model.getBoard();
    List<List<Cell>> cells = board.getCells();

    Move bestMove = null;
    int minOpponentValue = Integer.MAX_VALUE;

    // Try all possible moves
    for (Card card : hand) {
      for (int row = 0; row < cells.size(); row++) {
        for (int col = 0; col < cells.get(row).size(); col++) {
          Cell cell = board.getCell(row, col);
          // Check if move is valid
          if ((cell instanceof PawnCell) 
              && cell.getPlayer() == currentPlayer
              && card.getCost() <= cell.getValue()) {
            
            // Simulate move and evaluate opponent's best response
            int opponentValue = evaluateOpponentResponse(model, card, row, col);
            
            // Minimize the opponent's gain
            if (opponentValue < minOpponentValue) {
              minOpponentValue = opponentValue;
              bestMove = new Move(card, row, col);
            }
          }
        }
      }
    }

    // Return best defensive move
    if (bestMove != null) {
      return List.of(bestMove);
    }
    return new ArrayList<>();
  }

  /**
   * Evaluates how good the opponent's response would be after we make a move.
   * Returns a score representing the quality of the opponent's best move.
   */
  private int evaluateOpponentResponse(SanguineModel model, Card card, int row, int col) {
    List<Move> opponentMoves = opponentStrategy.generateMoves(model);
    
    if (opponentMoves.isEmpty()) {
      return 0;
    }

    Player opponent = (model.getCurrentPlayer() == Player.RED) ? Player.BLUE : Player.RED;
    int maxOpponentScore = 0;

    for (Move opponentMove : opponentMoves) {
      int score = evaluateMoveValue(model, opponentMove, opponent);
      maxOpponentScore = Math.max(maxOpponentScore, score);
    }

    return maxOpponentScore;
  }

  /**
   * Evaluates the value of a move for a player 
   * based on the number of rows the player could win.
   */
  private int evaluateMoveValue(SanguineModel model, Move move, Player player) {
    Player opponent = (player == Player.RED) ? Player.BLUE : Player.RED;
    int rowsWon = 0;
    
    List<List<Cell>> cells = model.getBoard().getCells();
    for (int row = 0; row < cells.size(); row++) {
      int playerScore = model.getBoard().getRowScore(row, player);
      int opponentScore = model.getBoard().getRowScore(row, opponent);
      
      if (row == move.getRow()) {
        Cell cell = model.getBoard().getCell(row, move.getCol());
        if (cell instanceof PawnCell && cell.getPlayer() == player) {
          playerScore += cell.getValue();
        }
      }
      
      if (playerScore > opponentScore) {
        rowsWon++;
      }
    }
    
    return rowsWon;
  }
}
