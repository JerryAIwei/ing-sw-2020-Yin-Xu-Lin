package it.polimi.ingsw.xyl.model;

/**
 * It represents the worker of player
 *
 * @author Shaoxun
 */
public class Worker {
    private int positionX;
    private int positionY;
    private int fromLevel = 0;     // forced is not moved

    public Worker(int initialPositionX, int initialPositionY) {
        this.positionX = initialPositionX;
        this.positionY = initialPositionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    public int fromLevel() {
        return fromLevel;
    }

    public void setFromLevel(int fromLevel3) {
        this.fromLevel = fromLevel3;
    }
}
