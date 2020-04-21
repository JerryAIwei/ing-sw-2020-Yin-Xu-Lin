package it.polimi.ingsw.xyl.model.message;

public class CreateNewGameMessage extends Message {
    private final String playerName;

    public CreateNewGameMessage(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

}
