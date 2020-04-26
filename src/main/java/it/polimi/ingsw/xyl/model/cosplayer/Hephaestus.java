package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;


/**
 * @author Lin Xin
 */

public class Hephaestus extends Cosplayer {
    private int buildWorker = 0;
    private Direction firstBuildDirection = null;

    public Hephaestus(Player player) {
        super(player);
        super.godPower = GodPower.HEPHAESTUS;
    }

    /**
     * Hephaestus' build: worker may build one additional block(not dome)
     * on top of your first block.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    public void build(int worker, Direction direction, boolean buildDome){
        if (worker == workerInAction){
            try {
                GameBoard currentGameBoard = player.getCurrentGameBoard();
                IslandBoard currentIslandBoard = currentGameBoard.getIslandBoard();
                int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
                int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
                int targetOccupiedBy = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].isOccupiedBy();
                boolean noDome = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].getLevel() != Level.DOME;
                boolean noLevel3 =
                        currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].getLevel() != Level.LEVEL3;

                if (nextAction == Action.BUILD) {
                    if (targetOccupiedBy == -1 && noDome && noLevel3) {
                        currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                        firstBuildDirection = direction;
                        buildWorker = worker;
                        nextAction = Action.BUILDOREND;
                    } else if (targetOccupiedBy == -1 && noDome) {
                        currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                        firstBuildDirection = direction;
                        nextAction = Action.MOVE;
                        workerInAction = -1;
                        currentGameBoard.toNextPlayer();
                    } else {
                        System.out.println("Chosen worker can't build at target space!");
                    }
                }else if(buildWorker == worker && firstBuildDirection == direction && noLevel3){
                    currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                    currentGameBoard.toNextPlayer();
                    nextAction = Action.MOVE;
                    workerInAction = -1;
                }
                else{
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
