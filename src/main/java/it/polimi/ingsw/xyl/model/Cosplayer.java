package it.polimi.ingsw.xyl.model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;


/**
 * This class represents the role of the Player: God or Civilian
 *
 * @author Shaoxun
 */
public class Cosplayer {
    protected enum Action {
        MOVE, BUILD, MOVEORBUILD,BUILDOREND
    }
    protected Action nextAction = Action.MOVE;
    protected int workerInAction = -1;
    protected Player player;
    protected GodPower godPower = GodPower.ANONYMOUS;

    public String getNextAction() {
        return nextAction.toString();
    }

    public int getWorkerInAction(){
        return this.workerInAction;
    }

    public GodPower getGodPower() {
        return godPower;
    }

    public Cosplayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void prepare(){
        nextAction = Action.MOVE;
        workerInAction = -1;
    }

    /**
     * move method of cosplayer(Civilian Mod)
     *
     * @param worker    '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction) {
        if (getAvailableMoves(worker).contains(direction)) {
            int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            IslandBoard currentIslandBoard = player.getCurrentGameBoard().getIslandBoard();
            Space currentSpace =
                    currentIslandBoard.getSpaces()[player.getWorkers()[worker].getPositionX()][player.getWorkers()[worker].getPositionY()];
            player.getWorkers()[worker].setFromLevel(currentSpace.getLevel().toInt());
            // free the current occupied space
            currentSpace.setOccupiedBy(-1);
            // change the worker's position to target space
            player.getWorkers()[worker].setPosition(
                    player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0],
                    player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]
            );
            // reset forced status of worker
            player.getWorkers()[worker].resetForced();
            // occupy the target space by playerId
            currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setOccupiedBy(player.getPlayerId() * 10 + worker);
            // update nextAction and workerInAction
            nextAction = Action.BUILD;
            workerInAction = worker;
            // check win
            checkWin();
        } else {
            System.out.println("Your move is not available!");
            throw new RuntimeException("Move not available.");
        }
    }

    /**
     * build method of cosplayer(Civilian Mod)
     *
     * @param worker    '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    public void build(int worker, Direction direction, boolean buildDome) {
        if (worker == workerInAction && getAvailableBuilds(worker).contains(direction)) {
            GameBoard currentGameBoard = player.getCurrentGameBoard();
            IslandBoard currentIslandBoard = currentGameBoard.getIslandBoard();
            int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            // increase the level of the target space
            currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
            // change currentPlayer after finish building
            currentGameBoard.toNextPlayer();
            // update nextAction and workerInAction
            nextAction = Action.MOVE;
            workerInAction = -1;
        } else {
            System.out.println("Your build is not available!");
            throw new RuntimeException("Build not available.");
        }
    }

    /**
     * checkWin() is used after every move: if one of your workers moves
     * up on top of level 3 during your turn, you instantly win.
     *
     * checkWin() is used after each toNextPlayer(): if all of your workers
     * cannot move, you lose.
     * ( "You must always perform a move then build on your turn.
     * If you are unable to, you lose." The only condition in which one
     * is not able to build is "in a hole", that means,
     * all the eight spaces are occupied by another workers or domes,
     * but you can never get into a hole after an effective move.)
     */
    public void checkWin() {
        /* Civilian Mod */
        // If your worker is forced onto the third level,
        // you do not win the game. Moving from one third
        // level space to another also does not trigger a win.
        IslandBoard currentIslandBoard = player.getCurrentGameBoard().getIslandBoard();
        boolean win = (player.getWorkers()[0].fromLevel() != 3 &&
                !player.getWorkers()[0].isForced() &&
                currentIslandBoard.getSpaces()
                        [player.getWorkers()[0].getPositionX()]
                        [player.getWorkers()[0].getPositionY()].getLevel() == Level.LEVEL3)
                || (player.getWorkers()[1].fromLevel() != 3 &&
                !player.getWorkers()[1].isForced() &&
                currentIslandBoard.getSpaces()
                        [player.getWorkers()[1].getPositionX()]
                        [player.getWorkers()[1].getPositionY()].getLevel() == Level.LEVEL3);
        if (win) {
            player.setCurrentStatus(PlayerStatus.WIN);
            player.getCurrentGameBoard().setCurrentStatus(GameStatus.GAMEENDED);
        }
        boolean lose =
                player.getCosplayer().getAvailableMoves(0).isEmpty()
                        && player.getCosplayer().getAvailableMoves(1).isEmpty();
        if (lose) {
            player.setCurrentStatus(PlayerStatus.LOSE);
            int ax = player.getWorkers()[0].getPositionX();
            int ay = player.getWorkers()[0].getPositionY();
            int bx = player.getWorkers()[1].getPositionX();
            int by = player.getWorkers()[1].getPositionY();
            player.getCurrentGameBoard().getIslandBoard().getSpaces()[ax][ay].setOccupiedBy(-1);
            player.getCurrentGameBoard().getIslandBoard().getSpaces()[bx][by].setOccupiedBy(-1);
        }
    }

    protected ArrayList<Direction> getAvailable(int x, int y){
        EnumSet<Direction> all = EnumSet.allOf(Direction.class);
        ArrayList<Direction> available = new ArrayList<>(all);
        if (x == 0) {
            available.remove(Direction.LEFT);
            available.remove(Direction.UPLEFT);
            available.remove(Direction.DOWNLEFT);
        }
        if (x == 4) {
            available.remove(Direction.RIGHT);
            available.remove(Direction.UPRIGHT);
            available.remove(Direction.DOWNRIGHT);
        }
        if (y == 0) {
            available.remove(Direction.DOWN);
            available.remove(Direction.DOWNLEFT);
            available.remove(Direction.DOWNRIGHT);
        }
        if (y == 4) {
            available.remove(Direction.UP);
            available.remove(Direction.UPLEFT);
            available.remove(Direction.UPRIGHT);
        }
        Iterator<Direction> iterator = available.iterator();
        Direction a;
        Space targetSpace;
        while (iterator.hasNext()) {
            a = iterator.next();
            targetSpace = player.getCurrentGameBoard().getIslandBoard().getSpaces()
                    [x + a.toMarginalPosition()[0]]
                    [y + a.toMarginalPosition()[1]];
            // remove occupied by by a dome
            if (targetSpace.getLevel() == Level.DOME) {
                iterator.remove();
            }
        }
        return available;
    }

    /**
     * get all available move directions of a chosen worker
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @return all available direction of the worker.
     */
    public ArrayList<Direction> getAvailableMoves(int worker) {
        int x = player.getWorkers()[worker].getPositionX();
        int y = player.getWorkers()[worker].getPositionY();
        ArrayList<Direction> availableMoves = getAvailable(x,y);
        Iterator<Direction> iterator = availableMoves.iterator();
        Direction a;
        Space targetSpace;
        Space currentSpace;
        while (iterator.hasNext()) {
            a = iterator.next();
            targetSpace = player.getCurrentGameBoard().getIslandBoard().getSpaces()
                    [x + a.toMarginalPosition()[0]]
                    [y + a.toMarginalPosition()[1]];
            // remove occupied by another worker
            if (targetSpace.isOccupiedBy() != -1) {
                iterator.remove();
                continue;
            }
            currentSpace = player.getCurrentGameBoard().getIslandBoard().getSpaces()[x][y];
            int relativeLevel = targetSpace.getLevel().toInt() - currentSpace.getLevel().toInt();
            boolean noMoveUp = player.getCurrentGameBoard().getIslandBoard().isNoLevelUp();
            // remove relative level not allowed
            if (relativeLevel > 1 || (noMoveUp && relativeLevel == 1))
                iterator.remove();
        }
        return availableMoves;
    }


    /**
     * get all available build directions of a chosen worker
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @return all available direction of the worker.
     */
    public ArrayList<Direction> getAvailableBuilds(int worker) {
        int x = player.getWorkers()[worker].getPositionX();
        int y = player.getWorkers()[worker].getPositionY();
        ArrayList<Direction> availableBuilds = getAvailable(x,y);
        Iterator<Direction> iterator = availableBuilds.iterator();
        Direction a;
        Space targetSpace;
        while (iterator.hasNext()) {
            a = iterator.next();
            targetSpace = player.getCurrentGameBoard().getIslandBoard().getSpaces()
                    [x + a.toMarginalPosition()[0]]
                    [y + a.toMarginalPosition()[1]];
            // remove occupied by another worker
            if (targetSpace.isOccupiedBy() != -1) {
                iterator.remove();
            }
        }
        return availableBuilds;
    }

    // only for test
    public void only_for_test_setWorkerInAction(int worker){
        this.workerInAction = worker;
    }
}
