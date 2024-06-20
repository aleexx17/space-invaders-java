package aleexx17.spaceinvaders.controller;

import aleexx17.spaceinvaders.model.Alien;
import aleexx17.spaceinvaders.model.GameState;
import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.concurrent.CopyOnWriteArrayList;

public class GameController {
    @FXML private Canvas gameCanvas;
    @FXML private Pane gamePane;
    @FXML private Button newGameButton;
    @FXML private Button pauseButton;
    @FXML private Button viewScoresButton;
    @FXML private Label scoreLabel;
    @FXML private Label livesLabel;

    private GameState gameState;
    private Image alienImage;
    private boolean isPaused = false;
    private int imageSize = 60;

    public GameController() {
        this.gameState = new GameState();
    }

    @FXML
    public void initialize() {
        alienImage = new Image(getClass().getResourceAsStream("../images/alien.png"));
        Alien.setAlienImage(alienImage);

        newGameButton.setOnAction(e -> handleNewGame());
        pauseButton.setOnAction(e -> handlePauseGame());
        viewScoresButton.setOnAction(e -> handleViewScores());

        gameCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
    }

    @FXML
    private void handleNewGame() {
        startGame();
    }

    @FXML
    private void handlePauseGame() {
        isPaused = !isPaused;
        pauseButton.setText(isPaused ? "Resume" : "Pause");
    }

    @FXML
    private void handleViewScores() {
        // Implement high score display
    }

    private void handleMouseClick(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        for (Alien alien : new CopyOnWriteArrayList<>(gameState.getAliens())) {
            if (alien.isAlive() && mouseX >= alien.getX() && mouseX <= alien.getX() + imageSize &&
                    mouseY >= alien.getY() && mouseY <= alien.getY() + imageSize) {
                alien.setAlive(false);
                gameState.setScore(gameState.getScore() + 1);
                scoreLabel.setText(String.valueOf(gameState.getScore()));

                // Terminate the thread
                if (alien.getMovementThread() != null) {
                    alien.getMovementThread().interrupt();
                }

                // Add visual effect for explosion
                createExplosion(alien.getX() + 25, alien.getY() + 25);

                gameState.getAliens().remove(alien);
                break;
            }
        }
    }

    private void createExplosion(double centerX, double centerY) {
        Circle circle = new Circle(centerX, centerY, 5, Color.RED);
        gamePane.getChildren().add(circle);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), circle);
        scaleTransition.setToX(5);
        scaleTransition.setToY(5);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), circle);
        fadeTransition.setToValue(0);

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.setOnFinished(event -> gamePane.getChildren().remove(circle));
        parallelTransition.play();
    }

    private void startGame() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused) {
                    updateGame();
                    renderGame();
                }
            }
        };
        gameLoop.start();

        Thread alienMovementThread = new Thread(new AlienMovementTask());
        alienMovementThread.setDaemon(true);
        alienMovementThread.start();
    }

    private void updateGame() {
        // Move aliens down at their respective speeds
        for (Alien alien : gameState.getAliens()) {
            alien.setY(alien.getY() + alien.getSpeed());
        }
    }

    private void renderGame() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        for (Alien alien : gameState.getAliens()) {
            if (alien.isAlive()) {
                // Draw the alien image at a smaller size (e.g., 50x50)
                gc.drawImage(Alien.getAlienImage(), alien.getX(), alien.getY(), imageSize, imageSize);
            }
        }
    }

    private class AlienMovementTask extends Task<Void> {
        @Override
        protected Void call() {
            while (true) {
                if (!isPaused) {
                    double speed = 1 + Math.random(); // Speed between 1 and 2
                    double x = 50 + (Math.random() * (gameCanvas.getWidth() - 100)); // Random x position
                    Alien newAlien = new Alien(x, 0, speed);
                    gameState.getAliens().add(newAlien);

                    // Start a thread for this alien's movement
                    Thread movementThread = new Thread(() -> {
                        while (newAlien.isAlive()) {
                            if (!isPaused) {
                                newAlien.setY(newAlien.getY() + newAlien.getSpeed());
                                try {
                                    Thread.sleep(50); // Adjust speed as needed
                                } catch (InterruptedException e) {
                                    // Handle interruption
                                    break;
                                }
                            }
                        }
                    });
                    newAlien.setMovementThread(movementThread);
                    movementThread.start();
                }
                try {
                    Thread.sleep(500 + (long)(Math.random() * 1000)); // Sleep between 500 and 1500 ms
                } catch (InterruptedException e) {
                    // Handle exception
                }
            }
        }
    }
}
