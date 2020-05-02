package it.polimi.ingsw.xyl.model;

/**
 * This class is the abstraction of the MAP of the Santorini Game,
 * (or also called IslandBoard, where the players put workers and
 * build Blocks and Domes.)
 * Attribute noMoveUp is used to realize GodPower Athena.
 *
 * @author Shaoxun
 */
public class IslandBoard {
    private final Space[][] spaces;
    private boolean noLevelUp = false;

    public IslandBoard(){
        spaces = new Space[5][5];
        for (int i = 0; i !=5; i++){
            for (int j = 0; j !=5; j++){
                spaces[i][j]= new Space();
            }
        }
    }

    public Space[][] getSpaces() {
        return spaces;
    }

    public boolean isNoLevelUp() {
        return noLevelUp;
    }

    public void setNoLevelUp(boolean noLevelUp) {
        this.noLevelUp = noLevelUp;
    }

}
