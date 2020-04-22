package it.polimi.ingsw.xyl.model;

import it.polimi.ingsw.xyl.model.message.Message;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class VirtualGame extends Message {
    public class VPlayer implements Serializable{
        private int playerId;
        private String playerName;
        private GodPower godPower;
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

        public GodPower getGodPower() {
            return godPower;
        }

        public String getNextAction() {
            return nextAction;
        }

        public PlayerStatus getPlayerStatus() {
            return playerStatus;
        }
    }

    private int gameId;
    private GameStatus gameStatus ;//= GameStatus.WAITINGINIT;
    private Map<Integer, VPlayer> vPlayers = new HashMap<>();
    private Space[][] spaces = new Space[5][5];
    private Vector<GodPower> availableGodPowers = new Vector<>();
    private int currentPlayerId;

    public VirtualGame(int gameId){
        this.gameId = gameId;
    }
    public int getGameId() {
        return gameId;
    }

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
                tempVPlayer.godPower = player.getCosplayer().getGodPower();
                tempVPlayer.playerStatus = player.getCurrentStatus();
                vPlayers.put(tempVPlayer.playerId,tempVPlayer);
            }
        }
    }

    public Space[][] getSpaces() {
        return spaces;
    }

    public void setSpaces(Space[][] spaces) {
        this.spaces = spaces;
    }

    public Vector<GodPower> getAvailableGodPowers() {
        return availableGodPowers;
    }

    public void setAvailableGodPowers(Vector<GodPower> availableGodPowers) {

            this.availableGodPowers = (Vector<GodPower>)availableGodPowers.clone(); //Todo:need deep copy
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
