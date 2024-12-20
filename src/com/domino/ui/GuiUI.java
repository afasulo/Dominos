package com.domino.ui;

import com.domino.model.Domino;
import com.domino.model.Player;
import com.domino.game.GameBoard;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GuiUI extends Application implements GameUI {
    /**
     * The root node of the scene graph for the game UI.
     */
    private VBox root;

    /**
     * A container for displaying the game state.
     */
    private VBox gameStateBox;

    /**
     * A container for displaying the game board.
     */
    private FlowPane boardBox;

    /**
     * A container for displaying the player's hand.
     */
    private HBox playerHandBox;

    /**
     * A button for the player to draw a domino from the boneyard.
     */
    private Button drawButton;

    /**
     * A button for the player to quit the game.
     */
    private Button quitButton;

    /**
     * A string representing the player's move. It can be "d" for draw, "p" for play, or "q" for quit.
     */
    private String playerMove;

    /**
     * The index of the domino that the player has decided to play.
     */
    private int selectedDominoIndex;

    /**
     * A boolean indicating whether the player has decided to rotate the domino before playing it.
     */
    private boolean rotateDecision;

    /**
     * A boolean indicating whether the player has decided to play the domino on the left side of the game board.
     */
    private boolean playLeftDecision;

    /**
     * A CountDownLatch used to wait for the player to make a move.
     */
    private CountDownLatch moveLatch;

    /**
     * The singleton instance of the GuiUI class.
     */
    private static GuiUI instance;

    /**
     * A CountDownLatch used to wait for the JavaFX application to start.
     */
    private static final CountDownLatch LATCH = new CountDownLatch(1);

    /**
     * returns the singlton instance of the GuiUI class
     * if the instacne DNE it launches the JavaFX app in a new thread and waits till its started
     * once the jfx app is started it sets the instance and returns it
     */
    public static GuiUI getInstance() {
        if (instance == null) {
            new Thread(() -> Application.launch(GuiUI.class)).start();
            try {
                LATCH.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


    /**
     * this is the main entry point for all jfx apps
     * called afteer init() has returned and after the OS is ready for the jfx to run
     * this is where the jfx app is initialized
     */
    @Override
    public void start(Stage primaryStage) {
        // set the instance to this instance of GuiUI
        instance = this;

        primaryStage.setTitle("Dominos");

        // init root vbox with padding and background color
        root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #F0F8FF;");

        // create new scene root Vbox and dimensions
        Scene scene = new Scene(root, 1000, 700);

        // TODO: this isnt working
        // Try to load CSS, but don't crash if it's not found
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS file not found. Using default styles.");
        }

        // create new Vbox for gamestate box and set alignment
        gameStateBox = new VBox(5);
        gameStateBox.setAlignment(Pos.CENTER);

        // create new flowpane called baordBox
        boardBox = new FlowPane(5, 5);
        boardBox.setPrefWrapLength(900);
        boardBox.setAlignment(Pos.CENTER);

        // create new scrollpane for the boardBox
        ScrollPane boardScrollPane = new ScrollPane(boardBox);
        boardScrollPane.setFitToWidth(true);
        boardScrollPane.setPrefViewportHeight(300);

        // create new hbox for the player hand box and set alignment
        playerHandBox = new HBox(5);
        playerHandBox.setAlignment(Pos.CENTER);


        // create new buttons for draw and quit
        drawButton = new Button("Draw From Boneyard");
        quitButton = new Button("Quit");

        // create new hbox for buttons
        HBox buttonBox = new HBox(10, drawButton, quitButton);
        buttonBox.setAlignment(Pos.CENTER);

        // add all nodes to the root vbox
        root.getChildren().addAll(gameStateBox, boardScrollPane, playerHandBox, buttonBox);

        // add action listeners to the buttons
        addActionListeners();

        // set the scene on the primary stage and show it
        primaryStage.setScene(scene);
        primaryStage.show();

        // count down the LATCH to indicate the jfx app has started running
        LATCH.countDown();
    }


    /**
     *  add action listeners to draw from boneyard and quit button
     *  when draw button is clicked, it sets the player move to "d" and counts down the moveLatch
     *  when quit button is clicked, it sets the player move to "q" and counts down the moveLatch
     */
    private void addActionListeners() {
        drawButton.setOnAction(e -> {
            playerMove = "d";
            moveLatch.countDown();
        });

        quitButton.setOnAction(e -> {
            playerMove = "q";
            moveLatch.countDown();
        });
    }


    /**
     * create a visual representation of domino
     */
    private Node createDominoVisual(Domino domino, boolean isPlayable, boolean isHorizontal) {
        //create stackpane to hold the domino
        StackPane dominoPane = new StackPane();
        double width = isHorizontal ? 80 : 40;
        double height = isHorizontal ? 40 : 80;
        dominoPane.setPrefSize(width, height);

        // create background rectangle for the domino
        Rectangle background = new Rectangle(width, height);
        background.setFill(Color.IVORY);
        background.setStroke(Color.BLACK);

        // pane to hold the dots or pips on the domino
        Pane dotsPane = new Pane();
        dotsPane.setPrefSize(width, height);

        // add dots (aka pips) to domino
        addDots(dotsPane, domino.getLeftValue(), isHorizontal, true);
        addDots(dotsPane, domino.getRightValue(), isHorizontal, false);

        // line sperator for the domino. puts it in the middle of the domino if horizontal, or in the middle of the domino if vertical
        Line separator = new Line();
        if (isHorizontal) {
            separator.setStartX(width / 2);
            separator.setEndX(width / 2);
            separator.setStartY(2);
            separator.setEndY(height - 2);
        } else {
            separator.setStartX(2);
            separator.setEndX(width - 2);
            separator.setStartY(height / 2);
            separator.setEndY(height / 2);
        }
        separator.setStroke(Color.BLACK);

        // paint the domino. add backround, dots, and separator to the dominoPane
        dominoPane.getChildren().addAll(background, dotsPane, separator);

        // if the domino is playable add a dropshadow effect to the domino
        if (isPlayable) {
            dominoPane.setEffect(new DropShadow(5, Color.GOLD));
        }

        // add scale transitions to the dominopane when hovered over
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(100), dominoPane);
        scaleIn.setToX(1.1);
        scaleIn.setToY(1.1);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(100), dominoPane);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);

        // mouse event handlers for the dominoPane
        // when mouse enters the dominoPane, play the scaleIn transition
        // when mouse exits the dominoPane, play the scaleOut transition
        // TODO: investiagte why drawFromBoneyard is not working
        dominoPane.setOnMouseEntered(e -> scaleIn.play());
        dominoPane.setOnMouseExited(e -> scaleOut.play());

        dominoPane.setOnMouseClicked(e -> {
            playerMove = "p";
            selectedDominoIndex = playerHandBox.getChildren().indexOf(dominoPane);
            rotateDecision = e.isShiftDown();
            playLeftDecision = e.getX() < dominoPane.getWidth() / 2;
            moveLatch.countDown();
        });

        return dominoPane;
    }


    /**
     * add dots (aka pips) to a dominooo visual representation
     * @param pane the Pane where dots are added
     * @param value the numbers of dots to add (1-6)
     * @param isHorizontal whether the domino is horizontal. they are horizontal if they are played on the board and verticlal if they are in the player's hand or aka tray
     * @param isLeft wheather to draw the dots on the left side of the domino or the right side. true for left, false for right
     */
    private void addDots(Pane pane, int value, boolean isHorizontal, boolean isLeft) {
        double width = pane.getPrefWidth();
        double height = pane.getPrefHeight();
        double dotRadius = Math.min(width, height) / 12; // Adjust dot size based on domino size

        double xOffset = isHorizontal ? (isLeft ? 0 : width / 2) : 0;
        double yOffset = isHorizontal ? 0 : (isLeft ? 0 : height / 2);

        double[][] positions;
        switch (value) {
            case 1:
                positions = new double[][]{{0.5, 0.5}};
                break;
            case 2:
                positions = new double[][]{{0.25, 0.25}, {0.75, 0.75}};
                break;
            case 3:
                positions = new double[][]{{0.25, 0.25}, {0.5, 0.5}, {0.75, 0.75}};
                break;
            case 4:
                positions = new double[][]{{0.25, 0.25}, {0.25, 0.75}, {0.75, 0.25}, {0.75, 0.75}};
                break;
            case 5:
                positions = new double[][]{{0.25, 0.25}, {0.25, 0.75}, {0.5, 0.5}, {0.75, 0.25}, {0.75, 0.75}};
                break;
            case 6:
                positions = new double[][]{{0.25, 0.2}, {0.25, 0.5}, {0.25, 0.8}, {0.75, 0.2}, {0.75, 0.5}, {0.75, 0.8}};
                break;
            default:
                positions = new double[][]{};
        }

        for (double[] pos : positions) {
            double x = xOffset + pos[0] * width / 2;
            double y = yOffset + pos[1] * height / 2;
            pane.getChildren().add(createDot(x, y, dotRadius));
        }
    }

    /**
     *  paint the dot (or pip) onto the domino
     * @param x the x cordinate of the center of the dot
     * @param y the y cordinate of the center of the dot
     * @param radius the radius of the dot
     * @return the dot (or pip) painted onto the domino
     */
    private Circle createDot(double x, double y, double radius) {
        Circle dot = new Circle(x, y, radius, Color.NAVY);
        dot.setStroke(Color.BLACK);
        dot.setStrokeWidth(0.5);
        return dot;
    }

    @Override
    public void displayWelcomeMessage() {
        Platform.runLater(() -> {
            if (gameStateBox != null) {
                Label welcomeLabel = new Label("Welcome to Dominos!");
                welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
                gameStateBox.getChildren().add(welcomeLabel);
            }
        });
    }

    @Override
    public void displayGameState(GameBoard board, int boneyardSize, Player currentPlayer, Player otherPlayer) {
        Platform.runLater(() -> {
            if (gameStateBox != null && boardBox != null && playerHandBox != null) {
                gameStateBox.getChildren().clear();
                gameStateBox.getChildren().addAll(
                        new Label("Boneyard contains " + boneyardSize + " dominos"),
                        new Label(otherPlayer.getName() + " has " + otherPlayer.getMyDominos().size() + " dominos"),
                        new Label(currentPlayer.getName() + "'s turn")
                );

                boardBox.getChildren().clear();
                String[] boardDominos = board.toString().split(" ");
                boolean isTopRow = true;
                for (String dominoStr : boardDominos) {
                    if (!dominoStr.equals("|")) {
                        String[] values = dominoStr.replaceAll("[\\[\\]]", "").split("\\|");
                        int leftValue = Integer.parseInt(values[0]);
                        int rightValue = Integer.parseInt(values[1]);
                        Domino domino = new Domino(leftValue, rightValue);
                        Node dominoNode = createDominoVisual(domino, false, true);
                        if (!isTopRow) {
                            dominoNode.setTranslateY(30); // Stagger the bottom row
                        }
                        boardBox.getChildren().add(dominoNode);
                        isTopRow = !isTopRow; // Alternate between top and bottom row
                    }
                }

                if (currentPlayer.getName().equals("Human")) {
                    updatePlayerHand(currentPlayer.getMyDominos());
                }
            }
        });
    }

    private void updatePlayerHand(List<Domino> hand) {
        Platform.runLater(() -> {
            playerHandBox.getChildren().clear();
            for (Domino domino : hand) {
                playerHandBox.getChildren().add(createDominoVisual(domino, true, false));
            }
        });
    }

    @Override
    public void displayPlayerHand(List<Domino> hand) {
        updatePlayerHand(hand);
    }

    @Override
    public void displayMessage(String message) {
        Platform.runLater(() -> {
            Label messageLabel = new Label(message);
            messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #4A4A4A;");
            gameStateBox.getChildren().add(messageLabel);
        });
    }

    @Override
    public String getPlayerMove() {
        moveLatch = new CountDownLatch(1);
        try {
            moveLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return playerMove;
    }

    @Override
    public int getDominoIndex() {
        return selectedDominoIndex;
    }

    @Override
    public boolean getRotateDecision() {
        return rotateDecision;
    }

    @Override
    public boolean getPlayLeftDecision() {
        return playLeftDecision;
    }

    @Override
    public void displayGameResult(GameBoard board, Player winner, int humanScore, int computerScore) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(winner.getName() + " wins!");
            alert.setContentText("Final scores:\nHuman: " + humanScore + "\nComputer: " + computerScore);

            // Custom styling for the alert
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            dialogPane.getStyleClass().add("custom-alert");

            alert.showAndWait();
        });
    }
}