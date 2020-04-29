package it.polimi.ingsw.xyl.model.message;

public class StartGameMessage extends Message{
    private final int gameId;
    private final String fromPlayer;
    private final int startPlayerId;
    public StartGameMessage(int gameId, String fromPlayer, int startPlayerId){
        this.gameId = gameId;
        this.fromPlayer = fromPlayer;
        this.startPlayerId = startPlayerId;
    }

    public int getGameId() {
        return gameId;
    }

    public String getFromPlayer() {
        return fromPlayer;
    }

    public int getStartPlayerId() {
        return startPlayerId;
    }
}
