package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.IslandBoard;
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
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction){
        IslandBoard currentIslandBoard = this.getPlayer().getCurrentGameBoard().getIslandBoard();
        int originalPositionX = this.getPlayer().getWorkers()[worker].getPositionX();
        int originalPositionY = this.getPlayer().getWorkers()[worker].getPositionY();
        int targetOccupiedBy = currentIslandBoard.getSpaces()
                [originalPositionX + direction.toMarginalPosition()[0]]
                [originalPositionY + direction.toMarginalPosition()[1]].isOccupiedBy();
        int backwardsPositionX = originalPositionX + direction.toMarginalPosition()[0] * 2;
        int backwardsPositionY = originalPositionY + direction.toMarginalPosition()[1] * 2;
        int backwardsOccupiedBy = currentIslandBoard.getSpaces()
                [backwardsPositionX]
                [backwardsPositionY].isOccupiedBy();
        if(targetOccupiedBy != -1 && backwardsOccupiedBy == -1){

        }
    }
}
