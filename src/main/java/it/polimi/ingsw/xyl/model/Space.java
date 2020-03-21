package it.polimi.ingsw.xyl.model;

import static it.polimi.ingsw.xyl.model.Level.GROUND;

/**
 * This class is the abstraction of a single space of the IslandBoard
 * @author Shaoxun
 */
public class Space {
    private Level level = GROUND;      // default GROUND
    private int occupiedByPlayer = 0;  // playerId if occupied, otherwise 0;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int isOccupiedByPlayer() {
        return occupiedByPlayer;
    }

    public void setOccupiedByPlayer(int occupiedByPlayerID) {
        this.occupiedByPlayer = occupiedByPlayerID;
    }
}
