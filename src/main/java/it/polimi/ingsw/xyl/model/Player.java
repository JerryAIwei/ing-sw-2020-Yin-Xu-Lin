package it.polimi.ingsw.xyl.model;

/**
 * It represents the player of the Santorini game.
 *
 * @author Shaoxun
 */
public class Player {
    private int playerId;
    private String playerName;
    private GameBoard currentGameBoard;
    private Cosplayer cosplayer;
    private int[] workerAPosition;
    private int[] workerBPosition;
    private PlayerStatus currentStatus;

    public Player(int playerId, String playerName){
        this.playerId = playerId;
        this.playerName = playerName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public GameBoard getCurrentGameBoard() {
        return currentGameBoard;
    }

    public void setCurrentGameBoard(GameBoard currentGameBoard) {
        this.currentGameBoard = currentGameBoard;
    }

    public Cosplayer getCosplayer() {
        return cosplayer;
    }

    public void setCosplayer(Cosplayer cosplayer) {
        this.cosplayer = cosplayer;
    }

    public int[] getWorkerPosition(Character ab){
        if(ab =='A')
            return workerAPosition;
        else if(ab =='B')
            return workerBPosition;
        else
            throw new IllegalArgumentException("Worker should be A or B!");
    }

    public void setWorkerPosition(Character ab, int[] workerPosition) {
        if(ab =='A')
            this.workerAPosition = workerPosition;
        else if(ab =='B')
            this.workerBPosition = workerPosition;
        else
            throw new IllegalArgumentException("Worker should be A or B!");
    }

    public PlayerStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(PlayerStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
