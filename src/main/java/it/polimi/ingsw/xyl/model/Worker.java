package it.polimi.ingsw.xyl.model;

/**
 * It represents the worker of player
 *
 * @author Shaoxun
 */
public class Worker {
    private int positionX;
    private int positionY;
    private int fromLevel = 0;
    private boolean forced = false;

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

    public void setFromLevel(int fromLevel) {
        this.fromLevel = fromLevel;
    }

    public boolean isForced() {
        return forced;
    }

    public void setForced() {
        this.forced = true;
    }

    public void resetForced(){
        this.forced = false;
    }
}
