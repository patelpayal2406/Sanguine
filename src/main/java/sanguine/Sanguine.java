package sanguine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import sanguine.controller.SanguineController;
import sanguine.controller.SanguineGuiController;
import sanguine.model.Card;
import sanguine.model.Coordinate;
import sanguine.model.InfluenceCard;
import sanguine.model.Player;
import sanguine.model.SanguineGame;
import sanguine.model.SanguineModel;
import sanguine.view.SanguineFrame;
import sanguine.view.SanguineView;

/**
 * Represents the Sanguine game that reads a configuration file to make the deck of influence cards.
 */
public final class Sanguine {
  /**
   * The main method that reads a config file for the possible deck of influence cards.
   *
   * @param args an array of arguments from the command line
   */
  public static void main(String[] args) {
    //ensures that there exists an argument to take in
    if (args.length < 5) {
      throw new IllegalArgumentException("Please specify a file name");
    }
    int rows = Integer.parseInt(args[1]);
    int cols = Integer.parseInt(args[2]);
    int handSize = Integer.parseInt(args[3]);
    boolean shuffle = Boolean.parseBoolean(args[4]);
    try {
      //reads the configuration file twice to give the model a deck for each player
      SanguineModel model = new SanguineGame(loadConfig(args[0], Player.RED),
              loadConfig(args[0], Player.BLUE));
      //creates windows for each player to interact with
      SanguineView redView = new SanguineFrame(model, Player.RED);
      SanguineView blueView = new SanguineFrame(model, Player.BLUE);
      //sets up a shared controller for the players
      SanguineController redController = new SanguineGuiController(redView, model);
      SanguineController blueController = new SanguineGuiController(blueView, model);
      //uses the user inputs to start the game
      redController.playGame(rows, cols, handSize, shuffle);
      blueController.playGame(rows, cols, handSize, shuffle);
      redView.makeVisible();
      blueView.makeVisible();
    }  catch (FileNotFoundException e) {
      System.out.println("Configuration file not found");
    } catch (NumberFormatException e) {
      System.out.println("Please input valid row, column, and card hand size numbers");
    }
  }

  private static List<Card> loadConfig(String config, Player player)
          throws IllegalArgumentException, FileNotFoundException {
    List<Card> cards = new ArrayList<>();
    String path = "docs" + File.separator + config;
    //ensures that the path of the file exists
    File configFile = new File(path);
    if (!configFile.exists()) {
      throw new FileNotFoundException("Config file does not exist");
    }
    Scanner scanner = new Scanner(configFile);
    while (scanner.hasNextLine()) {
      String[] cardInfo = scanner.nextLine().split(" ");
      //ensures there is a name, cost, and value to an influence card
      if (cardInfo.length != 3) {
        throw new IllegalArgumentException("Invalid config file format");
      }
      String name = cardInfo[0];
      int cost = Integer.parseInt(cardInfo[1]);
      int value = Integer.parseInt(cardInfo[2]);
      List<Coordinate> influence = new ArrayList<>();
      // Go through grid of influence, relative row and column of card influences are used
      //to translate coordinates onto the game board
      for (int row = 0; row < 5; row++) {
        String cells = scanner.nextLine();
        int relativeRow = row - 2;
        for (int col = 0; col < 5; col++) {
          int relativeCol = col - 2;
          if (cells.charAt(col) == 'I') {
            influence.add(new Coordinate(relativeCol, relativeRow));
          }
        }
      }
      Card card = new InfluenceCard(player, name, cost, value, influence);
      cards.add(card);
    }
    return cards;
  }
}
