package it.polimi.ingsw.xyl.model.message;

public class RoleControllerMessage extends Message {
    protected final int playerId;
    public RoleControllerMessage(int gameId, int playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getGameId() {
        return gameId;
    }
}
