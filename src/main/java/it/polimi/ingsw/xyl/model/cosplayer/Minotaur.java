package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;



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
    @Override
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
        }else {
            System.out.println("Your move is not available!");
            throw new RuntimeException("Move not available.");
        }
    }

    /**
     * get all available move directions of a Minotaur's worker
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @return all available direction of the worker.
     */
    @Override
    public ArrayList<Direction> getAvailableMoves(int worker) {
        int x = player.getWorkers()[worker].getPositionX();
        int y = player.getWorkers()[worker].getPositionY();
        ArrayList<Direction> minotaurAvailableMoves = getAvailable(x,y);
        Iterator<Direction> iterator = minotaurAvailableMoves.iterator();
        Direction a;
        Space targetSpace;
        Space currentSpace;
        while (iterator.hasNext()) {
            a = iterator.next();
            targetSpace = player.getCurrentGameBoard().getIslandBoard().getSpaces()
                    [x + a.toMarginalPosition()[0]]
                    [y+ a.toMarginalPosition()[1]];
            // remove occupied by a dome or Minotaur's own worker
            if (targetSpace.isOccupiedBy() == (player.getPlayerId() * 10 + 1) ||
                    targetSpace.isOccupiedBy() == (player.getPlayerId() * 10)) {
                iterator.remove();
                continue;
            }
            // if targetSpace is occupied by opponent's worker, check power is applicable.
            if(targetSpace.isOccupiedBy() != -1
                    && targetSpace.isOccupiedBy() != (player.getPlayerId() * 10 + 1)
                    &&targetSpace.isOccupiedBy() != (player.getPlayerId() * 10)){
               int backwardsPositionX = x + 2 * a.toMarginalPosition()[0];
               int backwardsPositionY = y+ 2 * a.toMarginalPosition()[1];
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

            currentSpace =
                    player.getCurrentGameBoard().getIslandBoard().getSpaces()[x][y];

            int relativeLevel = targetSpace.getLevel().toInt() - currentSpace.getLevel().toInt();
            boolean noMoveUp = player.getCurrentGameBoard().getIslandBoard().isNoLevelUp();
            // remove relative level not allowed
            if (relativeLevel > 1 || (noMoveUp && relativeLevel == 1))
                iterator.remove();
        }
        return minotaurAvailableMoves;
    }
}
