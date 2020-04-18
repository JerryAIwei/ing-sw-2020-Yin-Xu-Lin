package it.polimi.ingsw.xyl.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * This class is the abstraction of A Santorini GAME,
 * A game lobby handles with one or more game(board).
 *
 * @author Shaoxun
 */
public class GameBoard {
    private int gameId;
    private int playerNumber = 3;  // add default value in case any exception caused by not setting playerNumber
    private Player currentPlayer;
    private Map<Integer,Player> players;
    private IslandBoard islandBoard;
    private Vector<GodPower> availableGodPowers;
    private GameStatus currentStatus;

    public GameBoard(int gameId) {
        this.gameId = gameId;
        this.islandBoard = new IslandBoard();
        this.players = new HashMap<>();
        this.availableGodPowers = new Vector<>();
    }

    public GameBoard(int gameId, int playerNumber, IslandBoard islandBoard) {
        this.gameId = gameId;
        this.playerNumber = playerNumber;
        this.islandBoard = islandBoard;
        this.players = new HashMap<>();
        this.availableGodPowers = new Vector<>();
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
        if(currentStatus == GameStatus.INGAME && currentPlayer.getCurrentStatus() != PlayerStatus.WAITING4INIT) {
            currentPlayer.getCosplayer().checkWin();
            if (currentPlayer.getCurrentStatus() == PlayerStatus.LOSE)
                toNextPlayer();
        }
        currentPlayer.getCosplayer().prepare();
    }

    public void toNextPlayer(int playerId) {
        currentPlayer = players.get(playerId);
    }

    public Map<Integer,Player> getPlayers() {
        return players;
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

    public Vector<GodPower> getAvailableGodPowers(){
        return availableGodPowers;
    }

    public void addAvailableGodPowers(GodPower godPowers){
        availableGodPowers.add(godPowers);
    }

    public GameStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(GameStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
