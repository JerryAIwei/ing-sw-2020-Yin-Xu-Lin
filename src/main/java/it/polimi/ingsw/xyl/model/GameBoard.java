package it.polimi.ingsw.xyl.model;

import java.util.Vector;

/**
 * This class is the abstraction of A Santorini GAME,
 * A game lobby handles with one or more game(board).
 *
 * @author Shaoxun
 */
public class GameBoard {
    private int gameId;
    private int playerNumber = 3;  // add default value in case any exception caused by not setting playerNumber
    private int turnId;
    private Vector<Player> players;
    private IslandBoard islandBoard;
    private Vector<GodPower> availableGodPowers;
    private GameStatus currentStatus;

    public GameBoard(int gameId) {
        this.gameId = gameId;
        this.islandBoard = new IslandBoard();
        this.players = new Vector<>();
        this.availableGodPowers = new Vector<>();
    }

    public GameBoard(int gameId, int playerNumber, IslandBoard islandBoard) {
        this.gameId = gameId;
        this.playerNumber = playerNumber;
        this.islandBoard = islandBoard;
        this.players = new Vector<>();
        this.availableGodPowers = new Vector<>();
    }

    public int getGameId() {
        return gameId;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getTurnId() {
        return turnId;
    }

    public void setTurnId(int turnId) {
        this.turnId = turnId;
    }

    public Vector<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setCurrentStatus(PlayerStatus.INGAMEBOARD);
    }

    public IslandBoard getIslandBoard() {
        return islandBoard;
    }

    public Vector<GodPower> getAvailableGodPowers(){
        return availableGodPowers;
    }

    public void addAvailableGodPowers(GodPower godPowers){
        availableGodPowers.add(godPowers);
    }

    public GameStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(GameStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
