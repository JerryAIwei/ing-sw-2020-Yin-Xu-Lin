package it.polimi.ingsw.xyl.model;

import java.util.HashMap;
import java.util.Vector;

/**
 * This class represents a game lobby,
 * where one or more game is in process.
 *
 * @author Shaoxun
 */
public class GameLobby {
    private static final int NO_GAME_ID = -1;
    private static final String RECONNECTION = "_RECONNECTION";
    private final Vector<GameBoard> gameBoards;
    private final HashMap<String,Integer> allPlayers;

    public GameLobby() {
        gameBoards= new Vector<>();
        allPlayers = new HashMap<>(); //TODO: when a game end, delete relevant players
    }

    public HashMap<String,Integer> getAllPlayers() {
        return allPlayers;
    }

    public String add2AllPlayers(String playerName, int a){
        if (a == NO_GAME_ID){
            if (allPlayers.get(playerName) == null) {
                allPlayers.put(playerName, a);
                return playerName;
            }else{
                int gameId = allPlayers.get(playerName);
                if(gameBoards.get(gameId).getReconnecting()) {
                    // if reconnection
                    return RECONNECTION;
                }else{
                    return null;
                }
            }
        }else{
            allPlayers.put(playerName, a);
            return playerName;
        }
    }

    public Vector<GameBoard> getGameBoards() {
        return gameBoards;
    }

    public void addGameBoard(GameBoard gameBoard) {
        this.gameBoards.add(gameBoard);
    }

}
