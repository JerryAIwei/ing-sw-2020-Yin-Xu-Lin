package it.polimi.ingsw.xyl.model;

import it.polimi.ingsw.xyl.model.message.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class VirtualGame extends Message {
    public class VPlayer implements Serializable {
        private final int playerId;
        private final String playerName;
        private String godPower;
        private String nextAction;
        private int workerInAction = -1;
        private PlayerStatus playerStatus = PlayerStatus.INGAMEBOARD;
        private int workerAX;
        private int workerAY;
        private int workerBX;
        private int workerBY;
        private final HashMap<String, ArrayList<Direction>> availableMovesAndBuilds = new HashMap<>();

        VPlayer(int playerId, String playerName) {
            this.playerId = playerId;
            this.playerName = playerName;
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getGodPower() {
            return godPower;
        }

        public String getNextAction() {
            return nextAction;
        }

        public int getWorkerInAction() {
            return workerInAction;
        }

        public PlayerStatus getPlayerStatus() {
            return playerStatus;
        }

        public ArrayList<Direction> getAvailable(String action, int worker) {
            return availableMovesAndBuilds.get(action + worker);
        }
    }

    private final int gameId;
    private GameStatus gameStatus;
    private final HashMap<Integer, VPlayer> vPlayers = new HashMap<>();
    private Space[][] spaces = new Space[5][5];
    private final ArrayList<GodPower> availableGodPowers = new ArrayList<>();
    private int currentPlayerId;

    public VirtualGame(int gameId) {
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

    public HashMap<Integer, VPlayer> getVPlayers() {
        return vPlayers;
    }

    public void updateVPlayers(Collection<Player> players) {
        for (Player player : players) {
            if (vPlayers.get(player.getPlayerId()) != null) {
                VPlayer tempVPlayer = vPlayers.get(player.getPlayerId());
                tempVPlayer.playerStatus = player.getCurrentStatus();
                tempVPlayer.godPower = player.getCosplayer().getGodPower().toString();
                if (player.getCosplayer() != null)
                    tempVPlayer.nextAction = player.getCosplayer().getNextAction();
                if (player.getCurrentStatus() == PlayerStatus.WORKERPREPARED) {
                    tempVPlayer.workerAX = player.getWorkers()[0].getPositionX();
                    tempVPlayer.workerAY = player.getWorkers()[0].getPositionY();
                    tempVPlayer.workerBX = player.getWorkers()[1].getPositionX();
                    tempVPlayer.workerBY = player.getWorkers()[1].getPositionY();
                    tempVPlayer.workerInAction = player.getCosplayer().getWorkerInAction();
                    tempVPlayer.availableMovesAndBuilds.put("Move0", player.getCosplayer().getAvailableMoves(0));
                    tempVPlayer.availableMovesAndBuilds.put("Move1", player.getCosplayer().getAvailableMoves(1));
                    tempVPlayer.availableMovesAndBuilds.put("Build0", player.getCosplayer().getAvailableBuilds(0));
                    tempVPlayer.availableMovesAndBuilds.put("Build1", player.getCosplayer().getAvailableBuilds(1));
                }
            } else {
                VPlayer tempVPlayer = new VPlayer(player.getPlayerId(), player.getPlayerName());
                tempVPlayer.playerStatus = player.getCurrentStatus();
                tempVPlayer.godPower = player.getCosplayer().getGodPower().toString();
                vPlayers.put(tempVPlayer.playerId, tempVPlayer);
            }
        }
    }

    public Space[][] getSpaces() {
        return spaces;
    }

    public void setSpaces(Space[][] spaces) {
        this.spaces = spaces;
    }

    public ArrayList<GodPower> getAvailableGodPowers() {
        return availableGodPowers;
    }

    public void setAvailableGodPowers(ArrayList<GodPower> availableGodPowers) {
        this.availableGodPowers.clear();
        this.availableGodPowers.addAll(availableGodPowers);
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

}
