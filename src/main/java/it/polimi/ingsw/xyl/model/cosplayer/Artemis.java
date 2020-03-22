package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.Player;

import static it.polimi.ingsw.xyl.model.GodPower.ARTEMIS;

public class Artemis extends Cosplayer{

    private boolean firstMove = true;
    private int[] initialPosition = new int[2];

    public Artemis(Player player) {
        super(player);
        super.godPower = ARTEMIS;
    }

    /**
     * Artemis' move: worker may move one additional time,
     * but not back to its initial space.
     *
     * @param worker 'A' or 'B' represent two works of a player.
     * @param direction see Direction class.
     */
    public void move(Character worker, Direction direction) {
        if(firstMove){
            this.initialPosition[0] = this.getPlayer().getWorkerPosition(worker)[0];
            this.initialPosition[1] = this.getPlayer().getWorkerPosition(worker)[1];
            // do something
            this.firstMove = false;
        }else {

        }
    }
}
