package it.polimi.ingsw.xyl.model.message;

public class SetInitialWorkerPositionMessage extends Message {
    private final int playerId;
    private final int workerAX;
    private final int workerAY;
    private final int workerBX;
    private final int workerBY;

    public SetInitialWorkerPositionMessage(int gameId, int playerId, int ax, int ay, int bx, int by) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.workerAX = ax;
        this.workerAY = ay;
        this.workerBX = bx;
        this.workerBY = by;
    }

    public int getGameId(){
        return gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getWorkerPosition(int worker, Character xy) {
        if (worker == 0) {
            if (xy == 'x') {
                return workerAX;
            } else if (xy == 'y') {
                return workerAY;
            }
        } else if (worker == 1) {
            if (xy == 'x') {
                return workerBX;
            } else if (xy == 'y') {
                return workerBY;
            }
        }
        return -1;
    }
}
