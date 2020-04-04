package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import static it.polimi.ingsw.xyl.model.GodPower.ATLAS;
import static it.polimi.ingsw.xyl.model.Level.DOME;

/**
 * @author Lin Xin
 */

public class Atlas extends Cosplayer {

    public Atlas(Player player) {
        super(player);
        super.godPower = ATLAS;
    }

    /**
     * Atlas' build: worker may build a dome at any level.
     *
     * @param worker    '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    public void build(int worker, Direction direction, boolean buildDome) {
        try {
            GameBoard currentGameBoard = this.getPlayer().getCurrentGameBoard();
            IslandBoard currentIslandBoard = currentGameBoard.getIslandBoard();
            int targetPositionX = this.getPlayer().getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            int targetPositionY = this.getPlayer().getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            int targetOccupiedBy = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].isOccupiedBy();
            boolean noDome = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].getLevel() != DOME;

            if (targetOccupiedBy == -1 && noDome) {
                if (buildDome) {
                    currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setLevel(DOME);
                } else {
                    currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                }
                currentGameBoard.toNextPlayer();
            } else {
                System.out.println("Chosen worker can't build dome at target space");
            }
        } catch (Exception e) {
            System.out.println("Array out of bounds");
            throw e;
        }
    }
}