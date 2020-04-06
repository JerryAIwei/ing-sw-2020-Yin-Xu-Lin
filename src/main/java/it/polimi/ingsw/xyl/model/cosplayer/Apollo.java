package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;
import sun.jvm.hotspot.memory.SpaceClosure;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Vector;

import static it.polimi.ingsw.xyl.model.GodPower.APOLLO;

/**
 * @author Lin Xin
 */


public class Apollo extends Cosplayer {

    public Apollo(Player player) {
        super(player);
        super.godPower = APOLLO;
    }

    /**
     * Apollo's move: worker may move into an opponent worker's space
     * by forcing their worker to the space to the space yours just vacated.
     *
     * @param worker    '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction) {
        IslandBoard currentIslandBoard = this.getPlayer().getCurrentGameBoard().getIslandBoard();
        int originalPositionX = this.getPlayer().getWorkers()[worker].getPositionX();
        int originalPositionY = this.getPlayer().getWorkers()[worker].getPositionY();
        int targetOccupiedBy = 0;
        Vector<Direction> apolloAvailableMoves = getApolloAvailableMoves(worker);

        if (apolloAvailableMoves.contains(direction)){
            targetOccupiedBy = currentIslandBoard.getSpaces()
                    [originalPositionX + direction.toMarginalPosition()[0]]
                    [originalPositionX + direction.toMarginalPosition()[1]].isOccupiedBy();
        }

        if (targetOccupiedBy != -1 && targetOccupiedBy != 0) {
            int opponentWorkerId = targetOccupiedBy % 10;
            int opponentPlayerId = targetOccupiedBy / 10;
            this.getPlayer().getCurrentGameBoard().getIslandBoard().getSpaces()
                    [originalPositionX][originalPositionY].setOccupiedBy(-1);
            assert direction.toOpposite() != null;
            Space opponentCurrentSpace = currentIslandBoard.getSpaces()
                    [originalPositionX + direction.toMarginalPosition()[0]]
                    [originalPositionY + direction.toMarginalPosition()[1]];
            this.getPlayer().getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                    [opponentWorkerId].setFromLevel(opponentCurrentSpace.getLevel().toInt());
            opponentCurrentSpace.setOccupiedBy(-1);
            this.getPlayer().getCurrentGameBoard().getPlayers().get(opponentPlayerId).getWorkers()
                    [opponentWorkerId].setPosition(originalPositionX, originalPositionY);
            checkWin();
        }
        super.move(worker, direction);
        if (targetOccupiedBy != -1 && targetOccupiedBy != 0) {
            this.getPlayer().getCurrentGameBoard().getIslandBoard().getSpaces()
                    [originalPositionX][originalPositionY].setOccupiedBy(targetOccupiedBy);
            System.out.println("Apollo's move");
        }
    }


    public Vector<Direction> getApolloAvailableMoves(int worker) {
        int originalPositionX = this.getPlayer().getWorkers()[worker].getPositionX();
        int originalPositionY = this.getPlayer().getWorkers()[worker].getPositionY();
        EnumSet<Direction> all = EnumSet.allOf(Direction.class);
        Vector<Direction> apolloAvailableMoves = new Vector<>(all);

        if (originalPositionX == 0) {
            apolloAvailableMoves.remove(Direction.LEFT);
            apolloAvailableMoves.remove(Direction.UPLEFT);
            apolloAvailableMoves.remove(Direction.DOWNLEFT);
        }
        if (originalPositionX == 4) {
            apolloAvailableMoves.remove(Direction.RIGHT);
            apolloAvailableMoves.remove(Direction.UPRIGHT);
            apolloAvailableMoves.remove(Direction.DOWNRIGHT);
        }
        if (originalPositionX == 0) {
            apolloAvailableMoves.remove(Direction.DOWN);
            apolloAvailableMoves.remove(Direction.DOWNLEFT);
            apolloAvailableMoves.remove(Direction.DOWNRIGHT);
        }
        if (originalPositionX == 4) {
            apolloAvailableMoves.remove(Direction.UP);
            apolloAvailableMoves.remove(Direction.UPLEFT);
            apolloAvailableMoves.remove(Direction.UPRIGHT);
        }
        Iterator<Direction> iterator = apolloAvailableMoves.iterator();
        while (iterator.hasNext()) {
            Direction a = iterator.next();
            Space targetSpace = this.getPlayer().getCurrentGameBoard().getIslandBoard().getSpaces()
                    [originalPositionX + a.toMarginalPosition()[0]]
                    [originalPositionY+ a.toMarginalPosition()[1]];
            // remove occupied by a dome
            if (targetSpace.getLevel() == Level.DOME) {
                iterator.remove();
                continue;
            }

            Space currentSpace =
                    this.getPlayer().getCurrentGameBoard().getIslandBoard().getSpaces()[originalPositionX][originalPositionY];

            int relativeLevel = targetSpace.getLevel().toInt() - currentSpace.getLevel().toInt();
            boolean noMoveUp = this.getPlayer().getCurrentGameBoard().getIslandBoard().isNoMoveUp();
            // remove relative level not allowed
            if (relativeLevel > 1 || (noMoveUp && relativeLevel == 1))
                iterator.remove();
        }
        return apolloAvailableMoves;
    }

}
