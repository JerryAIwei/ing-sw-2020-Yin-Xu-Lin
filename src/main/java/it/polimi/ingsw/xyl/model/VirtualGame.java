package it.polimi.ingsw.xyl.model;

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
        public int getPlayerId() {
            return playerId;
        }

        public void setPlayerId(int playerId) {
            this.playerId = playerId;
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public Cosplayer getCosplayer() {
            return cosplayer;
        }

        public void setCosplayer(Cosplayer cosplayer) {
            this.cosplayer = cosplayer;
        }

        public PlayerStatus getCurrentStatus() {
            return currentStatus;
        }

        public void setCurrentStatus(PlayerStatus currentStatus) {
            this.currentStatus = currentStatus;
        }

        public int getWorkerAX() {
            return workerAX;
        }

        public void setWorkerAX(int workerAX) {
            this.workerAX = workerAX;
        }

        public int getWorkerAY() {
            return workerAY;
        }

        public void setWorkerAy(int workerAY) {
            this.workerAY = workerAY;
        }

        public int getWorkerBX() {
            return workerBX;
        }

        public void setWorkerBX(int workerBX) {
            this.workerBX = workerBX;
        }

        public int getWorkerBy() {
            return workerBY;
        }

        public void setWorkerBy(int workerBY) {
            this.workerBY = workerBY;
        }



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

    public void updateVPlayers(Player[] players) {
        for(Player player:players) {
            if (vPlayers.get(player.getPlayerId()) != null) {
                VPlayer tempVPlayer = vPlayers.get(player.getPlayerId());
                tempVPlayer.currentStatus = player.getCurrentStatus();
                tempVPlayer.workerAX = player.getWorkers()[0].getPositionX();
                tempVPlayer.workerAY = player.getWorkers()[0].getPositionY();
                tempVPlayer.workerBX = player.getWorkers()[1].getPositionX();
                tempVPlayer.workerBY = player.getWorkers()[1].getPositionY();
            } else {
                VPlayer tempVPlayer = new VPlayer(player.getPlayerId(), player.getPlayerName());
                tempVPlayer.cosplayer = player.getCosplayer();
                tempVPlayer.currentStatus = player.getCurrentStatus();
                tempVPlayer.workerAX = player.getWorkers()[0].getPositionX();
                tempVPlayer.workerAY = player.getWorkers()[0].getPositionY();
                tempVPlayer.workerBX = player.getWorkers()[1].getPositionX();
                tempVPlayer.workerBY = player.getWorkers()[1].getPositionY();
                //Todo: switch case by currentStatus
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
}
