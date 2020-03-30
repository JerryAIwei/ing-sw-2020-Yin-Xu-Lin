package it.polimi.ingsw.xyl.model.message;

import it.polimi.ingsw.xyl.model.GodPower;

public class AvailableGodPowersMessage extends Message {
    private final int playerNumber;
    private final GodPower godPowerA;
    private final GodPower godPowerB;
    private final GodPower godPowerC;

    public AvailableGodPowersMessage(int gameId, GodPower godPowerA, GodPower godPowerB){
        this.playerNumber = 2;
        this.gameId = gameId;
        this.godPowerA = godPowerA;
        this.godPowerB = godPowerB;
        this.godPowerC = null;
    }
    public AvailableGodPowersMessage(int gameId, GodPower godPowerA, GodPower godPowerB, GodPower godPowerC){
        this.playerNumber = 3;
        this.gameId = gameId;
        this.godPowerA = godPowerA;
        this.godPowerB = godPowerB;
        this.godPowerC = godPowerC;
    }

    public GodPower getGodPower(Character c) {
        switch(c){
            case 'A' : return godPowerA;
            case 'B' : return godPowerB;
            case 'C' : return godPowerC;
            default : return null;
        }
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

    public int getGameId(){
        return gameId;
    }
}
