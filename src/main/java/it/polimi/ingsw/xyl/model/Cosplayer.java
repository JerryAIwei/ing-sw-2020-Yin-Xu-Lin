package it.polimi.ingsw.xyl.model;

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

    public Cosplayer(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    /**
     * move method of cosplayer(Civilian Mod)
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction){
        try {
            int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            IslandBoard currentIslandBoard = player.getCurrentGameBoard().getIslandBoard();
            Space targetSpace = currentIslandBoard.getSpaces()[targetPositionX][targetPositionY];
            int targetOccupiedBy = targetSpace.isOccupiedByPlayer();
            boolean noDome = targetSpace.getLevel() != DOME;
            Space currentSpace =
                    currentIslandBoard.getSpaces()[player.getWorkers()[worker].getPositionX()][player.getWorkers()[worker].getPositionY()];
            int relativeLevel = targetSpace.getLevel().toInt() - currentSpace.getLevel().toInt();

            if (targetOccupiedBy == 0 && noDome && relativeLevel <= 1) {
                // free the current occupied space
                currentIslandBoard.getSpaces()[player.getWorkers()[worker].getPositionX()][player.getWorkers()[worker].getPositionY()].setOccupiedByPlayer(0);
                // chang the worker's position to target space
                player.getWorkers()[worker].setPosition(
                        player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0],
                        player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]
                );
                // occupy the target space by playerId
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setOccupiedByPlayer(player.getPlayerId());
            } else {
                System.out.println("Chosen worker can't be moved, the target space is occupied!");
            }
        }catch(Exception e){
                System.out.println("Array out of bounds");
                throw e;
        }
    }

    /**
     * build method of cosplayer(Civilian Mod)
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    public void build(int worker, Direction direction, boolean buildDome){
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
        }catch (Exception e){
            System.out.println("Array out of bounds");
            throw e;
        }
    }
}