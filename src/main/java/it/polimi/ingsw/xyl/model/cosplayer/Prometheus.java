package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Vector;

import static it.polimi.ingsw.xyl.model.GodPower.PROMETHEUS;

/**
 * @author Lin Xin
 **/

public class Prometheus extends Cosplayer {
    private boolean firstActionBuild = false;
    private Action nextAction = Action.MOVEORBUILD;
    public Prometheus(Player player) {
        super(player);
        super.godPower = PROMETHEUS;
    }

    /**
     * Prometheus' move and build:
     * If your worker does not move up(means to a higher level),
     * it may build both before and after moving.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction){
        if (nextAction == Action.MOVEORBUILD) {
            super.move(worker, direction);
            nextAction = Action.BUILD;
        } else if (nextAction == Action.MOVE) {
            if (getAvailableMoves(worker).contains(direction)) {
                super.move(worker, direction);
                nextAction = Action.BUILD;
            } else {
                System.out.println("Your move is not available!");
            }
        }
    }

    /**
     * Prometheus' move and build:
     * If your worker does not move up(means to a higher level),
     * it may build both before and after moving.
     *
     * @param worker 'A' or 'B' represent two works of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    public void build(int worker, Direction direction, boolean buildDome){
        if(nextAction == Action.MOVEORBUILD){
            super.build(worker, direction, buildDome);
            firstActionBuild = true;
            nextAction = Action.MOVE;
        }else if(nextAction == Action.BUILD){
            super.build(worker, direction, buildDome);
            if(firstActionBuild)
                firstActionBuild = false;
        }
    }

    public Vector<Direction> getAvailableMoves(int worker){
        int x = player.getWorkers()[worker].getPositionX();
        int y = player.getWorkers()[worker].getPositionY();
        EnumSet<Direction> all = EnumSet.allOf(Direction.class);
        Vector<Direction> availableMoves = new Vector<>(all);
        // remove out of boundary
        if (x == 0) {
            availableMoves.remove(Direction.LEFT);
            availableMoves.remove(Direction.UPLEFT);
            availableMoves.remove(Direction.DOWNLEFT);
        }
        if (x == 4) {
            availableMoves.remove(Direction.RIGHT);
            availableMoves.remove(Direction.UPRIGHT);
            availableMoves.remove(Direction.DOWNRIGHT);
        }
        if (y == 0) {
            availableMoves.remove(Direction.DOWN);
            availableMoves.remove(Direction.DOWNLEFT);
            availableMoves.remove(Direction.DOWNRIGHT);
        }
        if (y == 4) {
            availableMoves.remove(Direction.UP);
            availableMoves.remove(Direction.UPLEFT);
            availableMoves.remove(Direction.UPRIGHT);
        }
        Iterator<Direction> iterator = availableMoves.iterator();
        while (iterator.hasNext()) {
            Direction a = iterator.next();
            Space targetSpace = player.getCurrentGameBoard().getIslandBoard().getSpaces()
                    [x + a.toMarginalPosition()[0]]
                    [y + a.toMarginalPosition()[1]];
            // remove occupied by another worker or by a dome
            if (targetSpace.isOccupiedBy() != -1 || targetSpace.getLevel() == Level.DOME) {
                iterator.remove();
                continue;
            }

            Space currentSpace =
                    player.getCurrentGameBoard().getIslandBoard().getSpaces()[player.getWorkers()[worker]
                            .getPositionX()][player.getWorkers()[worker].getPositionY()];

            int relativeLevel = targetSpace.getLevel().toInt() - currentSpace.getLevel().toInt();

            //when use power, worker cannot move up
            if (nextAction == Action.MOVE) {
                player.getCurrentGameBoard().getIslandBoard().setNoMoveUp(true);
            }
            boolean noMoveUp = player.getCurrentGameBoard().getIslandBoard().isNoMoveUp();

            if (relativeLevel > 1 || (noMoveUp && relativeLevel == 1))
                iterator.remove();
            player.getCurrentGameBoard().getIslandBoard().setNoMoveUp(false);
            }
        return availableMoves;
    }
}
