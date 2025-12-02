package sanguine.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import sanguine.model.Player;

/**
 * Listener for mouse clicked events to know what was selected on the board.
 */
public class ClickListener extends MouseAdapter {
  private final FeatureListener listener;
  private final PublisherPanel panel;

  /**
   * Initializes the specific listener for the specific panel that was clicked on.
   *
   * @param listener listener for mouse events
   * @param panel either a BoardPanel or HandPanel on a frame
   */
  public ClickListener(FeatureListener listener, PublisherPanel panel) {
    if (listener == null) {
      throw new IllegalArgumentException("Arguments cannot be null");
    }
    this.listener = listener;
    this.panel = panel;
  }

  @Override
  public void mouseClicked(MouseEvent evt) {
    //delegates to the appropriate method based on with panel was clicked on
    if (this.panel instanceof BoardPanel) {
      this.handleBoardClick(evt, this.panel.getPlayer());
    } else {
      this.handleHandClick(evt, this.panel.getPlayer());
    }
  }

  private void handleBoardClick(MouseEvent evt, Player player) {
    //scales the mouse coordinates with the logical coordinates to get the appropriate cell index
    JPanel jpanel = this.panel.getJpanel();
    int logicalHeight = this.panel.getLogicalHeight();
    int logicalWidth = this.panel.getLogicalWidth();
    int cellSize = Math.min(jpanel.getWidth() / logicalWidth, jpanel.getHeight() / logicalHeight);
    int xcoordOffset = (jpanel.getWidth() - cellSize * logicalWidth) / 2;
    int ycoordOffset = (jpanel.getHeight() - cellSize * logicalHeight) / 2;
    int mouseX = evt.getX();
    int mouseY = evt.getY();
    int panelCol = (mouseX - xcoordOffset) / cellSize;
    int panelRow = (mouseY - ycoordOffset) / cellSize;

    if (panelCol > 0 && panelCol < logicalWidth - 1
            && panelRow >= 0 && panelRow < logicalHeight) {
      int boardCol = panelCol - 1;
      int boardRow = panelRow;
      //notifies the BoardPanel about which cell was selected
      this.listener.selectBoardCell(boardRow, boardCol, player);
    }
  }

  private void handleHandClick(MouseEvent evt, Player player) {
    //scales the mouse coordinates with the logical coordinates to get the appropriate card index
    JPanel jpanel = this.panel.getJpanel();
    int cardWidth = jpanel.getWidth() / this.panel.getLogicalWidth();
    int cardHeight = cardWidth * 2;
    int index = evt.getX() / cardWidth;

    if (evt.getY() >= 0 && evt.getY() <= cardHeight
            && index >= 0 && index < this.panel.getLogicalWidth()) {
      //notifies the HandPanel about which card was selected
      this.listener.selectCard(index, player);
    }
  }
}