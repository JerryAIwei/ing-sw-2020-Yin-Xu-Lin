package it.polimi.ingsw.xyl.model;

/**
 * It represents the player of the Santorini game.
 *
 * @author Shaoxun
 */
public class Player {
    private final int playerId;
    private final String playerName;
    private GameBoard currentGameBoard;
    private Cosplayer cosplayer;
    private Worker[] workers;
    private PlayerStatus currentStatus = PlayerStatus.INLOBBY;

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

    public Worker[] getWorkers() {
        return workers;
    }

    public void setWorkers(int xOfWorkerA, int yOfWorkerA, int xOfWorkerB, int yOfWorkerB){
        Worker workerA = new Worker(xOfWorkerA,yOfWorkerA);
        Worker workerB = new Worker(xOfWorkerB,yOfWorkerB);
        this.workers = new Worker[]{workerA, workerB};
        this.currentStatus = PlayerStatus.WORKERPREPARED;
    }

    public PlayerStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(PlayerStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
