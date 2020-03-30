package it.polimi.ingsw.xyl.model.message;

public class PlayerNameMessage extends Message {
    private final String playerName;

    public PlayerNameMessage(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
