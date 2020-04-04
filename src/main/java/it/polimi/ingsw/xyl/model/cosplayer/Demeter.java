package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import static it.polimi.ingsw.xyl.model.GodPower.DEMETER;
import static it.polimi.ingsw.xyl.model.Level.DOME;

/**
 * @author Lin Xin
 */

public class Demeter extends Cosplayer {
    private boolean firstBuild = true;
    private int[] firstBuildPositions = new int[2];
    private Direction firstBuildDirection = null;

    public Demeter(Player player) {
        super(player);
        super.godPower = DEMETER;
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
        try {
            GameBoard currentGameBoard = this.getPlayer().getCurrentGameBoard();
            IslandBoard currentIslandBoard = currentGameBoard.getIslandBoard();
            int targetPositionX = this.getPlayer().getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            int targetPositionY = this.getPlayer().getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            int targetOccupiedBy = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].isOccupiedBy();
            boolean noDome = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].getLevel() != DOME;

            if (firstBuild) {
                if (targetOccupiedBy == -1 && noDome) {
                    currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                    this.firstBuildDirection = direction;
                    this.firstBuild = false;
                    currentGameBoard.toNextPlayer();
                } else {
                    System.out.println("Chosen worker can't build at target space!");
                }
            } else if (this.firstBuildDirection != direction && targetOccupiedBy == -1 && noDome) {
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                this.firstBuild = true;
            } else {
                System.out.println("Chosen worker can't build at target space!");
            }
        }
        catch (Exception e){
            System.out.println("Array out of bounds");
            throw e;
        }
    }
}
