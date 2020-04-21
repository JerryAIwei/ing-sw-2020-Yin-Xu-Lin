package it.polimi.ingsw.xyl.model;

import java.util.*;

/**
 * This class represents a game lobby,
 * where one or more game is in process.
 *
 * @author Shaoxun
 */
public class GameLobby {
    private final Vector<GameBoard> gameBoards;
    private final Map<String,Integer> allPlayers = new HashMap<>(); //TODO: when a game end, delete relevant players

    public GameLobby() {
        gameBoards= new Vector<>();
    }

    public Map<String,Integer> getAllPlayers() {
        return allPlayers;
    }

    public String add2AllPlayers(String playerName, int a){
        if (allPlayers.get(playerName) == null) {
            allPlayers.put(playerName, a);
            return playerName;
        }
        else
           return null;
    }

    public Vector<GameBoard> getGameBoards() {
        return gameBoards;
    }

    public void addGameBoard(GameBoard gameBoard) {
        this.gameBoards.add(gameBoard);
    }

}
