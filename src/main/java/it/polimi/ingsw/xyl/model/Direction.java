package it.polimi.ingsw.xyl.model;

/**
 *  This class represents all possible directions
 *  that a worker could move in and build blocks in.
 *
 *  @author Shaoxun
 */
public enum Direction {
    UP, DOWN, LEFT, RIGHT, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT;

    /**
     * Direction to relative margin of the worker
     *
     * @return an array MP, MP[0] represents the horizontal margin
     * and MP[1] represents the vertical margin.
     */
    public int[] toMarginalPosition(){
        switch (this) {
            case UP:
                return new int[]{0, 1};
            case DOWN:
                return new int[]{0, -1};
            case LEFT:
                return new int[]{-1, 0};
            case RIGHT:
                return new int[]{1, 0};
            case UPLEFT:
                return new int[]{-1, 1};
            case UPRIGHT:
                return new int[]{1, 1};
            case DOWNLEFT:
                return new int[]{-1, -1};
            case DOWNRIGHT:
                return new int[]{1, -1};
            default:
                return new int[]{0, 0};
        }
    }

    public Direction toOpposite(){
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case UPLEFT:
                return DOWNRIGHT;
            case UPRIGHT:
                return DOWNLEFT;
            case DOWNLEFT:
                return UPRIGHT;
            case DOWNRIGHT:
                return UPLEFT;
            default:
                return null;
        }
    }
}
