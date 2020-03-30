package it.polimi.ingsw.xyl.model.message;

public class MyTurnFinishedMessage extends RoleControllerMessage {

    public MyTurnFinishedMessage(int gameId, int playerId) {
        super(gameId, playerId);
    }

    public boolean isFinished() {
        return true;
    }

    public int getGameId() {
        return gameId;
    }

    public int getPlayerId(){
        return playerId;
    }
}
