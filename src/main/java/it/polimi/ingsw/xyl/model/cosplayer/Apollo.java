package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * @author Lin Xin
 */


public class Apollo extends Cosplayer {

    public Apollo(Player player) {
        super(player);
        super.godPower = GodPower.APOLLO;
    }

    /**
     * Apollo's move: worker may move into an opponent worker's space
     * by forcing their worker to the space to the space yours just vacated.
     *
     * @param worker    '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    @Override
    public void move(int worker, Direction direction) {
        IslandBoard currentIslandBoard = player.getCurrentGameBoard().getIslandBoard();
        int originalPositionX = player.getWorkers()[worker].getPositionX();
        int originalPositionY = player.getWorkers()[worker].getPositionY();
        int targetOccupiedBy = currentIslandBoard.getSpaces()
                [originalPositionX + direction.toMarginalPosition()[0]]
                [originalPositionY + direction.toMarginalPosition()[1]].isOccupiedBy(); ;
        ArrayList<Direction> availableMoves = getAvailableMoves(worker);
        if (targetOccupiedBy != -1 && availableMoves.contains(direction)) {
            int opponentWorkerId = targetOccupiedBy % 10;
            int opponentPlayerId = targetOccupiedBy / 10;
            player.getCurrentGameBoard().getIslandBoard().getSpaces()
                    [originalPositionX][originalPositionY].setOccupiedBy(-1);
            Space opponentCurrentSpace = currentIslandBoard.getSpaces()
                    [originalPositionX + direction.toMarginalPosition()[0]]
                    [originalPositionY + direction.toMarginalPosition()[1]];
            player.getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                    [opponentWorkerId].setFromLevel(opponentCurrentSpace.getLevel().toInt());
            player.getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                    [opponentWorkerId].setForced();
            opponentCurrentSpace.setOccupiedBy(-1);
            player.getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                    [opponentWorkerId].setPosition(originalPositionX, originalPositionY);
        }
        super.move(worker, direction); // super.move will change nextAction & workerInAction and checkWin.
        if (targetOccupiedBy != -1 && availableMoves.contains(direction)) {
            player.getCurrentGameBoard().getIslandBoard().getSpaces()
                    [originalPositionX][originalPositionY].setOccupiedBy(targetOccupiedBy);
        }
        checkWin();  // I don't know whehter checkWin() twice will have any side effect.
    }


    /**
     * get all available move directions of an Apollo's worker
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @return all available direction of the worker.
     */
    @Override
    public ArrayList<Direction> getAvailableMoves(int worker) {
        int x = player.getWorkers()[worker].getPositionX();
        int y = player.getWorkers()[worker].getPositionY();
        ArrayList<Direction> apolloAvailableMoves = getAvailable(x, y);
        Iterator<Direction> iterator = apolloAvailableMoves.iterator();
        Direction a;
        Space targetSpace;
        Space currentSpace;
        while (iterator.hasNext()) {
            a = iterator.next();
            targetSpace = player.getCurrentGameBoard().getIslandBoard().getSpaces()
                    [x + a.toMarginalPosition()[0]]
                    [y+ a.toMarginalPosition()[1]];
            // remove occupied by his own worker
            if (targetSpace.isOccupiedBy() == (player.getPlayerId() * 10 + 1) ||
                    targetSpace.isOccupiedBy() == (player.getPlayerId() * 10)) {
                iterator.remove();
                continue;
            }
            currentSpace =
                    player.getCurrentGameBoard().getIslandBoard().getSpaces()[x][y];

            int relativeLevel = targetSpace.getLevel().toInt() - currentSpace.getLevel().toInt();
            boolean noMoveUp = player.getCurrentGameBoard().getIslandBoard().isNoLevelUp();
            // remove relative level not allowed
            if (relativeLevel > 1 || (noMoveUp && relativeLevel == 1))
                iterator.remove();
        }
        return apolloAvailableMoves;
    }

}
