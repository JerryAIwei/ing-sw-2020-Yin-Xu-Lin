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
    private static final Space[][] spaces = new Space[5][5];
    private boolean noMoveUp;

    public static Space[][] getSpaces() {
        return spaces;
    }

    public static void setSpaces(int x, int y, Space space){
        if(x>=0 && y>=0 && x<=4 && y <=4)
            spaces[x][y] = space;
        else
            throw new IllegalArgumentException("Space should in IslandBoard!");
    }

    public boolean isNoMoveUp() {
        return noMoveUp;
    }

    public void setNoMoveUp(boolean noMoveUp) {
        this.noMoveUp = noMoveUp;
    }

    public void updateIslandBoard(){

    }
}
