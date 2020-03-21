package it.polimi.ingsw.xyl.model;

import static it.polimi.ingsw.xyl.model.Level.*;

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

    public void increaseLevel() {
        if(level.toInt() == 0)
            this.level = LEVEL1;
        else if(level.toInt() == 1)
            this.level = LEVEL2;
        else if(level.toInt() == 2)
            this.level = LEVEL3;
        else if(level.toInt() == 3)
            this.level = DOME;
    }

    public int isOccupiedByPlayer() {
        return occupiedByPlayer;
    }

    public void setOccupiedByPlayer(int occupiedByPlayerID) {
        this.occupiedByPlayer = occupiedByPlayerID;
    }
}
