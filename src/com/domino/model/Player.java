package com.domino.model;

import com.domino.game.GameBoard;
import com.domino.game.Side;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This is an abstract class representing a player in the Domino game.
 * It defines common functionalities for any player type, such as holding dominos,
 * interacting with the game board, and calculating the score.
 *
 * Key functionalities:
 *  - Constructor to initialize player details and draw initial dominos.
 *  - Methods to get the player's name, dominos, and score.
 *  - Methods to manage and play dominos.
 */
public abstract class Player {
    protected String name;
    protected Boneyard boneyard;
    protected LinkedList<Domino> myDominos;
    protected GameBoard gameBoard;

    public Player(String name, Boneyard boneyard, GameBoard gameBoard) {
        this.name = name;
        this.boneyard = boneyard;
        this.gameBoard = gameBoard;
        this.myDominos = new LinkedList<>();
        for (int i = 0; i < 7; i++) {
            addDominoFromBoneyard();
        }
    }

    public String getName() {
        return this.name;
    }

    public LinkedList<Domino> getMyDominos() {
        return this.myDominos;
    }

    public void addDominoFromBoneyard() {
        if (!boneyard.isEmpty()) {
            Domino domino = boneyard.draw();
            if (domino != null) {
                this.myDominos.add(domino);
            }
        }
    }

    public int getScore() {
        return myDominos.stream()
                .mapToInt(d -> d.getLeftValue() + d.getRightValue())
                .sum();
    }

    public List<Domino> getPlayableDominos(GameBoard gameBoard) {
        if (gameBoard.isEmpty()) {
            return new LinkedList<>(myDominos);
        }
        return myDominos.stream()
                .filter(d -> gameBoard.isLegalMove(Side.LEFT, d) || gameBoard.isLegalMove(Side.RIGHT, d) ||
                        gameBoard.isLegalMove(Side.LEFT, d.getRotated()) || gameBoard.isLegalMove(Side.RIGHT, d.getRotated()))
                .collect(Collectors.toList());
    }

    public void removeDomino(Domino domino) {
        this.myDominos.remove(domino);
    }

    public void removeDomino(int index) {
        if (index >= 0 && index < myDominos.size()) {
            this.myDominos.remove(index);
        }
    }

    public void removeDominoFromHand(Domino domino) {
        getMyDominos().removeIf(d ->
                (d.getLeftValue() == domino.getLeftValue() && d.getRightValue() == domino.getRightValue()) ||
                        (d.getLeftValue() == domino.getRightValue() && d.getRightValue() == domino.getLeftValue())
        );
    }

    public int getSize() {
        return this.myDominos.size();
    }

    public boolean isPlayerEmpty() {
        return this.myDominos.isEmpty();
    }

    @Override
    public String toString() {
        return myDominos.stream()
                .map(Domino::toString)
                .collect(Collectors.joining(" "));
    }
}