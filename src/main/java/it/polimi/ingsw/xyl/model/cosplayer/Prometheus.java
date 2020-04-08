package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Vector;

import static it.polimi.ingsw.xyl.model.GodPower.PROMETHEUS;

public class Prometheus extends Cosplayer {
    private boolean climb =false;
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
        } else if (nextAction == Action.MOVE) {
            if (getAvailableMoves(worker).contains(direction)) {
                int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
                int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
                IslandBoard currentIslandBoard = player.getCurrentGameBoard().getIslandBoard();
                Space currentSpace =
                        currentIslandBoard.getSpaces()[player.getWorkers()[worker].getPositionX()][player.getWorkers()[worker].getPositionY()];
                player.getWorkers()[worker].setFromLevel(currentSpace.getLevel().toInt());
                currentSpace.setOccupiedBy(-1);
                player.getWorkers()[worker].setPosition(
                        player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0],
                        player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]
                );
                player.getWorkers()[worker].resetForced();
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setOccupiedBy(player.getPlayerId() * 10 + worker);
                nextAction = Action.BUILD;
                checkWin();
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
        }else if(nextAction == Action.BUILD){
            super.build(worker, direction, buildDome);
            if(firstActionBuild) {
                nextAction = Action.MOVEORBUILD;
                firstActionBuild = false;
            }
            /*
            if(!firstActionBuild) {
                super.build(worker, direction, buildDome);
            }else{
                super.build(worker, direction, buildDome);
                nextAction = Action.MOVEORBUILD;
            }*/
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
            boolean noMoveUp = player.getCurrentGameBoard().getIslandBoard().isNoMoveUp();

            //when use power, worker cannot move up
            if (nextAction == Action.MOVE && relativeLevel > 0){
                player.getCurrentGameBoard().getIslandBoard().setNoMoveUp(true);
            }
            // remove relative level not allowed
            // Worker may not move up more than one level
            if (relativeLevel > 1 || (noMoveUp && relativeLevel == 1))
                iterator.remove();
        }
        return availableMoves;
    }
}
