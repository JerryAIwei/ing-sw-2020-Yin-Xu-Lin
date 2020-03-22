package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import static it.polimi.ingsw.xyl.model.GodPower.DEMETER;

public class Demeter extends Cosplayer {
    private boolean firstBuild = true;
    private int[] firstBuildPositions = new int[2];

    public Demeter(Player player) {
        super(player);
        super.godPower = DEMETER;
    }

    /**
     * Demeter's build: worker may build one additional time,
     * but not on the same space.
     *
     * @param worker 'A' or 'B' represent two works of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    public void build(Character worker, Direction direction, boolean buildDome){
        if(firstBuild){
            int targetPositionX = 0; //TODO: real target position which is available to build
            int targetPositionY = 0;

            // do something

            this.firstBuildPositions[0] = targetPositionX;
            this.firstBuildPositions[1] = targetPositionY;
            this.firstBuild = false;
        }else{

        }
    }
}
