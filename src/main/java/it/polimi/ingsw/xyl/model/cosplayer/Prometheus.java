package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents the role of the Player: Prometheus
 * Prometheus' move and build:
 * If your worker does not move up(means to a higher level),
 * it may build both before and after moving.
 * 
 * @author Lin Xin
 * @author Shaoxun
 **/

public class Prometheus extends Cosplayer {

    public Prometheus(Player player) {
        super(player);
        super.godPower = GodPower.PROMETHEUS;
        nextAction = Action.MOVEORBUILD;
    }

    @Override
    public void prepare(){
        nextAction = Action.MOVEORBUILD;
        workerInAction = -1;
    }

    /**
     * Prometheus' move and build:
     * If your worker does not move up(means to a higher level),
     * it may build both before and after moving.
     *
     * @param worker    '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    @Override
    public void move(int worker, Direction direction) {
        if (getAvailableMoves(worker).contains(direction)
                && (nextAction == Action.MOVEORBUILD
                || (nextAction == Action.MOVE && worker == workerInAction))) {
            super.move(worker, direction);
            //  nextAction = Action.BUILD; // super.move did
        }else {
            System.out.println("Your move is not available!");
            throw new RuntimeException("Move not available.");
        }
    }

    /**
     * Prometheus' move and build:
     * If your worker does not move up(means to a higher level),
     * it may build both before and after moving.
     *
     * @param worker    'A' or 'B' represent two works of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    @Override
    public void build(int worker, Direction direction, boolean buildDome) {
        if (getAvailableBuilds(worker).contains(direction)) {
            GameBoard currentGameBoard = player.getCurrentGameBoard();
            IslandBoard currentIslandBoard = currentGameBoard.getIslandBoard();
            int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            if (nextAction == Action.BUILD && worker == workerInAction) {
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                nextAction = Action.MOVEORBUILD;
                workerInAction = -1;
                currentGameBoard.toNextPlayer();
            } else if (nextAction == Action.MOVEORBUILD) {
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                workerInAction = worker;
                nextAction = Action.MOVE;
            } else {
                System.out.println("Your build is not available!");
                throw new RuntimeException("Build not available.");
            }
        }else{
            System.out.println("Your build is not available!");
            throw new RuntimeException("Build not available.");
        }
    }

    @Override
    public ArrayList<Direction> getAvailableMoves(int worker) {
        ArrayList<Direction> availableMoves = super.getAvailableMoves(worker);
        if (nextAction == Action.MOVE) {
            Iterator<Direction> iterator = availableMoves.iterator();
            Direction a;
            Space currentSpace;
            Space targetSpace;
            while (iterator.hasNext()) {
                a = iterator.next();
                currentSpace = player.getCurrentGameBoard().getIslandBoard().getSpaces()
                        [player.getWorkers()[worker].getPositionX()]
                        [player.getWorkers()[worker].getPositionY()];
                targetSpace = player.getCurrentGameBoard().getIslandBoard().getSpaces()
                        [player.getWorkers()[worker].getPositionX() + a.toMarginalPosition()[0]]
                        [player.getWorkers()[worker].getPositionY() + a.toMarginalPosition()[1]];
                int relativeLevel = targetSpace.getLevel().toInt() - currentSpace.getLevel().toInt();
                if (relativeLevel == 1)
                    iterator.remove();
            }
        }
        return availableMoves;
    }
}
