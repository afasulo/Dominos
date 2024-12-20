package com.domino.model;

import com.domino.game.GameBoard;
import com.domino.game.Side;
import java.util.LinkedList;


/**
 * This class represents a computer player in the Domino game.
 * It extends the Player class and implements logic for selecting and playing dominos.
 *
 * Key functionalities:
 *  - Automatically selects a valid domino to play.
 *  - Methods for finding valid dominos based on the current game board state.
 *  - Logic for matching dominos to the board, including considering wildcards (0 value).
 */
public class ComputerPlayer extends Player {

    public ComputerPlayer(Boneyard boneyard, GameBoard gameBoard) {
        super("Computer", boneyard, gameBoard);
    }


    /**
     * Automatically selects a valid domino to play based on the current game board state.
     * If no valid moves are available, the computer player draws a domino from the boneyard.
     * I dractically simplified this method to make it easier to understannd
     * and to debug issues I was having
     */
    public void play() {
        LinkedList<PlayableDomino> validDominos = findValidDominos();

        if (validDominos.isEmpty()) {
            System.out.println("Computer has no valid moves. Drawing from boneyard.");
            addDominoFromBoneyard();
            return;
        }

        // Choose the first valid domino (dumb strategy)
        PlayableDomino dominoToPlay = validDominos.getFirst();

        playDomino(dominoToPlay);
    }

    /**
     * Finds all valid dominos that can be played on the current game board.
     * This used to create a linked list of all playable dominos then rank the best
     * play but I simplified it to just return the first valid domino found
     * KISS - Keep It Simple Stupid
     * @return a list of PlayableDomino objects representing valid dominos
     */
    private LinkedList<PlayableDomino> findValidDominos() {
        LinkedList<PlayableDomino> validDominos = new LinkedList<>();
        int leftBoardValue = gameBoard.getLeftValue();
        int rightBoardValue = gameBoard.getRightValue();

        for (Domino d : getMyDominos()) {
            // Check without rotation
            if (canMatch(d.getLeftValue(), rightBoardValue)) {
                validDominos.add(new PlayableDomino(d, false, Side.RIGHT));
            } else if (canMatch(d.getRightValue(), leftBoardValue)) {
                validDominos.add(new PlayableDomino(d, false, Side.LEFT));
            }
            // Check with rotation
            else if (canMatch(d.getRightValue(), rightBoardValue)) {
                validDominos.add(new PlayableDomino(d, true, Side.RIGHT));
            } else if (canMatch(d.getLeftValue(), leftBoardValue)) {
                validDominos.add(new PlayableDomino(d, true, Side.LEFT));
            }
        }
        return validDominos;
    }

    /**
     * Checks if a domino value can be matched to a board value.
     * @param dominoValue
     * @param boardValue
     * @return true if the domino can be played on the board, false otherwise
     */
    private boolean canMatch(int dominoValue, int boardValue) {
        return dominoValue == boardValue || dominoValue == 0 || boardValue == 0;
    }

    /**
     * Plays a domino on the game board and removes it from the player's hand.
     * @param pd the PlayableDomino object representing the domino to play
     */
    private void playDomino(PlayableDomino pd) {
        Domino dominoToPlay = pd.needsRotation
                ? new Domino(pd.domino.getRightValue(), pd.domino.getLeftValue())
                : pd.domino;

        gameBoard.addDomino(pd.side, dominoToPlay);
        removeDominoFromHand(pd.domino);
        System.out.println("Computer played " + dominoToPlay + " on the " + pd.side + " side.");
    }

    /**
     * Represents a domino that can be played on the game board.
     */
    private static class PlayableDomino {
        Domino domino;
        boolean needsRotation;
        Side side;


        PlayableDomino(Domino domino, boolean needsRotation, Side side) {
            this.domino = domino;
            this.needsRotation = needsRotation;
            this.side = side;
        }
    }
}
