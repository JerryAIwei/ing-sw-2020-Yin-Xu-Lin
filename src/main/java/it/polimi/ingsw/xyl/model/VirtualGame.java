package it.polimi.ingsw.xyl.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VirtualGame {
    private class VPlayer{
        private int playerId;
        private String playerName;
        private Cosplayer cosplayer;
        private PlayerStatus currentStatus = PlayerStatus.INGAMEBOARD;
        private int workerAX;
        private int workerAY;
        private int workerBX;
        private int workerBY;

        VPlayer(int playerId, String playerName){
            this.playerId = playerId;
            this.playerName = playerName;
        }
    }

    private GameStatus gameStatus = GameStatus.WAITINGPLAYER;
    private Map<Integer, VPlayer> vPlayers = new HashMap<>();

    private Space[][] spaces = new Space[5][5];

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Map<Integer, VPlayer> getVPlayers() {
        return vPlayers;
    }

    public void updateVPlayers(Collection<Player> players) {
        for(Player player:players) {
            if (vPlayers.get(player.getPlayerId()) != null) {
                VPlayer tempVPlayer = vPlayers.get(player.getPlayerId());
                tempVPlayer.currentStatus = player.getCurrentStatus();
                if(player.getCurrentStatus() == PlayerStatus.WORKERPREPARED) {
                    tempVPlayer.workerAX = player.getWorkers()[0].getPositionX();
                    tempVPlayer.workerAY = player.getWorkers()[0].getPositionY();
                    tempVPlayer.workerBX = player.getWorkers()[1].getPositionX();
                    tempVPlayer.workerBY = player.getWorkers()[1].getPositionY();
                }
            } else {
                VPlayer tempVPlayer = new VPlayer(player.getPlayerId(), player.getPlayerName());
                tempVPlayer.cosplayer = player.getCosplayer();
                tempVPlayer.currentStatus = player.getCurrentStatus();
                vPlayers.put(tempVPlayer.playerId,tempVPlayer);
            }
        }
    }

    public Space[][] getSpaces() {
        return spaces;
    }

    public void setSpaces(Space[][] spaces) {
        this.spaces = spaces;//Todo:need deep copy
    }

    // only for test
    public String getFirstPlayerName(){
        return vPlayers.get(0).playerName;
    }
}
