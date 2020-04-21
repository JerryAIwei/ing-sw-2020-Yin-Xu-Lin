package it.polimi.ingsw.xyl.model.message;

import it.polimi.ingsw.xyl.model.GodPower;

public class AvailableGodPowersMessage extends Message {
    private final int gameId;
    private final GodPower godPowerA;
    private final GodPower godPowerB;
    private final GodPower godPowerC;

    public AvailableGodPowersMessage(int gameId, GodPower godPowerA, GodPower godPowerB){
        this.gameId = gameId;
        this.godPowerA = godPowerA;
        this.godPowerB = godPowerB;
        this.godPowerC = null;
    }
    public AvailableGodPowersMessage(int gameId, GodPower godPowerA, GodPower godPowerB, GodPower godPowerC){
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


    public int getGameId(){
        return gameId;
    }
}
