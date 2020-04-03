package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.Player;

import static it.polimi.ingsw.xyl.model.GodPower.ATLAS;

/**
 * @author Lin Xin
 */

public class Atlas extends Cosplayer {


    public Atlas(Player player) {
        super(player);
        super.godPower = ATLAS;
    }

    /**
     * Atlas' build: worker may build a dome at any level.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    public void build(int worker, Direction direction, boolean buildDome){
        if (buildDome){

        }
        else{

        }
    }
}
