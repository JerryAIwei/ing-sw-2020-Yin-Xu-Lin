package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.GodPower;
import it.polimi.ingsw.xyl.model.Player;

import java.util.EnumSet;
import java.util.Vector;

/*
 * @author Lin Xin
 */

public class Athena extends Cosplayer{
    boolean lastMoveUp = false;
    public Athena(Player player) {
        super(player);
        super.godPower = GodPower.ATHENA;
    }

    /**
     * Athena's power: if one of your workers moved up on
     * your last turn, opponent workers cannot move up this turn.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction){
        super.move(worker, direction);
        Vector<Direction> availableMoves = super.getAvailableMoves(worker);
        int opponentPlayerId = 0;
        if(availableMoves.contains(direction)){
            player.getCurrentGameBoard().getPlayers().get(opponentPlayerId).
                    getCurrentGameBoard().getIslandBoard().setNoMoveUp(true);
            // how to set back to default: false after a move?
        }
    }

    public void setOpponentNoMoveUp(int worker, Direction direction){

    }
}
