package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.Player;

import static it.polimi.ingsw.xyl.model.GodPower.MINOTAUR;

public class Minotaur extends Cosplayer {

    public Minotaur(Player player) {
        super(player);
        super.godPower = MINOTAUR;
    }

    /**
     * Minotaur's move: worker may move into an opponent worker's
     * space, if their worker can be forced one space straight backwards
     * to an unoccupied space at ANY level.
     *
     * @param worker 'A' or 'B' represent two works of a player.
     * @param direction see Direction class.
     */
    public void move(Character worker, Direction direction){

    }
}
