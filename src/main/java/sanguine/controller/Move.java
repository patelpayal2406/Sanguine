package sanguine.controller;

import sanguine.model.Card;

/**
 * Represents a move in the game, consisting of a card to play and the target coordinates.
 */
public class Move {
  private final Card card;
  private final int row;
  private final int col;

  /**
   * Creates a new move with the given card and coordinates.
   *
   * @param card the card to play
   * @param row the row index to place the card
   * @param col the column index to place the card
   */
  public Move(Card card, int row, int col) {
    this.card = card;
    this.row = row;
    this.col = col;
  }

  /**
   * Returns the card for this move.
   *
   * @return the card to play
   */
  public Card getCard() {
    return card;
  }

  /**
   * Returns the row index for this move.
   *
   * @return the row index
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns the column index for this move.
   *
   * @return the column index
   */
  public int getCol() {
    return col;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Move move = (Move) obj;
    return row == move.row && col == move.col && card.equals(move.card);
  }

  @Override
  public int hashCode() {
    return 31 * (31 * card.hashCode() + row) + col;
  }
}
