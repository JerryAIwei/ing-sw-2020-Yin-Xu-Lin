package it.polimi.ingsw.xyl.model;

/**
 * Different levels of a specific space
 *
 * @author Shaoxun
 */
public enum Level {
    GROUND,
    LEVEL1,     // the first level block
    LEVEL2,     // the second level block
    LEVEL3,     // the third level block
    DOME;       // usually a dome can only be placed on the top of LEVEL3 blocks

    public int toInt(){
        switch(this){
            default: return 0;
            case LEVEL1: return 1;
            case LEVEL2: return 2;
            case LEVEL3: return 3;
            case DOME: return 4;
        }
    }
}
