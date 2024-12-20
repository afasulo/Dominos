package com.domino.game;

import com.domino.model.*;
import com.domino.ui.GameUI;
import java.util.List;

/**
 * This class represents the game itself.
 * It contains the game board, players, and boneyard.
 * It also handles the game loop and player turns.
 *
 * Key functionalities:
 * - Constructor to initialize the game components.
 * - Method to start the game and handle player turns.
 * - Methods to switch players, play dominos, and determine the winner.
 * - Method to check if the game is over.
 * - Method to handle human and computer player turns.
 * - Method to draw from the boneyard.
 * - Method to place the first domino.
 * - Method to check if a player can draw from the boneyard.
 */
public class Game {
    private Boneyard boneyard;
    private Player humanPlayer;
    private Player computerPlayer;
    private GameBoard gameBoard;
    private Player currentPlayer;
    private int consecutivePasses;
    private GameUI ui;

    /**
     * Initializes game components and players.
     * TODO: Consider allowing custom player names or multiple human players.
     */
    public Game(GameUI ui) {
        this.ui = ui;
        this.gameBoard = new GameBoard();
        this.boneyard = new Boneyard();
        this.humanPlayer = new HumanPlayer("Human", boneyard, gameBoard);
        this.computerPlayer = new ComputerPlayer(boneyard, gameBoard);
        this.currentPlayer = humanPlayer;
        this.consecutivePasses = 0;
    }

    /**
     * Main game loop.
     * Handles turn-based gameplay until a win condition is met.
     */
    public void start() {
        ui.displayWelcomeMessage();
        placeFirstDomino(); // This is here for a reason that took me hours to debug......
        while (!isGameOver()) {
            ui.displayGameState(gameBoard, boneyard.getSize(), currentPlayer, getOtherPlayer());
            if (currentPlayer == humanPlayer) {
                handleHumanTurn();
            } else {
                handleComputerTurn();
            }
            if (!isGameOver()) {
                switchPlayer();
            }
        }
        Player winner = determineWinner();
        ui.displayGameResult(gameBoard, winner, humanPlayer.getScore(), computerPlayer.getScore());
    }

    /**
     * Handles human player's turn.
     * Provides options to play, draw, or quit.
     * TODO: Implement a hint system for new players.
     */
    private void handleHumanTurn() {
        while (true) {
            String move = ui.getPlayerMove();
            try {
                switch (move) {
                    case "p":
                        int index = ui.getDominoIndex();
                        boolean rotate = ui.getRotateDecision();
                        boolean playLeft = ui.getPlayLeftDecision();
                        PlayedDomino played = ((HumanPlayer) humanPlayer).playDomino(gameBoard, index, rotate, playLeft);
                        if (played != null) {
                            playDomino(played);
                            return;
                        } else {
                            ui.displayMessage("Invalid move. Try again.");
                        }
                        break;
                    case "d":
                        if (canDrawFromBoneyard()) {
                            drawFromBoneyard(humanPlayer);
                            return;
                        } else {
                            ui.displayMessage("Cannot draw from boneyard. You have playable dominos.");
                        }
                        break;
                    case "q":
                        ui.displayMessage("Thanks for playing!");
                        System.exit(0);
                    default:
                        ui.displayMessage("Invalid move. Please try again.");
                        break;
                }
            } catch (IllegalArgumentException e) {
                ui.displayMessage("Invalid move: " + e.getMessage() + ". Please try again.");
            }
        }
    }


    /**
     * Places the first domino to start the game.
     * This could be randomized or always done by the human player.
     */
    private void placeFirstDomino() {
        System.out.println("Place the first domino to start the game.");
        ui.displayPlayerHand(humanPlayer.getMyDominos());
        int index = ui.getDominoIndex();
        Domino firstDomino = humanPlayer.getMyDominos().get(index);
        gameBoard.placeFirstDomino(firstDomino);
        humanPlayer.removeDominoFromHand(firstDomino);
        switchPlayer();
    }

    /**
     * Handles computer player's turn.
     * Simulates thinking time for better user experience.
     * TODO: Implement difficulty levels for the Computer. Hueristics, etc.
     */
    private void handleComputerTurn() {
        ui.displayMessage("Computer is thinking...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println("Before computer's move:");
//        System.out.println("Board: " + gameBoard);
//        System.out.println("Computer's hand: " + computerPlayer.getMyDominos());

        ((ComputerPlayer) computerPlayer).play();

//        System.out.println("After computer's move:");
//        System.out.println("Board: " + gameBoard);
//        System.out.println("Computer's hand: " + computerPlayer.getMyDominos());
//
//        ui.displayMessage("Computer has played.");
    }

    /**
     * Plays a domino on the board.
     * Updates game state and resets pass counter.
     * @param played the PlayedDomino object representing the played domino
     */
    private void playDomino(PlayedDomino played) {
        Domino domino = played.rotated ? played.domino : played.domino;
        Side side = played.playLeft ? Side.LEFT : Side.RIGHT;
        gameBoard.addDomino(side, domino);
        consecutivePasses = 0;
    }

    /**
     * Checks if the current player can draw from the boneyard.
     * @return true if the player has no playable dominos and the boneyard is not empty
     */
    private boolean canDrawFromBoneyard() {
        return humanPlayer.getPlayableDominos(gameBoard).isEmpty() && !boneyard.isEmpty();
    }

    /**
     * Draws a domino from the boneyard.
     * Updates game state and increments pass counter.
     * @param player the player to draw the domino
     */
    private void drawFromBoneyard(Player player) {
        if (boneyard.isEmpty()) {
            ui.displayMessage(player.getName() + " passes.");
            consecutivePasses++;
        } else {
            player.addDominoFromBoneyard();
            ui.displayMessage(player.getName() + " drew from the boneyard.");
            consecutivePasses = 0;
        }
    }

    /**
     * Checks if the game is over.
     * The game is over if a player has no dominos, the boneyard is empty, or both players pass consecutively.
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return currentPlayer.getMyDominos().isEmpty() ||
                (boneyard.isEmpty() && consecutivePasses >= 2) ||
                (!canAnyPlayerPlay() && boneyard.isEmpty());
    }

    /**
     * Checks if any player can play a domino.
     * @return true if either player has playable dominos, false otherwise
     */
    private boolean canAnyPlayerPlay() {
        return !humanPlayer.getPlayableDominos(gameBoard).isEmpty() ||
                !computerPlayer.getPlayableDominos(gameBoard).isEmpty();
    }

    /**
     * Determines the winner based on remaining domino values.
     * In case of a tie, the last player to make a move wins.
     * TODO: cooler tiebreader rules
     */
    public Player determineWinner() {
        int humanScore = humanPlayer.getScore();
        int computerScore = computerPlayer.getScore();

        if (humanScore < computerScore) {
            return humanPlayer;
        } else if (computerScore < humanScore) {
            return computerPlayer;
        } else {
            return (currentPlayer == humanPlayer) ? computerPlayer : humanPlayer;
        }
    }


    private void switchPlayer() {
        currentPlayer = (currentPlayer == humanPlayer) ? computerPlayer : humanPlayer;
    }

    private Player getOtherPlayer() {
        return (currentPlayer == humanPlayer) ? computerPlayer : humanPlayer;
    }
}