package aleexx17.spaceinvaders.model;

import javafx.scene.image.Image;

public class Alien {
    private double x;
    private double y;
    private double speed;
    private boolean alive;
    private Thread movementThread;
    private static Image alienImage;

    public Alien(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.alive = true;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Thread getMovementThread() {
        return movementThread;
    }

    public void setMovementThread(Thread movementThread) {
        this.movementThread = movementThread;
    }

    public static void setAlienImage(Image image) {
        alienImage = image;
    }

    public static Image getAlienImage() {
        return alienImage;
    }
}
