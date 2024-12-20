package com.domino.model;

/*
 * This class represents a single domino in the game.
 * A domino has two values (left and right) and can be rotated.
 *
 * Key functionalities:
 *  - Getters for the left and right values.
 *  - Methods to check for double dominos and rotation.
 */
public class Domino {
    private final int leftValue;
    private final int rightValue;

    public Domino(int left, int right) {
        this.leftValue = left;
        this.rightValue = right;
    }

    public int getLeftValue() {
        return leftValue;
    }

    public int getRightValue() {
        return rightValue;
    }

    public Domino getRotated() {
        return new Domino(rightValue, leftValue);
    }

    @Override
    public String toString() {
        return "[" + leftValue + "|" + rightValue + "]";
    }
}