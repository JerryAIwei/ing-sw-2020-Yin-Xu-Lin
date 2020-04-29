package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import java.util.ArrayList;

/**
 * @author Lin Xin
 */

public class Demeter extends Cosplayer {
    private int buildWorker = 0;
    private Direction firstBuildDirection = null;

    public Demeter(Player player) {
        super(player);
        super.godPower = GodPower.DEMETER;
    }
    /**
     * Demeter's build: worker may build one additional time,
     * but not on the same space.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    public void build(int worker, Direction direction, boolean buildDome){
        if (worker == workerInAction){
            try {
                GameBoard currentGameBoard = getPlayer().getCurrentGameBoard();
                IslandBoard currentIslandBoard = currentGameBoard.getIslandBoard();
                int targetPositionX = getPlayer().getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
                int targetPositionY = getPlayer().getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];

                if (nextAction == Action.BUILD && getAvailableBuilds(worker).contains(direction)) {
                    currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                    buildWorker = worker;
                    firstBuildDirection = direction;
                    nextAction = Action.BUILDOREND;
                } else if (buildWorker == worker && getAvailableBuilds(worker).contains(direction)) {
                    currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                    currentGameBoard.toNextPlayer();
                    nextAction = Action.MOVE;
                    workerInAction = -1;
                } else {
                    System.out.println("Chosen worker can't build at target space!");
                }
            }
            catch (Exception e){
                System.out.println("Array out of bounds");
                throw e;
            }
        }else{
            System.out.println("You shouldn't have different workers to operate.");
            throw new RuntimeException("You shouldn't have different workers to operate.");
        }
    }


    public ArrayList<Direction> getAvailableBuilds(int worker) {
        ArrayList<Direction> availableBuilds = super.getAvailableBuilds(worker);
        if (nextAction == Action.BUILDOREND)
            availableBuilds.remove(firstBuildDirection);
        return availableBuilds;
    }
}
