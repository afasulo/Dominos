package com.domino.game;

import com.domino.model.Domino;
import java.util.LinkedList;

/**
 * This class represents the game board in the Domino game.
 * The game board holds the dominos that have been played during the game.
 *
 * Key functionalities:
 *  - Place the first domino on the board.
 *  - Add a domino to the board.
 *  - Check if a move is legal.
 *  - Get the current values on the left and right ends of the board.
 *  - Get the size of the board.
 *  - Check if the board is empty.
 *  - Display the board as a string.
 */
public class GameBoard {
    private int currentLeftNumber;
    private int currentRightNumber;
    private LinkedList<Domino> board = new LinkedList<>();

    /**
     * Place the first domino on the board.
     * This was important to add o eensure currentLeftNumber and currentRightNumber are set.
     * so the computer can play acordingly
     * @param domino the domino to place
     */
    public void placeFirstDomino(Domino domino) {
        board.add(domino);
        currentLeftNumber = domino.getLeftValue();
        currentRightNumber = domino.getRightValue();
    }

    /**
     * Adds a domino to the board if it's a legal move.
     * Returns true if successful, false otherwise.
     * TODO: Consider throwing an exception for illegal moves instead of returning false.
     * @param side the side to add the domino to (LEFT or RIGHT)
     *             (Side is an enum defined in the game package)
     * @param domino the domino to add
     */
    public boolean addDomino(Side side, Domino domino) {
        if (isLegalMove(side, domino)) {
            if (side == Side.LEFT) {
                board.addFirst(domino);
                currentLeftNumber = domino.getLeftValue();
            } else {  // Side.RIGHT
                board.addLast(domino);
                currentRightNumber = domino.getRightValue();
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if a move is legal.
     * A move is legal if the domino matches the current end value or if either is a wildcard (0).
     * this method saw many different changes
     * @param side the side to add the domino to (LEFT or RIGHT)
     * @param domino the domino to add
     *
     */
    public boolean isLegalMove(Side side, Domino domino) {
        if (side.equals(Side.LEFT)) {
            int i = domino.getRightValue();
            return (i == 0 || this.currentLeftNumber == 0 || i == this.currentLeftNumber);
        }
        int x = domino.getLeftValue();
        return (x == 0 || this.currentRightNumber == 0 || x == this.currentRightNumber);
    }

    public int getSize() {
        return board.size();
    }

    public boolean isEmpty() {
        return board.isEmpty();
    }

    public int getLeftValue() {
        return currentLeftNumber;
    }

    public int getRightValue() {
        return currentRightNumber;
    }


    /**
     * Returns a string representation of the game board.
     * The board is displayed as two rows of dominos.
     * The top row shows the left values of the dominos, and the bottom row shows the right values.
     * The two rows are separated by a newline character.
     * Example:
     * [1, 2] [2, 3]
     *    [2, 2] [3., 0]
     * This method saw so many changes....
     * @return
     */
    public String toString() {
        if (isEmpty()) {
            return "";
        }
        StringBuilder topRow = new StringBuilder();
        StringBuilder bottomRow = new StringBuilder();
        boolean isFirstDomino = true;

        for (int i = 0; i < board.size(); i++) {
            Domino domino = board.get(i);
            if (i % 2 == 0) {
                // Top row
                if (!isFirstDomino) {
                    topRow.append(" ");
                }
                topRow.append("[").append(domino.getLeftValue()).append(", ").append(domino.getRightValue()).append("]");
                isFirstDomino = false;
            } else {
                // Bottom row
                if (bottomRow.length() == 0) {
                    // Add initial spaces for the first bottom domino
                    bottomRow.append("   ");
                } else {
                    bottomRow.append(" ");
                }
                bottomRow.append("[").append(domino.getLeftValue()).append(", ").append(domino.getRightValue()).append("]");
            }
        }

        return topRow.toString() + "\n" + bottomRow.toString();
    }
}