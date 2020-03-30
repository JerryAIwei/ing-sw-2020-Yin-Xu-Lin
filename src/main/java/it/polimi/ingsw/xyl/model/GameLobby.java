package it.polimi.ingsw.xyl.model;

import java.util.*;

/**
 * This class represents a game lobby,
 * where one or more game is in process.
 *
 * @author Shaoxun
 */
public class GameLobby {
    private String serverIP;
    private Vector<GameBoard> gameBoards;
    private Map<String,Integer> allPlayers = new HashMap<>(); //TODO: when a game end, delete relevant players

    public GameLobby() {
        gameBoards= new Vector<>();
        GameBoard gameBoard = new GameBoard(0);
        gameBoards.add(gameBoard);
    }

    public Map<String,Integer> getAllPlayers() {
        return allPlayers;
    }

    public void add2AllPlayers(String playerName, int gameId){
        allPlayers.put(playerName,gameId);
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Vector<GameBoard> getGameBoards() {
        return gameBoards;
    }

    public void addGameBoard(GameBoard gameBoard) {
        this.gameBoards.add(gameBoard);
    }

}
