package it.polimi.ingsw.xyl.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class VirtualGame implements Serializable {
    public class VPlayer{
        private int playerId;
        private String playerName;
        private Cosplayer cosplayer;
        private String nextAction;
        private PlayerStatus playerStatus = PlayerStatus.INGAMEBOARD;
        private int workerAX;
        private int workerAY;
        private int workerBX;
        private int workerBY;


        VPlayer(int playerId, String playerName){
            this.playerId = playerId;
            this.playerName = playerName;
        }

        public String getPlayerName() {
            return playerName;
        }

        public Cosplayer getCosplayer() {
            return cosplayer;
        }

        public String getNextAction() {
            return nextAction;
        }

        public PlayerStatus getPlayerStatus() {
            return playerStatus;
        }
    }

    private GameStatus gameStatus = GameStatus.WAITINGINIT;
    private Map<Integer, VPlayer> vPlayers = new HashMap<>();
    private Space[][] spaces = new Space[5][5];
    private Vector<GodPower> availableGodPowers;
    private int currentPlayerId;

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
                tempVPlayer.playerStatus = player.getCurrentStatus();
                if(player.getCosplayer()!=null)
                    tempVPlayer.nextAction = player.getCosplayer().getNextAction();
                if(player.getCurrentStatus() == PlayerStatus.WORKERPREPARED) {
                    tempVPlayer.workerAX = player.getWorkers()[0].getPositionX();
                    tempVPlayer.workerAY = player.getWorkers()[0].getPositionY();
                    tempVPlayer.workerBX = player.getWorkers()[1].getPositionX();
                    tempVPlayer.workerBY = player.getWorkers()[1].getPositionY();
                }
            } else {
                VPlayer tempVPlayer = new VPlayer(player.getPlayerId(), player.getPlayerName());
                tempVPlayer.cosplayer = player.getCosplayer();
                tempVPlayer.playerStatus = player.getCurrentStatus();
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

    public Vector<GodPower> getAvailableGodPowers() {
        return availableGodPowers;
    }

    public void setAvailableGodPowers(Vector<GodPower> availableGodPowers) {
        this.availableGodPowers = availableGodPowers; //Todo:need deep copy
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    // only for test
    public String getFirstPlayerName(){
        return vPlayers.get(0).playerName;
    }

    // only for test
    public String getCurrentPlayerAction(){
        return vPlayers.get(currentPlayerId).nextAction;
    }
}
