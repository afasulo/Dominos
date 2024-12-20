package com.domino.model;

public class PlayedDomino {
    public final Domino domino;
    public final boolean rotated;
    public final boolean playLeft;


    /**
     * Constructs a new PlayedDomino object.
     *
     */
    public PlayedDomino(Domino domino, boolean rotated, boolean playLeft) {
        this.domino = domino;
        this.rotated = rotated;
        this.playLeft = playLeft;
    }
}