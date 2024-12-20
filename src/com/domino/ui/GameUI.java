package com.domino.ui;

import com.domino.model.Domino;
import com.domino.model.Player;
import com.domino.game.GameBoard;
import java.util.List;

public interface GameUI {
    void displayWelcomeMessage();
    void displayGameState(GameBoard board, int boneyardSize, Player currentPlayer, Player otherPlayer);
    void displayPlayerHand(List<Domino> hand);
    void displayMessage(String message);
    String getPlayerMove();
    int getDominoIndex();
    boolean getRotateDecision();
    boolean getPlayLeftDecision();
    void displayGameResult(GameBoard board, Player winner, int humanScore, int computerScore);
}