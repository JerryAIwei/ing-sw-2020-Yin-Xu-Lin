package it.polimi.ingsw.xyl.model.message;

import it.polimi.ingsw.xyl.model.Direction;

public class MoveMessage extends RoleControllerMessage {
    private final int workerId;
    private final Direction direction;

    public MoveMessage(int gameId, int playerId, int worker, Direction direction) {
        super(gameId, playerId);
        this.workerId = worker;
        this.direction = direction;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public Direction getDirection() {
        return direction;
    }
}
