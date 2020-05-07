package it.polimi.ingsw.xyl.model.message;

public class ConnectionDroppedMessage extends Message{
    public final String playerName;
    public ConnectionDroppedMessage(String playerName){
        this.playerName = playerName;
    }
}
