package it.polimi.ingsw.xyl.model.message;

public class JoinGameMessage extends Message {
    private final int gameID;

    public JoinGameMessage(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
