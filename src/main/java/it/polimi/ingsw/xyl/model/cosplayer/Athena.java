package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

/**
 * This class represents the role of the Player: Athena
 * Athena's power: if one of your workers moved up on
 * your last turn, opponent workers cannot move up this turn.
 *
 * @author Lin Xin
 * @author Shaoxun
 */

public class Athena extends Cosplayer {

    public Athena(Player player) {
        super(player);
        super.godPower = GodPower.ATHENA;
    }

    public void prepare(){
        player.getCurrentGameBoard().getIslandBoard().setNoLevelUp(false);
        workerInAction = -1;
    }

    /**
     * Athena's power: if one of your workers moved up on
     * your last turn, opponent workers cannot move up this turn.
     *
     * @param worker    '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction) {
        if (getAvailableMoves(worker).contains(direction)) {
            int currentX = player.getWorkers()[worker].getPositionX();
            int currentY = player.getWorkers()[worker].getPositionY();
            IslandBoard currentIslandBoard = player.getCurrentGameBoard().getIslandBoard();
            Space currentSpace =
                    currentIslandBoard.getSpaces()[currentX][currentY];
            int targetX = currentX + direction.toMarginalPosition()[0];
            int targetY = currentY + direction.toMarginalPosition()[1];
            Space targetSpace = currentIslandBoard.getSpaces()[targetX][targetY];
            super.move(worker, direction);  // nextAction updated
            // if one of Athena's workers leveled up
            if (targetSpace.getLevel().toInt() - currentSpace.getLevel().toInt() == 1) {
                // set current island board noLevelUp
                currentIslandBoard.setNoLevelUp(true);
            }
        } else {
            System.out.println("Your move is not available!");
        }
    }
}
