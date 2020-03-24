package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.Player;

import static it.polimi.ingsw.xyl.model.GodPower.HEPHAESTUS;

public class Hephaestus extends Cosplayer {
    private boolean firstBuild = true;
    private Direction firstBuildDirection = null;

    public Hephaestus(Player player) {
        super(player);
        super.godPower = HEPHAESTUS;
    }

    /**
     * Hephaestus' build: worker may build one additional block(not dome)
     * on top of your first block.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    public void build(int worker, Direction direction, boolean buildDome){
        if(firstBuild){
            // do something

            this.firstBuildDirection= direction;
            this.firstBuild = false;
        }else{
            if(direction == firstBuildDirection){

            }else{

            }
        }
    }
}
