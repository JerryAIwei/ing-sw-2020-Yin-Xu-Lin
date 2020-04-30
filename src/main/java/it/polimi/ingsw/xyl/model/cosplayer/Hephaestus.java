package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import java.util.ArrayList;


/**
 * @author Lin Xin
 */

public class Hephaestus extends Cosplayer {
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
    @Override
    public void build(int worker, Direction direction, boolean buildDome){
        if (worker == workerInAction && getAvailableBuilds(worker).contains(direction)){
            GameBoard currentGameBoard = player.getCurrentGameBoard();
            IslandBoard currentIslandBoard = currentGameBoard.getIslandBoard();
            int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            boolean noLevel3 =
                    currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].getLevel() != Level.LEVEL3;

            if (nextAction == Action.BUILD  && noLevel3) {
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                firstBuildDirection = direction;
                workerInAction = worker;
                nextAction = Action.BUILDOREND;
            } else if (nextAction == Action.BUILD){
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                nextAction = Action.MOVE;
                workerInAction = -1;
                currentGameBoard.toNextPlayer();
            } else if (workerInAction == worker && firstBuildDirection == direction && noLevel3) {
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                currentGameBoard.toNextPlayer();
                nextAction = Action.MOVE;
                workerInAction = -1;
            } else {
                System.out.println("Chosen worker can't build at target space!");
                throw new RuntimeException("Build not available.");
            }
        }else{
            System.out.println("Your build is not available!");
            throw new RuntimeException("Build not available.");
        }
    }

    @Override
    public ArrayList<Direction> getAvailableBuilds(int worker) {
        if (nextAction == Action.BUILDOREND)
            return new ArrayList<>(){{add(firstBuildDirection);}};
        return super.getAvailableBuilds(worker);
    }
}
