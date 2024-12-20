package com.domino.model;

import java.util.LinkedList;
import java.util.Collections;

public class Boneyard {
    private LinkedList<Domino> boneyard = new LinkedList<>();

    public Boneyard() {
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                boneyard.add(new Domino(i, j));
            }
        }
        Collections.shuffle(boneyard);
    }

    public Domino draw() {
        if (!boneyard.isEmpty()) {
            return boneyard.removeFirst();
        }
        return null;
    }

    public boolean isEmpty() {
        return boneyard.isEmpty();
    }

    public int getSize() {
        return boneyard.size();
    }
}