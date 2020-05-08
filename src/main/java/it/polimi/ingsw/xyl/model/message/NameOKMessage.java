package it.polimi.ingsw.xyl.model.message;

import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.GameLobby;
import it.polimi.ingsw.xyl.model.GameStatus;
import it.polimi.ingsw.xyl.model.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class NameOKMessage extends Message {
    public class Games implements Serializable {
        private final int gameID;
        private final int playerNumber;
        private int currentNumber;
        private final ArrayList<String> currentPlayers = new ArrayList<>();

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

        public int getCurrentNumber() {
            return currentNumber;
        }

        public ArrayList<String> getCurrentPlayers() {
            return currentPlayers;
        }
    }
    private final ArrayList<Games> games = new ArrayList<>();


    public NameOKMessage(GameLobby gameLobby){
        for(GameBoard gb:gameLobby.getGameBoards()){
            if (gb.getCurrentStatus() != GameStatus.WAITINGINIT ) {
                Games game = new Games(gb.getGameId(), gb.getPlayerNumber());
                for (Player player : gb.getPlayers().values()) {
                    game.currentPlayers.add(player.getPlayerName());
                }
                game.currentNumber = game.currentPlayers.size();
                games.add(game);
            }
        }

    }

    public ArrayList<Games> getGames() {
        return games;
    }
}
