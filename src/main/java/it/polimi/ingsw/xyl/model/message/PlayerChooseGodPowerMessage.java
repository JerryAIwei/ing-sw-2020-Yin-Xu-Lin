package it.polimi.ingsw.xyl.model.message;

import it.polimi.ingsw.xyl.model.GodPower;

public class PlayerChooseGodPowerMessage extends Message {
    private final int playerId;
    private final GodPower godPower;

    public PlayerChooseGodPowerMessage(int gameId, int playerId, GodPower godPower){
        this.gameId = gameId;
        this.playerId = playerId;
        this.godPower = godPower;
    }

    public int getPlayerId() {
        return playerId;
    }

    public GodPower getGodPower() {
        return godPower;
    }

    public int getGameId(){
        return gameId;
    }
}
