package it.polimi.ingsw.xyl.model.message;

public class JoinGameMessage extends Message {

    private final String playerName;
    private final int gameID;

    public JoinGameMessage(String playerName, int gameID) {
        this.playerName = playerName;
        this.gameID = gameID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getGameID() {
        return gameID;
    }
}
