package it.polimi.ingsw.xyl.model.message;

import it.polimi.ingsw.xyl.network.server.PlayerServer;

public class PlayerNameMessage extends Message {
    private final String playerName;
    private PlayerServer ps;

    public PlayerNameMessage(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerServer getPs() {
        return ps;
    }

    public void setPs(PlayerServer ps) {
        this.ps = ps;
    }
}
