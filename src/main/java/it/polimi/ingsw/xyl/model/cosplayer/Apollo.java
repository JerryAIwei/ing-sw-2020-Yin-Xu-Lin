package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;
import sun.jvm.hotspot.memory.SpaceClosure;

import java.util.Vector;

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
        IslandBoard currentIslandBoard = this.getPlayer().getCurrentGameBoard().getIslandBoard();
        int originalPositionX = this.getPlayer().getWorkers()[worker].getPositionX();
        int originalPositionY = this.getPlayer().getWorkers()[worker].getPositionY();
        int targetOccupiedBy = currentIslandBoard.getSpaces()
                [originalPositionX + direction.toMarginalPosition()[0]]
                [originalPositionY + direction.toMarginalPosition()[1]].isOccupiedBy();

        if(targetOccupiedBy != -1){
            int opponentWorkerId = targetOccupiedBy % 10;
            int opponentPlayerId = targetOccupiedBy / 10;
            this.getPlayer().getCurrentGameBoard().getIslandBoard().getSpaces()
                    [originalPositionX][originalPositionY].setOccupiedBy(-1);
            assert direction.toOpposite() != null;
            Space opponentCurrentSpace = currentIslandBoard.getSpaces()
                    [originalPositionX + direction.toMarginalPosition()[0]]
                    [originalPositionY + direction.toMarginalPosition()[1]];
            this.getPlayer().getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                    [opponentWorkerId].setFromLevel(opponentCurrentSpace.getLevel().toInt());
            opponentCurrentSpace.setOccupiedBy(-1);
            this.getPlayer().getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                    [opponentWorkerId].setPosition(originalPositionX,originalPositionY);
        }
        super.move(worker, direction);
        if(targetOccupiedBy != -1){
            this.getPlayer().getCurrentGameBoard().getIslandBoard().getSpaces()
                    [originalPositionX][originalPositionY].setOccupiedBy(targetOccupiedBy);
        }
        System.out.println("Apollo's move");
    }
}
