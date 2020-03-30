package it.polimi.ingsw.xyl.model.message;

import it.polimi.ingsw.xyl.model.Direction;

public class BuildMessage extends RoleControllerMessage {
    private int workerId;
    private Direction direction;
    private boolean buildDome;

    public BuildMessage(int gameId, int playerId, int worker, Direction direction, boolean buildDome) {
        super(gameId, playerId);
        this.workerId = worker;
        this.direction = direction;
        this.buildDome = buildDome;
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

    public boolean isBuildDome() {
        return buildDome;
    }
}
