package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import static it.polimi.ingsw.xyl.model.GodPower.HEPHAESTUS;
import static it.polimi.ingsw.xyl.model.Level.DOME;

/**
 * @author Lin Xin
 */

public class Hephaestus extends Cosplayer {
    private boolean firstBuild = true;
    private Direction firstBuildDirection = null;

    public Hephaestus(Player player) {
        super(player);
        super.godPower = HEPHAESTUS;
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
        try{
            GameBoard currentGameBoard = this.getPlayer().getCurrentGameBoard();
            IslandBoard currentIslandBoard = currentGameBoard.getIslandBoard();
            int targetPositionX = this.getPlayer().getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            int targetPositionY = this.getPlayer().getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            int targetOccupiedBy = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].isOccupiedBy();
            boolean noDome = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].getLevel() != DOME;

            if (firstBuild && targetOccupiedBy == -1 && noDome){
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                this.firstBuildDirection= direction;
                this.firstBuild = false;
                currentGameBoard.toNextPlayer();
            }

            if (direction == firstBuildDirection){
                System.out.println("Chosen worker can't build at target space!");
            } else if (targetOccupiedBy == -1 && noDome) {
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
            } else {
                System.out.println("Chosen worker can't build at target space!");
            }
        }
        catch (Exception e){
            System.out.println("Array out of bounds");
            throw e;
        }
















        if(firstBuild){
            // do something

            this.firstBuildDirection= direction;
            this.firstBuild = false;
        }else{
            if(direction == firstBuildDirection){

            }else{

            }
        }
    }
}
