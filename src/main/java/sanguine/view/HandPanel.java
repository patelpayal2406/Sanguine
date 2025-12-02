package sanguine.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import sanguine.model.Card;
import sanguine.model.Coordinate;
import sanguine.model.Player;
import sanguine.model.ReadonlySanguineModel;

/**
 * Represents the JPanel that shows the player's card hand.
 */
public class HandPanel extends AbstractPanel {
  private int selectedCardIndex;
  private boolean cardIsSelected;

  /**
   * Initializes model, player, and unselected cells for panel.
   *
   * @param model viewable only model
   * @param player either red or blue
   */
  public HandPanel(ReadonlySanguineModel model, Player player) {
    super(model, player);
    this.selectedCardIndex = -1;
    this.cardIsSelected = false;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    if (logicalWidth == 0 || logicalHeight == 0) {
      return;
    }
    int cardWidth = this.getWidth() / this.logicalWidth;
    int cardHeight = cardWidth * 2;

    List<Card> hand = model.getPlayerHand(player);
    for (int cardIndex = 0; cardIndex < hand.size(); cardIndex++) {
      int x = cardIndex * cardWidth;
      int y = 0;
      //draws each card of the hand next to each other on the panel
      this.drawCard(g2d, hand.get(cardIndex), cardWidth, cardHeight, x, y, cardIndex);
    }
    g2d.dispose();
  }

  private void drawCard(Graphics2D g2d, Card card,
                        int cardWidth, int cardHeight, int x, int y, int cardIndex) {
    //if the card is selected, it is highlighted
    if (cardIsSelected && selectedCardIndex == cardIndex) {
      g2d.setColor(Color.CYAN);
    } else {
      Color player = card.getPlayer() == Player.RED
              ? new Color(235, 50, 50) : new Color(50, 95, 230);
      g2d.setColor(player);
    }
    //draws the name, cost, value, and board of influences for a card
    g2d.fillRect(x, y, cardWidth, cardHeight);
    g2d.setColor(Color.BLACK);
    g2d.drawRect(x, y, cardWidth, cardHeight);
    g2d.drawString(card.getName(), x + 20, y + 20);
    g2d.drawString("Cost: " + card.getCost(), x + 20, y + 35);
    g2d.drawString("Value: " + card.getValue(), x + 20, y + 50);
    this.drawInfluenceGrid(g2d, card, x);
  }

  private void drawInfluenceGrid(Graphics2D g2d, Card card, int cardX) {
    List<Coordinate> influences = card.getInfluence();
    //draws a mini board to show how the board would be influenced by this card
    int cellWidth = 20;
    int cellHeight = 25;
    int x = 0;
    int y = 0;
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        x = cardX + col * cellWidth + 15;
        y = row * cellHeight + 60;
        if (row == 2 && col == 2) {
          g2d.setColor(Color.ORANGE);
          g2d.fillRect(x, y, cellWidth, cellHeight);
        } else {
          g2d.setColor(Color.GRAY);
          g2d.fillRect(x, y, cellWidth, cellHeight);
        }
      }
    }
    this.drawInfluences(g2d, influences, cellWidth, cellHeight, cardX);
  }

  private void drawInfluences(Graphics2D g2d, List<Coordinate> influences,
                              int cellWidth, int cellHeight, int cardX) {
    int x = 0;
    int y = 0;
    //gets the coordinates of the card's influences to appropriately position them on the mini
    //influence grid
    for (Coordinate influence : influences) {
      for (int row = 0; row < 5; row++) {
        for (int col = 0; col < 5; col++) {
          x = cardX + col * cellWidth + 15;
          y = row * cellHeight + 60;
          if (new Coordinate(col - 2, row - 2).equals(influence)) {
            g2d.setColor(Color.CYAN);
            g2d.fillRect(x, y, cellWidth, cellHeight);
          }
          g2d.setColor(Color.BLACK);
          g2d.drawLine(x, y, x + cellWidth, y);
          g2d.drawLine(x, y, x, y + cellHeight);
        }
      }
    }
    //draws grid lines to separate the cells
    g2d.drawLine(cardX + 15, cellHeight * 5 + 60,
            cardX + 15 + cellWidth * 5, cellHeight * 5 + 60);
    g2d.drawLine(cardX + 15 + cellWidth * 5, 60,
            cardX + 15 + cellWidth * 5, cellHeight * 5 + 60);
  }

  @Override
  public void updateDimensions(int rows, int cols, int handSize) {
    //each card's height is twice the size of the width
    //the panel width is based on the number of cards in the hand
    this.logicalWidth = handSize;
    this.logicalHeight = 2;
    Dimension dimension = new Dimension(this.logicalWidth * 100, this.logicalHeight * 100);
    this.setPreferredSize(dimension);
    this.revalidate();
  }

  @Override
  public void highlightCard(int cardIndex, Player player, boolean selected) {
    if (player != this.player) {
      return;
    }
    //marks down the position of the card if it is selected
    if (selected) {
      this.selectedCardIndex = cardIndex;
      this.cardIsSelected = true;
    } else {
      //resets fields when card is deselected
      this.cardIsSelected = false;
      this.selectedCardIndex = -1;
    }
    this.repaint();
  }
}
