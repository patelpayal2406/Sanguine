package sanguine.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the model of the game where a deck of cards
 * is initialized and game states are handled.
 */
public class SanguineGame implements SanguineModel {
  private Board board;
  private Player currentPlayer;

  private final List<Card> redDeck;
  private final List<Card> blueDeck;
  private final List<Card> redHand;
  private final List<Card> blueHand;
  // INVARIANT: each player's deck and hand must contain cards that only belong to them

  private int handSize;
  private Player lastPlayerWhoPassed;
  private int consecutivePasses;

  /**
   * Creates the deck of influence cards for each player to use.
   *
   * @param redDeck  a list of influence cards for the red player
   * @param blueDeck a list of influence cards for the blue player
   */
  public SanguineGame(List<Card> redDeck, List<Card> blueDeck) {
    //ensures a deck only contains all cards for one player
    this.checkCorrectPlayers(redDeck, Player.RED);
    this.checkCorrectPlayers(blueDeck, Player.BLUE);
    this.redDeck = redDeck;
    this.blueDeck = blueDeck;
    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    this.handSize = 0;
    this.consecutivePasses = 0;
    this.lastPlayerWhoPassed = null;
  }

  private void checkCorrectPlayers(List<Card> deck, Player player) {
    for (Card card : deck) {
      if (card.getPlayer() != player) {
        throw new IllegalArgumentException("Card doesn't belong to this player");
      }
    }
  }

  @Override
  public void startGame(int rows, int cols, int handSize, boolean shuffled) {
    if (board != null) {
      throw new IllegalStateException("Game has already been started");
    }
    //throws an error if there are not enough cards in a deck to possible play on every cells
    //of the customized board
    if (redDeck.size() < rows * cols) {
      throw new IllegalArgumentException("Not enough cards to play on board size");
    }

    //shuffles the deck if parameter is true
    if (shuffled) {
      Collections.shuffle(this.redDeck);
      Collections.shuffle(this.blueDeck);
    }
    this.handSize = handSize;

    // Deal decks to hands
    this.dealDeck(Player.RED);
    this.dealDeck(Player.BLUE);

    board = new InfluenceBoard(rows, cols);
    currentPlayer = Player.RED;
    consecutivePasses = 0;
  }

  private void dealDeck(Player player) {
    if (this.handSize > (this.getDeck(player).size() / 3.0)) {
      throw new IllegalArgumentException("Hand size cannot be greater than "
              + "1/3 the deck size for " + player.toString());
    }
    //initially grabs the first 5 cards of the deck
    for (int i = 0; i < handSize; i++) {
      this.drawCardToHand(player);
    }
  }

  @Override
  public Player getCurrentPlayer() {
    this.checkGameStarted();
    return currentPlayer;
  }

  @Override
  public void playCard(Card card, int row, int col) {
    this.checkGameStarted();
    //ensures the card is owned by the current player
    if (card.getPlayer() != this.currentPlayer) {
      throw new IllegalArgumentException("Cannot play opponent's card");
    }
    //tries to place a card
    board.playCard(card, row, col);
    consecutivePasses = 0;
    //removes the card that was placed from the current player's hand
    this.removeCardFromHand(this.currentPlayer, card);
    //switches whose turn it is
    this.switchPlayer();
    //draws a card at the start of the next player's turn
    if (!this.getDeck(this.currentPlayer).isEmpty()) {
      this.drawCardToHand(this.currentPlayer);
    }
  }

  private void checkGameStarted() throws IllegalStateException {
    if (board == null) {
      throw new IllegalStateException("Game has not started");
    }
  }

  @Override
  public void switchPlayer() {
    this.checkGameStarted();
    this.currentPlayer = this.currentPlayer == Player.RED ? Player.BLUE : Player.RED;
  }

  private void drawCardToHand(Player player) {
    this.getPlayerHand(player).add((InfluenceCard) this.removeCardFromDeck(player));
  }

  private Card removeCardFromDeck(Player player) {
    return this.getDeck(player).removeFirst();
  }

  private void removeCardFromHand(Player player, Card card) {
    this.getPlayerHand(player).remove(card);
  }

  @Override
  public boolean gameOver() {
    this.checkGameStarted();
    //if the board contains any cell other than value cells, then the board is not full
    boolean boardFull = true;
    for (List<Cell> row : this.getBoard().getCells()) {
      for (Cell cell : row) {
        if (!(cell instanceof ValueCell)) {
          boardFull = false;
          break;
        }
      }
    }
    //game is over if consecutive passes from both players were made or board is full
    return consecutivePasses >= 2 || boardFull;
  }

  @Override
  public Player getWinner() {
    this.checkGameStarted();
    if (!gameOver()) {
      return null;
    }
    int redTotal = board.getTotalScore(Player.RED);
    int blueTotal = board.getTotalScore(Player.BLUE);
    //compares the total row scores and returns the player with the highest number
    if (redTotal > blueTotal) {
      return Player.RED;
    } else if (blueTotal > redTotal) {
      return Player.BLUE;
    } else {
      return null;
    }
  }

  @Override
  public void pass() {
    this.checkGameStarted();
    //consecutive passes only increases if the pass was made by two different people
    if (lastPlayerWhoPassed == this.currentPlayer) {
      consecutivePasses = 1;
    } else {
      consecutivePasses++;
    }
    //marks down who made the pass
    this.lastPlayerWhoPassed = this.currentPlayer;
    this.switchPlayer();
    //since the turn is being switched, a card is drawn for the next player
    if (!this.getDeck(this.currentPlayer).isEmpty()) {
      this.drawCardToHand(this.currentPlayer);
    }
  }

  @Override
  public Board getBoard() {
    this.checkGameStarted();
    return board;
  }

  @Override
  public List<Card> getDeck(Player player) {
    return player == Player.RED ? this.redDeck : this.blueDeck;
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    return player == Player.RED ? this.redHand : this.blueHand;
  }
}
