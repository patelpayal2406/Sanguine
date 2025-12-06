package sanguine.view;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import sanguine.model.Player;
import sanguine.model.ReadonlySanguineModel;

/**
 * The window of the game and can be either from the red or blue player's perspective.
 */
public class SanguineFrame extends JFrame implements SanguineView {
  private final BoardPanel boardPanel;
  private final HandPanel handPanel;
  private final ReadonlySanguineModel model;
  private final Player player;

  /**
   * Initializes the frame to hold different panels for each player.
   *
   * @param model viewable only Sanguine
   * @param player the player of the frame
   */
  public SanguineFrame(ReadonlySanguineModel model, Player player) {
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.model = model;
    this.player = player;
    //initializes the two panels for each frame
    this.boardPanel = new BoardPanel(this.model, this.player);
    this.handPanel = new HandPanel(this.model, this.player);
    this.setLayout(new BorderLayout());
    //panels are placed on top of each other
    this.add(boardPanel, BorderLayout.NORTH);
    this.add(handPanel, BorderLayout.SOUTH);
    this.pack();
  }

  @Override
  public void refresh() {
    this.repaint();
    String status;
    if (this.player != model.getCurrentPlayer()) {
      status = "Waiting";
    } else {
      status = "Playing";
    }
    this.setTitle(player.toString() + " - " + status);
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void setListener(FeatureListener listener) {
    //adds click listeners to each panel
    this.boardPanel.setListener(listener, this);
    this.handPanel.setListener(listener, this);
    //creates key listeners to connect the controller to the view
    //'c' is for confirming the placement of a selected card on the selected cell
    //'p' is for passing a turn
    KeyListener keyListener = new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'c') {
          listener.confirmMove(player);
        }
        if (e.getKeyChar() == 'p') {
          listener.passTurn(player);
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {

      }

      @Override
      public void keyReleased(KeyEvent e) {

      }
    };
    this.addKeyListener(keyListener);
  }

  @Override
  public void refreshSize(int rows, int cols, int handSize) {
    this.boardPanel.updateDimensions(rows, cols, handSize);
    this.handPanel.updateDimensions(rows, cols, handSize);
    this.boardPanel.revalidate();
    this.handPanel.revalidate();
    this.pack();
    this.refresh();
  }

  @Override
  public PublisherPanel getBoardPanel() {
    return this.boardPanel;
  }

  @Override
  public PublisherPanel getHandPanel() {
    return this.handPanel;
  }

  @Override
  public void endGame(Player winner) {
    //stops the players from continuing to interact with the view
    this.boardPanel.setEnabled(false);
    this.handPanel.setEnabled(false);
    for (KeyListener kl : this.getKeyListeners()) {
      this.removeKeyListener(kl);
    }
    //calls the method to send a message about the winner
    this.showWinner(winner);
  }

  @Override
  public void showWinner(Player winner) {
    String winnerMessage = (winner == null)
            ? "Game over! It's a tie." : "Game over! " + winner + " wins!";
    JOptionPane.showMessageDialog(this, winnerMessage);
  }

  @Override
  public void showTurn(Player player) {
    JOptionPane.showMessageDialog(this, this.model.getCurrentPlayer().toString() + "'s turn");
  }

  @Override
  public void showError(String reason) {
    JOptionPane.showMessageDialog(this, reason);
  }

  @Override
  public void showPass(Player player) {
    JOptionPane.showMessageDialog(this, player.toString() + " passed");
  }
}
