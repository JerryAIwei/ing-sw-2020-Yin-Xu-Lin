package it.polimi.ingsw.xyl.model;

import java.io.Serializable;

import static it.polimi.ingsw.xyl.model.Level.*;

/**
 * This class is the abstraction of a single space of the IslandBoard
 * @author Shaoxun
 */
public class Space implements Serializable {
    private Level level = GROUND;      // default GROUND
    private int occupiedBy = -1;  // (playerId * 10 + workerId) if occupied, otherwise -1;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void increaseLevel() {
        if (level.toInt() == 4)
            return;
        Level[] levels = new Level[]{LEVEL1,LEVEL2,LEVEL3,DOME};
        this.level = levels[level.toInt()];
    }

    public int isOccupiedBy() {
        return occupiedBy;
    }

    public void setOccupiedBy(int occupiedBy) {
        this.occupiedBy = occupiedBy;
    }
}
