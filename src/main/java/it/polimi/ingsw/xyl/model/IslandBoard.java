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
    private Space[][] spaces = new Space[5][5];
    private boolean noMoveUp;

    public IslandBoard(){
        for (int i = 0; i !=5; i++){
            for (int j = 0; j !=5; j++){
                spaces[i][j]= new Space();
            }
        }
    }

    public Space[][] getSpaces() {
        return spaces;
    }

    public boolean isNoMoveUp() {
        return noMoveUp;
    }

    public void setNoMoveUp(boolean noMoveUp) {
        this.noMoveUp = noMoveUp;
    }

    public void updateIslandBoard(){    }
}
