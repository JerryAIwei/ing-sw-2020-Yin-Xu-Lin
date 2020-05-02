package it.polimi.ingsw.xyl.model.message;

import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.GameLobby;
import it.polimi.ingsw.xyl.model.GameStatus;
import it.polimi.ingsw.xyl.model.Player;

import java.io.Serializable;
import java.util.Vector;

public class NameOKMessage extends Message {
    public class Games implements Serializable {
        private final int gameID;
        private final int playerNumber;
        private int currentNumber;
        private final Vector<String> currentPlayers = new Vector<>();

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

        public int getCurrentNumber() {
            return currentNumber;
        }
    }
    private final Vector<Games> games = new Vector<>();

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

    public Vector<Games> getGames() {
        return games;
    }
}
