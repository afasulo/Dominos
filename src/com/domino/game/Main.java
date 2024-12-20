package com.domino.game;

import com.domino.ui.GuiUI;
import com.domino.ui.GameUI;
import com.domino.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        // switch to GameUI = GuiUI.getInstance() to use the GUI
        // and switch to GameUI = new ConsoleUI() to use the console
        GameUI ui = GuiUI.getInstance();
        Game game = new Game(ui);
        game.start();
    }
}