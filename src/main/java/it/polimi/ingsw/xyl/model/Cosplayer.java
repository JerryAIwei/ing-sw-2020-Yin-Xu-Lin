package it.polimi.ingsw.xyl.model;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Vector;

import static it.polimi.ingsw.xyl.model.Level.DOME;

/**
 * This class represents the role of the Player: God or Civilian
 *
 * @author Shaoxun
 */
public class Cosplayer {
    private Player player;
    protected GodPower godPower = null;

    public GodPower getGodPower() {
        return godPower;
    }

    public Cosplayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    /**
     * move method of cosplayer(Civilian Mod)
     *
     * @param worker    '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction) {
        int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
        int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
        IslandBoard currentIslandBoard = player.getCurrentGameBoard().getIslandBoard();
        Vector<Direction> availableMoves = getAvailableMoves(worker);
        if (availableMoves.contains(direction)) {
            Space currentSpace =
                    currentIslandBoard.getSpaces()[player.getWorkers()[worker].getPositionX()][player.getWorkers()[worker].getPositionY()];
            player.getWorkers()[worker].setFromLevel(currentSpace.getLevel().toInt());
            // if move to lower level, set worker's fromLevel3 false
//            if (player.getWorkers()[worker].fromLevel() == 3) {
//                Space targetSpace = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY];
//                boolean moveToLowerLevel = targetSpace.getLevel().toInt() - currentSpace.getLevel().toInt() < 0;
//                if (moveToLowerLevel)
//                    player.getWorkers()[worker].setFromLevel(false);
//            }
            // free the current occupied space
            currentSpace.setOccupiedByPlayer(0);
            // chang the worker's position to target space
            player.getWorkers()[worker].setPosition(
                    player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0],
                    player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]
            );
            // occupy the target space by playerId
            currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setOccupiedByPlayer(player.getPlayerId());
            // check win
            checkWin();
        } else {
            System.out.println("Your move is not available!");
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
        try {
            GameBoard currentGameBoard = player.getCurrentGameBoard();
            IslandBoard currentIslandBoard = currentGameBoard.getIslandBoard();
            int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            int targetOccupiedBy = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].isOccupiedByPlayer();
            boolean noDome = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].getLevel() != DOME;
            // for Civilian Mod, player can build if the target space is free(not occupied by another player)
            // and there is no dome in the target space
            if (targetOccupiedBy == 0 && noDome) {
                // increase the level of the target space
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].increaseLevel();
                // change turnId after finish building

                /* turnId: 0, 1, 2 for playerNumber == 3 and 0, 1 for playerNumber == 2
                 *  current player is GameBoard.getPlayers[turnId]
                 *  at the beginning, the "Challenger" choose a "Start Player", re-sort the players ArrayList
                 *  for GodPowers which may move or build twice, set new turnID after everything done.
                 * */
                int playerNumber = currentGameBoard.getPlayerNumber();
                int nextTurnId = (currentGameBoard.getTurnId() + 1) % playerNumber;
                currentGameBoard.setTurnId(nextTurnId);
            } else {
                System.out.println("Chosen worker can't build in target space!");
            }
        } catch (Exception e) {
            System.out.println("Array out of bounds");
            throw e;
        }
    }

    /**
     * checkWin() is used after every move: if one of your workers moves
     * up on top of level 3 during your turn, you instantly win.
     *
     * checkWin() is used after each turnId change: if all of your workers
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
                currentIslandBoard.getSpaces()
                        [player.getWorkers()[0].getPositionX()]
                        [player.getWorkers()[0].getPositionY()].getLevel() == Level.LEVEL3)
                || (player.getWorkers()[1].fromLevel() != 3 &&
                currentIslandBoard.getSpaces()
                        [player.getWorkers()[1].getPositionX()]
                        [player.getWorkers()[1].getPositionY()].getLevel() == Level.LEVEL3);
        if (win) {
            player.setCurrentStatus(PlayerStatus.WIN);
            // notify();
        }
        boolean lose =
                player.getCosplayer().getAvailableMoves(0).size() == 0
                        && player.getCosplayer().getAvailableMoves(1).size() == 0;
        if (lose) {
            player.setCurrentStatus(PlayerStatus.LOSE);
        }
    }

    /**
     * get all available move directions of a chosen worker
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @return all available direction of the worker.
     */
    public Vector<Direction> getAvailableMoves(int worker) {
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
            if (targetSpace.isOccupiedByPlayer() != 0 || targetSpace.getLevel() == Level.DOME) {
                iterator.remove();
                continue;
            }

            Space currentSpace =
                    player.getCurrentGameBoard().getIslandBoard().getSpaces()[player.getWorkers()[worker]
                            .getPositionX()][player.getWorkers()[worker].getPositionY()];

            int relativeLevel = targetSpace.getLevel().toInt() - currentSpace.getLevel().toInt();
            boolean noMoveUp = player.getCurrentGameBoard().getIslandBoard().isNoMoveUp();
            // remove relative level not allowed
            if (relativeLevel > 1 || (noMoveUp && relativeLevel == 1))
                iterator.remove();
        }
        return availableMoves;
    }
}
