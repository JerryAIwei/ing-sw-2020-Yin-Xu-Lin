package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.Player;

import static it.polimi.ingsw.xyl.model.GodPower.APOLLO;

public class Apollo extends Cosplayer{

    public Apollo(Player player) {
        super(player);
        super.godPower = APOLLO;
    }

    /**
     * Apollo's move: worker may move into an opponent worker's space
     * by forcing their worker to the space to the space yours just vacated.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction){

    }

}
