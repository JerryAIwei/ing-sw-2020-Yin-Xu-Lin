package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

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
                int targetOccupiedBy = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].isOccupiedBy();
                boolean noDome = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].getLevel() != Level.DOME;

                if (nextAction == Action.BUILD) {
                    if (targetOccupiedBy == -1 && noDome) {
                        currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                        buildWorker = worker;
                        firstBuildDirection = direction;
                        nextAction = Action.BUILDOREND;
                    } else {
                        System.out.println("Chosen worker can't build at target space!");
                    }
                } else if (buildWorker == worker && firstBuildDirection != direction && targetOccupiedBy == -1 && noDome) {
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
}
