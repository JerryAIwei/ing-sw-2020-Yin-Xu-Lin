package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.Player;

import static it.polimi.ingsw.xyl.model.GodPower.PROMETHEUS;

public class Prometheus extends Cosplayer {
    private boolean climb =false;
    private boolean firstActionBuild = false;
    public Prometheus(Player player) {
        super(player);
        super.godPower = PROMETHEUS;
    }

    /**
     * Prometheus' move and build:
     * If your worker does not move up(means to a higher level),
     * it may build both before and after moving.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction){
        if(firstActionBuild){
            // unclear about the rule
            // TODO: understand the rule of Prometheus
        }
    }

    /**
     * rometheus' move and build:
     * If your worker does not move up(means to a higher level),
     * it may build both before and after moving.
     *
     * @param worker 'A' or 'B' represent two works of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    public void build(Character worker, Direction direction, boolean buildDome){

    }
}
