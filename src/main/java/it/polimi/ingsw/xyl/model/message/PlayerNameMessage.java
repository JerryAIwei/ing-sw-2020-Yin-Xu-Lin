package it.polimi.ingsw.xyl.model.message;

import java.net.InetAddress;

public class PlayerNameMessage extends Message {


    private InetAddress ip;
    private final String playerName;

    public PlayerNameMessage(String playerName) {
        this.playerName = playerName;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public InetAddress getIp() {
        return ip;
    }

    public String getPlayerName() {
        return playerName;
    }
}
