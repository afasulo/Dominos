package com.domino.ui;

import com.domino.model.Domino;
import com.domino.model.Player;
import com.domino.game.GameBoard;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements GameUI {
    private Scanner scanner;

    public ConsoleUI() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void displayWelcomeMessage() {
        System.out.println("Dominos game by Adam!");
    }

    /**
     * Display the current game state. This is called at the start of the game and after each move.
     * @param board The current game board
     * @param boneyardSize The number of dominos in the boneyard
     * @param currentPlayer The player whose turn it is
     * @param otherPlayer The other player
     */
    @Override
    public void displayGameState(GameBoard board, int boneyardSize, Player currentPlayer, Player otherPlayer) {
        System.out.println(otherPlayer.getName() + " has " + otherPlayer.getMyDominos().size() + " dominos");
        System.out.println("Boneyard contains " + boneyardSize + " dominos");
        if (currentPlayer.getName().equals("Human")) {
            System.out.println("Tray: " + currentPlayer.getMyDominos());
        }
        System.out.println(board.toString());
        System.out.println(currentPlayer.getName() + "'s turn");
    }


    @Override
    public void displayPlayerHand(List<Domino> hand) {
        System.out.println("Your hand: " + hand);
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Get the player's move choice: play domino, draw from boneyard, or quit.
     *  Fixed a bug where the input was not trimmed before returning
     *  and was causing this thing to print twice. argggg
     * @return The player's move choice
     */
    @Override
    public String getPlayerMove() {
        String input = "";
        while (true) {
            System.out.println("[p] Play Domino");
            System.out.println("[d] Draw from boneyard");
            System.out.println("[q] Quit");
            input = scanner.nextLine().trim().toLowerCase();

            // If the input is non-empty, break the loop
            if (!input.isEmpty()) {
                break;
            }
        }
        return input;
    }


    /**
     * Get the index of the domino the player wants to play.
     * @return The index of the domino
     * @return
     */
    @Override
    public int getDominoIndex() {
        System.out.println("Which domino?");
        int index = scanner.nextInt();
        scanner.nextLine();  // Consume leftover newline
        return index;
    }

    /**
     * Get the player's decision to rotate the domino before playing.
     * @return The player's decision
     */
    @Override
    public boolean getRotateDecision() {
        System.out.println("Rotate first? (y/n)");
        boolean decision = scanner.next().toLowerCase().startsWith("y");
        scanner.nextLine();  // Consume leftover newline
        return decision;
    }

    /**
     * Get the player's decision to play the domino on the left or right side of the board.
     * @return The player's decision
     */
    @Override
    public boolean getPlayLeftDecision() {
        System.out.println("Left or Right? (l/r)");
        boolean decision = scanner.next().toLowerCase().startsWith("l");
        scanner.nextLine();  // Consume leftover newline
        return decision;
    }

    /**
     * Display the final game result. This is called when the game is over.
     * @param board The final game board
     * @param winner The winning player
     * @param humanScore The human player's score
     * @param computerScore The computer player's score
     */
    @Override
    public void displayGameResult(GameBoard board, Player winner, int humanScore, int computerScore) {
        System.out.println("\n--- Game Over ---");
        System.out.println("Final board:\n" + board);
        System.out.println(winner.getName() + " wins!");
        System.out.println("Final scores:");
        System.out.println("Human: " + humanScore);
        System.out.println("Computer: " + computerScore);
    }
}