package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;


/**
 * @author Lin Xin
 */

public class Atlas extends Cosplayer {

    public Atlas(Player player) {
        super(player);
        super.godPower = GodPower.ATLAS;
    }

    /**
     * Atlas' build: worker may build a dome at any level.
     *
     * @param worker    '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    @Override
    public void build(int worker, Direction direction, boolean buildDome) {
        if (worker == workerInAction && getAvailableBuilds(worker).contains(direction)){
            GameBoard currentGameBoard = player.getCurrentGameBoard();
            IslandBoard currentIslandBoard = currentGameBoard.getIslandBoard();
            int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            if (buildDome) {
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setLevel(Level.DOME);
            } else {
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
            }
            // update nextAction and workerInAction
            nextAction = Action.MOVE;
            workerInAction = -1;
            currentGameBoard.toNextPlayer();
        }else{
            System.out.println("Your build is not available!");
            throw new RuntimeException("Build not available.");
        }
    }
}