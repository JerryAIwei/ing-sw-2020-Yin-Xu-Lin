package it.polimi.ingsw.xyl.model.message;

public class AfterGameMessage extends Message{
    public final String playerName;
    public final int gameId;
    public AfterGameMessage(String playerName, int gameId){
        this.playerName = playerName;
        this.gameId = gameId;
    }
}
