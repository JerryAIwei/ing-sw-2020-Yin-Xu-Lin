package it.polimi.ingsw.xyl.model.message;

import it.polimi.ingsw.xyl.model.GodPower;

public class PlayerChooseGodPowerMessage extends RoleControllerMessage {
    private final GodPower godPower;

    public PlayerChooseGodPowerMessage(int gameId, int playerId, GodPower godPower){
        super(gameId, playerId);
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
