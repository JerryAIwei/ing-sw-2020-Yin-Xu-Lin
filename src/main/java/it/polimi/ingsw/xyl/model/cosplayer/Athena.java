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
        int playerNumber = player.getCurrentGameBoard().getPlayerNumber();
        Player currentPlayer = player.getCurrentGameBoard().getCurrentPlayer();
        int nextPlayerId = (currentPlayer.getPlayerId() + 1) % playerNumber;
        if(super.getAvailableMoves(worker).contains(direction) && direction == Direction.UP){
            player.getCurrentGameBoard().getPlayers().get(nextPlayerId).
                        getCurrentGameBoard().getIslandBoard().setNoMoveUp(true);
            player.getCurrentGameBoard().getPlayers().get((nextPlayerId+1) % playerNumber).
                    getCurrentGameBoard().getIslandBoard().setNoMoveUp(true);
        }else{
            //player.getCurrentGameBoard().getIslandBoard().setNoMoveUp(false);
            player.getCurrentGameBoard().getPlayers().get(nextPlayerId).
                    getCurrentGameBoard().getIslandBoard().setNoMoveUp(false);
            player.getCurrentGameBoard().getPlayers().get((nextPlayerId+1) % playerNumber).
                    getCurrentGameBoard().getIslandBoard().setNoMoveUp(false);
        }
        super.move(worker, direction);
    }
}
