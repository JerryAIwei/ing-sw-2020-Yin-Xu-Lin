package it.polimi.ingsw.xyl.model;

import java.util.ArrayList;

/**
 * This class is the abstraction of A Santorini GAME (Civilian Mod),
 * A game lobby handles with one or more game(board).
 *
 * @author Shaoxun
 */
public class GameBoard {
    private int gameId;
    private int playerNumber;
    private int turnId;
    private ArrayList<Player> players;
    private IslandBoard islandBoard;
    private GameStatus currentStatus;

    public GameBoard(int gameId, int playerNumber, IslandBoard islandBoard) {
        this.gameId = gameId;
        this.playerNumber = playerNumber;
        this.islandBoard = islandBoard;
    }
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
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

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        //TODO: add playerNumber condition
        //TODO: multi-gameBoard
        try {
            players.add(player);
        }catch (Exception e){
            init();
            throw e;
        }
    }

    public IslandBoard getIslandBoard() {
        return islandBoard;
    }

    public void setIslandBoard(IslandBoard islandBoard) {
        this.islandBoard = islandBoard;
    }

    public GameStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(GameStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void init(){
        if(playerNumber == 2 || playerNumber==3)
            this.players = new ArrayList<>(playerNumber);
    }
}
