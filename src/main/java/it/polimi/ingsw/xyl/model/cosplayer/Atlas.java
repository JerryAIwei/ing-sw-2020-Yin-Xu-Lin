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
        GameBoard CurrentGameBoard = this.getPlayer().getCurrentGameBoard();
        IslandBoard CurrentIslandBoard = CurrentGameBoard.getIslandBoard();
        int targetPositionX = this.getPlayer().getWorkers()[worker].getPositionX();
        int targetPositionY = this.getPlayer().getWorkers()[worker].getPositionY();
        int targetOccupiedBy = CurrentIslandBoard.getSpaces()[targetPositionX][targetPositionY].isOccupiedBy();
        boolean noDome = CurrentIslandBoard.getSpaces()[targetPositionX][targetPositionY].getLevel() != DOME;
        try {
            if (noDome && (targetOccupiedBy == -1)) {
                if (buildDome) {
                    CurrentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setLevel(DOME);
                } else {
                    CurrentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                }
                CurrentGameBoard.toNextPlayer();
            } else {
                System.out.println("Chosen worker can't build dome at target space");
            }
        } catch (Exception e) {
            System.out.println("Array out of bounds");
            throw e;
        }
    }
}