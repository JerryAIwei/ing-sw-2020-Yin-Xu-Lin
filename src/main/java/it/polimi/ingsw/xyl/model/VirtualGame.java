package it.polimi.ingsw.xyl.model;

import it.polimi.ingsw.xyl.model.message.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * VirtualGame is used by server to
 * send all necessary data to client.
 * It's also used to implement the
 * Persistence advanced function of the project.
 *
 * @author Shaoxun
 * @author yaw
 */
public class VirtualGame extends Message {
    /**
     * VPlayer class is a virtual player class to save necessary data
     */
    public class VPlayer implements Serializable {
        public final int playerId;
        public final String playerName;
        private GodPower godPower;
        private String nextAction;
        private int workerInAction = -1;
        private PlayerStatus playerStatus = PlayerStatus.INGAMEBOARD;
        private Worker[] workers;
        private final HashMap<String, ArrayList<Direction>> availableMovesAndBuilds = new HashMap<>();

        VPlayer(int playerId, String playerName) {
            this.playerId = playerId;
            this.playerName = playerName;
        }

        public Worker[] getWorkers() {
            return workers;
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
    private int playerNumber;
    private final HashMap<Integer, VPlayer> vPlayers = new HashMap<>();
    private Space[][] spaces = new Space[5][5];
    private final ArrayList<GodPower> allGodPowers = new ArrayList<>();
    private final ArrayList<GodPower> availableGodPowers = new ArrayList<>();
    private int currentPlayerId;
    private boolean noLevelUp;

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

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public HashMap<Integer, VPlayer> getVPlayers() {
        return vPlayers;
    }

    public void updateVPlayers(Collection<Player> players) {
        for (Player player : players) {
            if (vPlayers.get(player.getPlayerId()) != null) {
                VPlayer tempVPlayer = vPlayers.get(player.getPlayerId());
                tempVPlayer.playerStatus = player.getCurrentStatus();
                tempVPlayer.godPower = player.getCosplayer().getGodPower();
                if (player.getCosplayer() != null)
                    tempVPlayer.nextAction = player.getCosplayer().getNextAction();
                if (player.getCurrentStatus() == PlayerStatus.WORKERPREPARED) {
                    tempVPlayer.workers = player.getWorkers();
                    tempVPlayer.workerInAction = player.getCosplayer().getWorkerInAction();
                    tempVPlayer.availableMovesAndBuilds.put("Move0", player.getCosplayer().getAvailableMoves(0));
                    tempVPlayer.availableMovesAndBuilds.put("Move1", player.getCosplayer().getAvailableMoves(1));
                    tempVPlayer.availableMovesAndBuilds.put("Build0", player.getCosplayer().getAvailableBuilds(0));
                    tempVPlayer.availableMovesAndBuilds.put("Build1", player.getCosplayer().getAvailableBuilds(1));
                }
            } else {
                VPlayer tempVPlayer = new VPlayer(player.getPlayerId(), player.getPlayerName());
                tempVPlayer.playerStatus = player.getCurrentStatus();
                tempVPlayer.godPower = player.getCosplayer().getGodPower();
                vPlayers.put(tempVPlayer.playerId, tempVPlayer);
            }
        }
        save();
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
        if (availableGodPowers.size()==playerNumber){
            allGodPowers.addAll(availableGodPowers);
        }
        this.availableGodPowers.clear();
        this.availableGodPowers.addAll(availableGodPowers);
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public ArrayList<GodPower> getAllGodPowers() {
        return allGodPowers;
    }

    public boolean isNoLevelUp() {
        return noLevelUp;
    }

    public void setNoLevelUp(boolean noLevelUp) {
        this.noLevelUp = noLevelUp;
    }

    /**
     * This method is used by saving VirtualGame object into a data file.
     * For persistence advanced function of the project.
     * All data files are in ./data/
     */
    private void save(){
        try
        {
            File dir = new File("./data/");
            boolean dirCreated = false;
            if(!dir.exists())
                dirCreated = dir.mkdirs();
            else
                dirCreated = true;
            if (dirCreated) {
                FileOutputStream fileOut = new FileOutputStream("./data/virtualGame_" + gameId + ".ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(this);
                out.close();
                fileOut.close();
            }
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }
}
