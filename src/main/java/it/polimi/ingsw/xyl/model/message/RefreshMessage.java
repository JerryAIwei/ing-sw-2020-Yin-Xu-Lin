package it.polimi.ingsw.xyl.model.message;

public class RefreshMessage extends Message{
    public final String playerName;
    public RefreshMessage(String playerName){
        this.playerName = playerName;
    }
}
