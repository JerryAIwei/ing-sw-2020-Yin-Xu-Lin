package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import java.util.EnumSet;
import java.util.Vector;

import static it.polimi.ingsw.xyl.model.GodPower.MINOTAUR;

/**
 * @author Lin Xin
 */


public class Minotaur extends Cosplayer {

    public Minotaur(Player player) {
        super(player);
        super.godPower = MINOTAUR;
    }

    /**
     * Minotaur's move: worker may move into an opponent worker's
     * space, if their worker can be forced one space straight backwards
     * to an unoccupied space at ANY level.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction){
        IslandBoard currentIslandBoard = this.getPlayer().getCurrentGameBoard().getIslandBoard();
        int originalPositionX = this.getPlayer().getWorkers()[worker].getPositionX();
        int originalPositionY = this.getPlayer().getWorkers()[worker].getPositionY();
        int targetOccupiedBy = currentIslandBoard.getSpaces()
                [originalPositionX + direction.toMarginalPosition()[0]]
                [originalPositionY + direction.toMarginalPosition()[1]].isOccupiedBy();
        int backwardsPositionX = originalPositionX + 2 * direction.toMarginalPosition()[0];
        int backwardsPositionY = originalPositionY + 2 * direction.toMarginalPosition()[1];
        int backwardsOccupiedBy = currentIslandBoard.getSpaces()[backwardsPositionX][backwardsPositionY].isOccupiedBy();
        boolean backwardsNoDome = currentIslandBoard.getSpaces()[backwardsPositionX][backwardsPositionY].getLevel() != Level.DOME;
        EnumSet<Direction> all = EnumSet.allOf(Direction.class);
        Vector<Direction> minotaurAvailableMoves = new Vector<>(all);

        if (originalPositionX == 1 || originalPositionX == 0) {
            minotaurAvailableMoves.remove(Direction.LEFT);
            minotaurAvailableMoves.remove(Direction.UPLEFT);
            minotaurAvailableMoves.remove(Direction.DOWNLEFT);
        }
        if (originalPositionX == 3 || originalPositionX == 4) {
            minotaurAvailableMoves.remove(Direction.RIGHT);
            minotaurAvailableMoves.remove(Direction.UPRIGHT);
            minotaurAvailableMoves.remove(Direction.DOWNRIGHT);
        }
        if (originalPositionX == 1 || originalPositionY == 0) {
            minotaurAvailableMoves.remove(Direction.DOWN);
            minotaurAvailableMoves.remove(Direction.DOWNLEFT);
            minotaurAvailableMoves.remove(Direction.DOWNRIGHT);
        }
        if (originalPositionX == 3 || originalPositionY == 4) {
            minotaurAvailableMoves.remove(Direction.UP);
            minotaurAvailableMoves.remove(Direction.UPLEFT);
            minotaurAvailableMoves.remove(Direction.UPRIGHT);
        }

        if(targetOccupiedBy != -1 && backwardsOccupiedBy == -1 && backwardsNoDome && minotaurAvailableMoves.contains(direction)) {
            int opponentWorkerId = targetOccupiedBy % 10;
            int opponentPlayerId = targetOccupiedBy / 10;
            Space opponentCurrentSpace = currentIslandBoard.getSpaces()
                    [originalPositionX + direction.toMarginalPosition()[0]]
                    [originalPositionY + direction.toMarginalPosition()[1]];
            Vector<Direction> availableMoves = getAvailableMoves(worker);

            super.move(worker, direction);
            availableMoves.addElement(direction);
            this.getPlayer().getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                    [opponentWorkerId].setFromLevel(opponentCurrentSpace.getLevel().toInt());
            opponentCurrentSpace.setOccupiedBy(-1);
            this.getPlayer().getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                    [opponentWorkerId].setPosition(backwardsPositionX, backwardsPositionY);
            currentIslandBoard.getSpaces()[backwardsPositionX][backwardsPositionY].setOccupiedBy(opponentPlayerId * 10 + worker);
            checkWin();
            System.out.println("Minotaur's move");
        }
        super.move(worker, direction);
    }
}
