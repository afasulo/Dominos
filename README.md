# Console Domino Game Object Design Diagram

## Objects

### Game
- **Data:**
  - boneyard: Boneyard
  - humanPlayer: Player
  - computerPlayer: Player
  - gameBoard: GameBoard
  - currentPlayer: Player
  - consecutivePasses: int
  - ui: GameUI

- **Functions:**
  - start()
  - dealInitialHands()
  - handleHumanTurn()
  - handleComputerTurn()
  - playDomino(PlayedDomino)
  - playHumanDomino()
  - canDrawFromBoneyard()
  - drawFromBoneyard(Player)
  - isGameOver()
  - canAnyPlayerPlay()
  - determineWinner()
  - switchPlayer()
  - getOtherPlayer()

### GameBoard
- **Data:**
  - leftChain: List<Domino>
  - rightChain: List<Domino>

- **Functions:**
  - placeFirstDomino(Domino)
  - canPlayLeft(Domino)
  - canPlayRight(Domino)
  - playLeft(Domino)
  - playRight(Domino)
  - isValidPlay(Domino, boolean)
  - getLeftValue()
  - getRightValue()
  - isEmpty()
  - toString()

### Player (Abstract)
- **Data:**
  - hand: List<Domino>
  - name: String

- **Functions:**
  - addDomino(Domino)
  - getHand()
  - getName()
  - getScore()
  - playDomino(GameBoard)
  - hasDomino(int)
  - removeDomino(int)
  - getPlayableDominos(GameBoard)
  - playDominoAtIndex(int)

### HumanPlayer (extends Player)
- **Data:**
  - scanner: Scanner

- **Functions:**
  - playDomino(GameBoard)

### ComputerPlayer (extends Player)
- **Functions:**
  - playDomino(GameBoard)

### Domino
- **Data:**
  - leftValue: int
  - rightValue: int

- **Functions:**
  - getLeftValue()
  - getRightValue()
  - matches(int)
  - getRotated()
  - toString()
  - isWildCard()

### Boneyard
- **Data:**
  - dominos: List<Domino>

- **Functions:**
  - initializeDominos()
  - shuffle()
  - draw()
  - isEmpty()
  - size()

### PlayedDomino
- **Data:**
  - domino: Domino
  - rotated: boolean
  - playLeft: boolean

### GameUI (Interface)
- **Functions:**
  - displayWelcomeMessage()
  - displayGameState(GameBoard, int, Player, Player)
  - displayPlayerHand(List<Domino>)
  - displayMessage(String)
  - getPlayerMove()
  - getDominoIndex()
  - getRotateDecision()
  - getPlayLeftDecision()
  - displayGameResult(GameBoard, Player, int, int)

### ConsoleUI (implements GameUI)
- **Data:**
  - scanner: Scanner

- **Functions:**
  - displayWelcomeMessage()
  - displayGameState(GameBoard, int, Player, Player)
  - displayPlayerHand(List<Domino>)
  - displayMessage(String)
  - getPlayerMove()
  - getDominoIndex()
  - getRotateDecision()
  - getPlayLeftDecision()
  - displayGameResult(GameBoard, Player, int, int)

## Relationships

- Game has-a Boneyard, GameBoard, and GameUI
- Game has-two Players (humanPlayer and computerPlayer)
- Player has-many Dominos (in hand)
- GameBoard has-many Dominos (in leftChain and rightChain)
- Boneyard has-many Dominos
- HumanPlayer and ComputerPlayer inherit from Player
- ConsoleUI implements GameUI
- Game uses PlayedDomino to represent a played move
# Domino Game GUI Object Design Diagram

### GuiUI (implements GameUI, extends Application)
- **Data:**
  - root: VBox
  - gameStateBox: VBox
  - boardBox: FlowPane
  - playerHandBox: HBox
  - drawButton: Button
  - quitButton: Button
  - playerMove: String
  - selectedDominoIndex: int
  - rotateDecision: boolean
  - playLeftDecision: boolean
  - moveLatch: CountDownLatch
  - instance: GuiUI (static)
  - LATCH: CountDownLatch (static final)

- **Functions:**
  - getInstance()
  - start(Stage)
  - addActionListeners()
  - createDominoVisual(Domino, boolean, boolean)
  - addDots(Pane, int, boolean, boolean)
  - createDot(double, double, double)
  - displayWelcomeMessage()
  - displayGameState(GameBoard, int, Player, Player)
  - updatePlayerHand(List<Domino>)
  - displayPlayerHand(List<Domino>)
  - displayMessage(String)
  - getPlayerMove()
  - getDominoIndex()
  - getRotateDecision()
  - getPlayLeftDecision()
  - displayGameResult(GameBoard, Player, int, int)

### Main
- **Functions:**
  - main(String[])

## New Relationships

- GuiUI implements GameUI
- GuiUI extends Application (JavaFX)
- Game uses GuiUI instead of ConsoleUI
- Main creates and starts the Game with GuiUI

## GUI-specific Components

### Visual Elements
- VBox (root container)
- VBox (gameStateBox)
- FlowPane (boardBox)
- ScrollPane (for boardBox)
- HBox (playerHandBox)
- HBox (buttonBox)
- Button (drawButton, quitButton)

### JavaFX Specific
- Stage (primary stage for the application)
- Scene (contains the root VBox)
- Various shape classes (Rectangle, Circle, Line) for domino visuals
- Various effect classes (DropShadow) for visual enhancements
- Animation classes (ScaleTransition) for interactive effects

## Key Differences from Console Version

1. **User Interaction:** 
   - Console version uses text input/output
   - GUI version uses graphical elements (buttons, visual dominos) for interaction

2. **Domino Representation:** 
   - Console version uses text representation
   - GUI version creates visual domino objects with dots and interactive capabilities

3. **Game State Display:** 
   - Console version prints text descriptions
   - GUI version updates visual elements (boardBox, playerHandBox)

4. **Player Input:** 
   - Console version uses scanner for input
   - GUI version uses button clicks and domino interactions

5. **Asynchronous Nature:** 
   - GUI version uses CountDownLatch to manage asynchronous user interactions

6. **Singleton Pattern:** 
   - GUI version implements a singleton pattern for GuiUI to ensure a single instance

7. **JavaFX Integration:** 
   - GUI version extends JavaFX Application class and implements the start() method

8. **Visual Enhancements:** 
   - GUI version includes animations, effects, and custom styling for better user experience
# Example out of Console Version:
Computer has no valid moves. Drawing from boneyard.
After computer's move:
Board: [1, 2] [2, 3] [6, 1] [1, 3] [5, 3] [0, 5] [0, 1] [4, 4] [6, 2] [0, 3] [0, 2]
   [2, 2] [0, 6] [1, 1] [0, 0] [3, 6] [5, 1] [0, 4] [4, 6] [2, 4] [3, 3]
Computer's hand: [[5|6], [3|4], [1|4]]
Computer has played.
Computer has 3 dominos
Boneyard contains 3 dominos
Tray: [[2|5]]
[1, 2] [2, 3] [6, 1] [1, 3] [5, 3] [0, 5] [0, 1] [4, 4] [6, 2] [0, 3] [0, 2]
   [2, 2] [0, 6] [1, 1] [0, 0] [3, 6] [5, 1] [0, 4] [4, 6] [2, 4] [3, 3]
Human's turn
[p] Play Domino
[d] Draw from boneyard
[q] Quit
p
Which domino?
0
Rotate first? (y/n)
n
Left or Right? (l/r)
r

--- Game Over ---
Final board:
[1, 2] [2, 3] [6, 1] [1, 3] [5, 3] [0, 5] [0, 1] [4, 4] [6, 2] [0, 3] [0, 2]
   [2, 2] [0, 6] [1, 1] [0, 0] [3, 6] [5, 1] [0, 4] [4, 6] [2, 4] [3, 3] [2, 5]
Human wins!
Final scores:
Human: 0
Computer: 23
