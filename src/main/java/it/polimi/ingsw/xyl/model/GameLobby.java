package it.polimi.ingsw.xyl.model;

import java.util.ArrayList;

/**
 * This class represents a game lobby,
 * where one or more game is in process.
 *
 * @author Shaoxun
 */
public class GameLobby {
    private String serverIP;
    private ArrayList<GameBoard> gameBoards;

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public ArrayList<GameBoard> getGameBoards() {
        return gameBoards;
    }

    public void addGameBoards(GameBoard gameBoard) {
        try {
            this.gameBoards.add(gameBoard);
        }catch (Exception e){
            init();
            throw e;
        }
    }
    public void init() {
        gameBoards= new ArrayList<>();
    }
}
