package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.Player;

public class Athena extends Cosplayer{
    public Athena(Player player) {
        super(player);
    }

    /**
     * Athena's power: if one of your workers moved up on
     * your last turn, opponent workers cannot move up this turn.
     *
     * @param worker 'A' or 'B' represent two works of a player.
     * @param direction see Direction class.
     */
    public void move(Character worker, Direction direction){

    }
}
