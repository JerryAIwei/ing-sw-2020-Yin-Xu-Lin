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
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction) {
        if(firstMove){
            this.initialPosition[0] = this.getPlayer().getWorkers()[worker].getPositionX();
            this.initialPosition[1] = this.getPlayer().getWorkers()[worker].getPositionY();
            // do something
            this.firstMove = false;
        }else {

        }
    }
}
