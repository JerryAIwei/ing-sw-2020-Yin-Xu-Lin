package it.polimi.ingsw.xyl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This class is the abstraction of A Santorini GAME,
 * A game lobby handles with one or more game(board).
 *
 * @author Shaoxun
 */
public class GameBoard {
    private final int gameId;
    private int playerNumber = 3;
    private Player currentPlayer;
    private final HashMap<Integer, Player> players;
    private final IslandBoard islandBoard;
    private final ArrayList<GodPower> availableGodPowers;
    private GameStatus currentStatus;
    private boolean reconnecting;

    public GameBoard(int gameId) {
        this.gameId = gameId;
        this.islandBoard = new IslandBoard();
        this.players = new HashMap<>();
        this.availableGodPowers = new ArrayList<>();
    }

    public GameBoard(int gameId, int playerNumber, IslandBoard islandBoard) {
        this.gameId = gameId;
        this.playerNumber = playerNumber;
        this.islandBoard = islandBoard;
        this.players = new HashMap<>();
        this.availableGodPowers = new ArrayList<>();
    }

    public int getGameId() {
        return gameId;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    // at the beginning, the "Challenger" (owner of the game) is the current player;
    // after one complete turn, "Challenger" chooses a "Start Player",
    // set currentPlayer to "Start Player"
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void toNextPlayer() {
        currentPlayer = players.get((currentPlayer.getPlayerId() + 1) % playerNumber);
        if (currentStatus == GameStatus.INGAME && currentPlayer.getCurrentStatus() != PlayerStatus.WAITING4INIT) {
            currentPlayer.getCosplayer().prepare();
            currentPlayer.getCosplayer().checkWin();
            if (currentPlayer.getCurrentStatus() == PlayerStatus.LOSE)
                toNextPlayer();
        }
    }

    public void toNextPlayer(int playerId) {
        currentPlayer = players.get(playerId);
        currentPlayer.getCosplayer().prepare();
    }

    public void restoreNextPlayer(int playerId) {
        currentPlayer = players.get(playerId);
    }

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }

    public Player getPlayer(String playerName) {
        Player player = null;
        for (int i = 0;i<players.size();i++) {
            if (players.get(i).getPlayerName().equals(playerName)) {
                player = players.get(i);
                break;
            }
        }
        return player;
    }

    public ArrayList<String> getAllPlayerNames(){
        ArrayList<String> names = new ArrayList<>();
        for (Map.Entry<Integer, Player> entry : players.entrySet()) {
            names.add(entry.getValue().getPlayerName());
        }
        return names;
    }

    public void addPlayer(Player player) {
        players.put(players.size(), player);
        player.setCurrentStatus(PlayerStatus.INGAMEBOARD);
        if (players.size() == 1)
            currentPlayer = player;
    }

    public IslandBoard getIslandBoard() {
        return islandBoard;
    }

    public ArrayList<GodPower> getAvailableGodPowers() {
        return availableGodPowers;
    }

    public void addAvailableGodPowers(GodPower godPowers) {
        availableGodPowers.add(godPowers);
    }

    public GameStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(GameStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void setReconnecting(boolean reconnecting){
        this.reconnecting = reconnecting;
        if(reconnecting) {
            for (Map.Entry<Integer, Player> entry : players.entrySet()) {
                entry.getValue().setReconnecting(reconnecting);
            }
        }
    }

    public boolean getReconnecting(){
        return reconnecting;
    }
}
