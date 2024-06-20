package aleexx17.spaceinvaders.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameState {
    private Player player;
    private List<Alien> aliens;
    private int score;
    private int lives = 3;

    public GameState() {
        this.player = new Player();
        this.aliens = new CopyOnWriteArrayList<>();
        this.score = 0;

        // Initialize aliens with random speeds
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                double speed = 1 + Math.random(); // Speed between 1 and 2
                aliens.add(new Alien(50 + i * 100, 50 + j * 50, speed));
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public List<Alien> getAliens() {
        return aliens;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives(){
        return this.lives;
    }

    public void reduceLives(){
        this.lives--;
    }
}
