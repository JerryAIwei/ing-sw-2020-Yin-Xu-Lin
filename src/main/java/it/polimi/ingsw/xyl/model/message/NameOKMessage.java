package it.polimi.ingsw.xyl.model.message;

import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.GameLobby;
import it.polimi.ingsw.xyl.model.Player;

import java.io.Serializable;
import java.util.Vector;

public class NameOKMessage extends Message {
    public class Games implements Serializable {
        private int gameID;
        private int playerNumber;
        private Vector<String> currentPlayers = new Vector<>();

        public Games(int id, int num){
            gameID = id;
            playerNumber = num;
        }

        public int getGameID() {
            return gameID;
        }

        public int getPlayerNumber() {
            return playerNumber;
        }

        public Vector<String> getCurrentPlayers() {
            return currentPlayers;
        }
    }
    private Vector<Games> games = new Vector<>();

    public NameOKMessage(GameLobby gameLobby){
        for(GameBoard gb:gameLobby.getGameBoards()){
            Games game = new Games(gb.getGameId(),gb.getPlayerNumber());
            for(Player player:gb.getPlayers().values()){
                game.currentPlayers.add(player.getPlayerName());
            }
            games.add(game);
        }
    }

    public Vector<Games> getGames() {
        return games;
    }
}
