package com.domino.model;

import com.domino.game.GameBoard;
import com.domino.game.Side;

/**
 * This class represents a human player in the Domino game.
 * It extends the Player class and provides additional functionalities
 * for a human player to interact with the game board.
 *
 * Key functionalities:
 *  - Constructor to initialize player details and draw initial dominos.
 *  - Method to play a domino on the game board.
 */
public class HumanPlayer extends Player {

    /**
     * Constructor to initialize a human player with a name, boneyard, and game board.
     * @param name the name of the player
     * @param boneyard the boneyard from which the player draws dominos
     * @param gameBoard the game board on which the player plays dominos
     */
    public HumanPlayer(String name, Boneyard boneyard, GameBoard gameBoard) {
        super(name, boneyard, gameBoard);
    }

    /**
     * Method to play a domino on the game board.
     * @param gameBoard the game board on which to play the domino
     * @param index the index of the domino to play
     * @param rotate whether to rotate the domino before playing
     * @param playLeft whether to play the domino on the left side of the board
     * @return a PlayedDomino object representing the played domino
     */
    public PlayedDomino playDomino(GameBoard gameBoard, int index, boolean rotate, boolean playLeft) {
        if (index < 0 || index >= getMyDominos().size()) {
            return null;
        }

        Domino selectedDomino = getMyDominos().get(index);
        if (rotate) {
            selectedDomino = selectedDomino.getRotated();
        }

        Side side = playLeft ? Side.LEFT : Side.RIGHT;
        if (gameBoard.isLegalMove(side, selectedDomino)) {
            removeDomino(index);
            return new PlayedDomino(selectedDomino, rotate, playLeft);
        }

        return null;
    }


}