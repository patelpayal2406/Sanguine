package sanguine.view;

import java.awt.Graphics;
import javax.swing.JPanel;
import sanguine.model.Player;
import sanguine.model.ReadonlySanguineModel;

/**
 * Abstracted class for the panels of the view frames.
 */
public abstract class AbstractPanel extends JPanel implements PublisherPanel {
  protected ReadonlySanguineModel model;
  protected Player player;
  protected int logicalWidth;
  protected int logicalHeight;

  /**
   * Sets up the model and player for each panel on a frame.
   *
   * @param model viewable only model of Sanguine
   * @param player enum player either red or blue
   */
  public AbstractPanel(ReadonlySanguineModel model, Player player) {
    if (model == null || player == null) {
      throw new IllegalArgumentException("Model and player cannot be null");
    }
    this.model = model;
    this.player = player;
    this.logicalWidth = 0;
    this.logicalHeight = 0;
  }

  @Override
  public void setListener(FeatureListener listener, SanguineView view) {
    this.addMouseListener(new ClickListener(listener, this));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  }

  @Override
  public abstract void updateDimensions(int rows, int cols, int handSize);

  @Override
  public int getLogicalWidth() {
    return this.logicalWidth;
  }

  @Override
  public int getLogicalHeight() {
    return this.logicalHeight;
  }

  @Override
  public JPanel getJpanel() {
    return this;
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }

  @Override
  public void highlightCell(int row, int col, Player player, boolean selected) {}

  @Override
  public void highlightCard(int cardIndex, Player player, boolean selected) {}
}
