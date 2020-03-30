package it.polimi.ingsw.xyl.model.message;

public class StartGameMessage extends Message{
    private String fromPlayer;
    private int startPlayerId;
    public StartGameMessage(int gameId, String fromPlayer, int playerId){
        this.gameId = gameId;
        this.fromPlayer = fromPlayer;
        this.startPlayerId = playerId;
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
