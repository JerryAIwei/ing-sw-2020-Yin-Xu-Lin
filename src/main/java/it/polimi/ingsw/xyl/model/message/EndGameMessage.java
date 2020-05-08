package it.polimi.ingsw.xyl.model.message;

public class EndGameMessage extends Message{
    public final String playerName;
    public final int isRestart;
    public EndGameMessage(String playerName, int isRestart){
        this.playerName = playerName;
        this.isRestart = isRestart;
    }
}
