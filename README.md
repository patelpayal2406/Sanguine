# Sanguine Game

## Overview
This codebase represents a game of Sanguine, based on the popular game [Queen's Blood](https://finalfantasy.fandom.com/wiki/Queen%27s_Blood).

All criteria and function of the game is designed and followed after [Assignment 5](https://northeastern.instructure.com/courses/225692/assignments/2952482).

## Quick Start
You can run a game of Sanguine using Gradle with the following parameters:
- Jar File Name (e.g. `java -jar f25-hw06-group-placid-bread-6134-dev.jar`)
- Deck Config File Name (e.g. `deck.config`)  3 5 5 true
- Number of rows on the board (positive integer)
- Number of columns on the board (greater than 1, odd integer)
- Number of cards available in players' hands (less than 1/3 of deck size which is a total of 30)
- Shuffled card deck (`true` or `false`)

For example:
`./gradlew run --args="java -jar f25-hw06-group-placid-bread-6134-dev.jar deck.config 3 5 5 true"`

## Key Components
1. SanguineModel

The model (`sanguine.model`) manages the state of the game as well as providing methods for playing cards, determining the winner, and maintaining the turn order.

To view and store the game, we created a `Board` interface, implemented by the `InfluenceBoard` class. This class contains the cells as well as the methods for interacting with the board. The `InfluenceBoard` class also exposes a `toString()` method to the model for textually representing the game board.

2. SanguineController


3. Supporting Classes

To represent the board (`sanguine.model`), we created the `Cell` interface which is implemented by a `ValueCell` and `PawnCell` to represent the two different types of cells that can exist on the board. An empty cell is represented as `null`.

**Coordinate System**: The board uses a (row, column) coordinate system where (0,0) is the top-left cell.
- Rows increase downwards.
- Columns increase to the right.
- Card influence patterns are defined relative to the card's center (0,0), with negative values representing positions above/left and positive values representing positions below/right.

To represent players (`sanguine.model`), we created a `Player` enum for a `RED` and `BLUE` player.

To represent cards (`sanguine.model`), we created a `Card` interface, implemented by the `InfluenceCard` class. This class contains all of the methods and state storage to get and track the card's cost, value, name, and influence. An additional `Coordinate` class was made to represent the cells of influence relative to the card.

## Changes for Part 2

1. `Sanguine`

- Modified `main()` to pass in arguments from command line to `startGame()`. Added the customization of player hand size and enablement of shuffling the card deck.
- Modified `loadConfig()` relativeRow and relativeCol when reading deck configurations to reflect the flipped orientation of x-axis and y-axis in the view component.

2. `SanguineGame`

- Modified `startGame()` to add the card deck shuffling function. Calls the new `dealDeck()` method to set up each player's hand.
- Added `dealDeck()` and `drawCardToHand()` to give each player the first 5 cards of the deck at the start of the game.
- Added `removeCardFromDeck()` and `removeCardFromHand()` to pass cards between decks and hands after cards are placed on the board.
- Modified `gameOver()` to return true if there were 2 consecutive and player alternating passes or if the board is full of value cells (placed cards).
- Modified `pass()` to keep track of which player passed their turn so consecutive passes only increase if they were made by both players.

3. `InfluenceBoard`

- Added `getTotalScore()` to break down player scores into individual row scores and total scores.
- Added `getCells()` to expose a copy list of board cells for other components to look at.

4. `Coordinate` and `InfluenceCard` 

- Added `equals()` and `hashCode()` methods for new `Move` and `HandPanel` classes
- Modified `playCard()` in `InfluenceCard` to reflect the flipped axis orientation when influencing cells of the board.

## New Interfaces/Classes for Part 2

1. `ReadonlySanguineModel`

- This interface only holds the observer methods of the model while `SanguineModel` holds the mutator methods. This ensures that the view does not mutate the game when the model is passed into it.

2. `SanguineGuiController`

- Calls `SanguineGame` to start the game with the user input arguments.
- Calls `SanguineFrame` to highlight/unhighlight cells and cards that were selected/deselected with mouse clicks.
- For this stub controller, the index of the card in the player's hand and the coordinate of the cell in the board grid is printed when each is clicked.

3. `SanguineFrame`

- There are two frames for each player to interact with and each window has a `BoardPanel` and `HandPanel` stacked on top of each other.
- A KeyListener is added to each frame for the controller to detect when a player has passed their turn ('p' key) or confirmed the placement of a selected card on a selected cell ('c' key).

4. `BoardPanel`

- This panel displays the board cells and row scores for each player. 
- The board contains gray empty cells, player-color pawn cells that were influenced, and player-color value cells where cards have been placed. 
- If a cell is selected, the background color changes to cyan.

5. `HandPanel`

- This panel displays each player's hand which can be differentiated by the background colors on each of the frames. 
- A card displays its name, cost, value, and influence grid. 
- The influence grid contains gray uninfluenced cells, cyan influenced cells, and an orange center cell for the relative position of influences. 
- If a card is selected, the background color changes to cyan.

6. `ClickListener`

- This `FeatureListener` responds to mouse events and converts mouse coordinates to logical row/column coordinates for the controller. 
- The MouseListener figures out which panel was clicked on to call the appropriate converter method.

7. `Strategy`

- An interface that defines the behavior for strategies to generate moves based on the current game state.
- This allows for different AI implementations to be easily swapped or combined.

8. `FillFirstStrategy`

- A simple strategy that finds the first valid move.
- It iterates through the cards in hand and available board cells, selecting the first legal placement found.

9. `MaximizeRowScoreStrategy`

- A strategy that prioritizes winning rows.
- It identifies rows where the player is currently losing or tied and selects moves that would result in the highest score for that row.

10. `ControlTheBoardStrategy`

- A strategy focused on board presence.
- It selects moves that maximize the number of cells owned (pawns and placed cards) by the player after the move.

11. `MinimaxStrategy`

- A defensive strategy that looks one step ahead.
- It evaluates the opponent's best possible response to each potential move and chooses the move that minimizes the opponent's maximum gain.

12. `CompositeStrategy`

- A strategy that combines multiple strategies.
- It chains strategies together, using the output of one strategy as the candidate pool for the next, allowing for complex decision-making processes.

13. `Move`

- A helper class to represent a potential move, consisting of a card to play and the target row and column.
- Used by strategies to return lists of suggested actions.

14. `MockBoard`

- A mock implementation of the `Board` interface for testing purposes.
- Allows setting up specific board states and logging method calls to verify interactions without relying on the full game logic.

15. `MockSanguineModel`

- A mock implementation of the `SanguineModel` interface.
- Used with `MockBoard` to test strategies through controlled game states.

## Changes for Part 3

1. `Board`

- Added a separate method that checks if a move is valid or not

2. `ReadonlySanguineModel`

- Added observer methods to allow clients to easily access more model information

3. `FeatureListener`

- Added confirmMove() for the controller to call the model after a player has pressed the 'c' key
- Added pass() for the controller to call the model after a player has pressed the 'p' key

4. `SanguineFrame`

- Added methods to send messages to the view frames about player actions and model status notifications 

5. `SanguineGuiController`

- Subscribes to both FeaturesListener and ModelListener to connect messages between model and view

6. `SanguineGame`

- Added methods to notify the controller about model statuses such as player turn switching, turn passing, error throwing, and game over details

## New Interfaces/Classes for Part 2

1. `ModelListener`

- This interface is the publisher for model status notifications that the controller subscribes to
- It's methods, turnChanged(), gameOver(), errorOccurrence(), and turnPassed(), tell the controllers for each player about the respective events that have occurred in the model component 
- The controller is able to send these notifications to the view for the players to be aware about
