package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Vector;


/**
 * @author Lin Xin
 */


public class Minotaur extends Cosplayer {

    public Minotaur(Player player) {
        super(player);
        super.godPower = GodPower.MINOTAUR;
    }

    /**
     * Minotaur's move: worker may move into an opponent worker's
     * space, if their worker can be forced one space straight backwards
     * to an unoccupied space at ANY level.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction) {
        if (getAvailableMoves(worker).contains(direction)) {
            IslandBoard currentIslandBoard = player.getCurrentGameBoard().getIslandBoard();
            int originalPositionX = player.getWorkers()[worker].getPositionX();
            int originalPositionY = player.getWorkers()[worker].getPositionY();
            int targetOccupiedBy = currentIslandBoard.getSpaces()
                    [originalPositionX + direction.toMarginalPosition()[0]]
                    [originalPositionY + direction.toMarginalPosition()[1]].isOccupiedBy();
            // Since getAvailableMoves deleted not allowed cases of using power,
            // if target space is occupied, it is occupied by an opponent worker which can move backwards
            // So, we force move that worker backwards and then perform a Civilian move for Minotaur.
            if (targetOccupiedBy != -1) {
                int backwardsPositionX = originalPositionX + 2 * direction.toMarginalPosition()[0];
                int backwardsPositionY = originalPositionY + 2 * direction.toMarginalPosition()[1];
                int opponentWorkerId = targetOccupiedBy % 10;
                int opponentPlayerId = targetOccupiedBy / 10;
                Space opponentCurrentSpace = currentIslandBoard.getSpaces()
                        [originalPositionX + direction.toMarginalPosition()[0]]
                        [originalPositionY + direction.toMarginalPosition()[1]];
                player.getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                        [opponentWorkerId].setFromLevel(opponentCurrentSpace.getLevel().toInt());
                player.getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                        [opponentWorkerId].setForced();
                opponentCurrentSpace.setOccupiedBy(-1);
                player.getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                        [opponentWorkerId].setPosition(backwardsPositionX, backwardsPositionY);
                currentIslandBoard.getSpaces()[backwardsPositionX][backwardsPositionY].setOccupiedBy(opponentPlayerId * 10 + opponentWorkerId);
            }
            // if target space is not occupied, it's just a Civilian move.
            super.move(worker, direction); // super.move will change nextAction and checkWin.
        }else
            System.out.println("Your move is not available!");
    }

    /**
     * get all available move directions of a Minotaur's worker
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @return all available direction of the worker.
     */
    public Vector<Direction> getAvailableMoves(int worker) {
        int originalPositionX = player.getWorkers()[worker].getPositionX();
        int originalPositionY = player.getWorkers()[worker].getPositionY();
        EnumSet<Direction> all = EnumSet.allOf(Direction.class);
        Vector<Direction> minotaurAvailableMoves = new Vector<>(all);

        if (originalPositionX == 0) {
            minotaurAvailableMoves.remove(Direction.LEFT);
            minotaurAvailableMoves.remove(Direction.UPLEFT);
            minotaurAvailableMoves.remove(Direction.DOWNLEFT);
        }
        if (originalPositionX == 4) {
            minotaurAvailableMoves.remove(Direction.RIGHT);
            minotaurAvailableMoves.remove(Direction.UPRIGHT);
            minotaurAvailableMoves.remove(Direction.DOWNRIGHT);
        }
        if (originalPositionY == 0) {
            minotaurAvailableMoves.remove(Direction.DOWN);
            minotaurAvailableMoves.remove(Direction.DOWNLEFT);
            minotaurAvailableMoves.remove(Direction.DOWNRIGHT);
        }
        if (originalPositionY == 4) {
            minotaurAvailableMoves.remove(Direction.UP);
            minotaurAvailableMoves.remove(Direction.UPLEFT);
            minotaurAvailableMoves.remove(Direction.UPRIGHT);
        }
        Iterator<Direction> iterator = minotaurAvailableMoves.iterator();
        while (iterator.hasNext()) {
            Direction a = iterator.next();
            Space targetSpace = player.getCurrentGameBoard().getIslandBoard().getSpaces()
                    [originalPositionX + a.toMarginalPosition()[0]]
                    [originalPositionY+ a.toMarginalPosition()[1]];
            // remove occupied by a dome or Minotaur's own worker
            if (targetSpace.getLevel() == Level.DOME ||
                    targetSpace.isOccupiedBy() == (player.getPlayerId() * 10 + 1) ||
                    targetSpace.isOccupiedBy() == (player.getPlayerId() * 10)) {
                iterator.remove();
                continue;
            }
            // if targetSpace is occupied by opponent's worker, check power is applicable.
            if(targetSpace.isOccupiedBy() != -1
                    && targetSpace.isOccupiedBy() != (player.getPlayerId() * 10 + 1)
                    &&targetSpace.isOccupiedBy() != (player.getPlayerId() * 10)){
               int backwardsPositionX = originalPositionX + 2 * a.toMarginalPosition()[0];
               int backwardsPositionY = originalPositionY+ 2 * a.toMarginalPosition()[1];
               if(backwardsPositionX < 0 || backwardsPositionY < 0 ){
                   // remove out of island board if apply power
                   iterator.remove();
                   continue;
               }else{
                   int backwardsOccupiedBy = player.getCurrentGameBoard().getIslandBoard().
                                   getSpaces()[backwardsPositionX][backwardsPositionY].isOccupiedBy();
                   boolean backwardsDome = player.getCurrentGameBoard().getIslandBoard().
                           getSpaces()[backwardsPositionX][backwardsPositionY].getLevel() == Level.DOME;
                   if(backwardsOccupiedBy != -1 || backwardsDome) {
                       // if backward space is occupied by a dome or another worker, power cannot apply
                       iterator.remove();
                       continue;
                   }
               }
            }

            Space currentSpace =
                    player.getCurrentGameBoard().getIslandBoard().getSpaces()[originalPositionX][originalPositionY];

            int relativeLevel = targetSpace.getLevel().toInt() - currentSpace.getLevel().toInt();
            boolean noMoveUp = player.getCurrentGameBoard().getIslandBoard().isNoLevelUp();
            // remove relative level not allowed
            if (relativeLevel > 1 || (noMoveUp && relativeLevel == 1))
                iterator.remove();
        }
        return minotaurAvailableMoves;
    }
}
