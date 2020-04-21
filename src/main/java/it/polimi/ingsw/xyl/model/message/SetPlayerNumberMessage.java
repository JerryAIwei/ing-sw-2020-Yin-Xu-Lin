package it.polimi.ingsw.xyl.model.message;

public class SetPlayerNumberMessage extends Message{
    private final int gameId;
    private final int playerNumber;

    public SetPlayerNumberMessage(int gameId, int playerNumber){
        this.gameId = gameId;
        this.playerNumber = playerNumber;
    }

    public int getGameId() {
        return gameId;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }
}
