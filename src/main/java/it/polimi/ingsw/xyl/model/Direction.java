package it.polimi.ingsw.xyl.model;

/**
 * This class represents all possible directions
 * that a worker could move in and build blocks in.
 *
 * @author Shaoxun
 */
public enum Direction {
    UP {
        @Override
        public String toString(){
          return "Up";
        }

        @Override
        public String toSymbol() {
            return "↑";
        }

        @Override
        public int[] toMarginalPosition() {
            return new int[]{0, 1};
        }

        @Override
        public Direction toOpposite() {
            return DOWN;
        }
    },
    DOWN {
        @Override
        public String toString(){
            return "Down";
        }

        @Override
        public String toSymbol() {
            return "↓";
        }

        @Override
        public int[] toMarginalPosition() {
            return new int[]{0, -1};
        }

        @Override
        public Direction toOpposite() {
            return UP;
        }
    },
    LEFT {
        @Override
        public String toString(){
            return "Left";
        }

        @Override
        public String toSymbol() {
            return "←";
        }

        @Override
        public int[] toMarginalPosition() {
            return new int[]{-1, 0};
        }

        @Override
        public Direction toOpposite() {
            return RIGHT;
        }
    },
    RIGHT {
        @Override
        public String toString(){
            return "Right";
        }

        @Override
        public String toSymbol() {
            return "→";
        }

        @Override
        public int[] toMarginalPosition() {
            return new int[]{1, 0};
        }

        @Override
        public Direction toOpposite() {
            return LEFT;
        }
    },
    UPLEFT {
        @Override
        public String toString(){
            return "Upper left";
        }

        @Override
        public String toSymbol() {
            return "↖";
        }

        @Override
        public int[] toMarginalPosition() {
            return new int[]{-1, 1};
        }

        @Override
        public Direction toOpposite() {
            return DOWNRIGHT;
        }
    },
    UPRIGHT {
        @Override
        public String toString(){
            return "Upper right";
        }

        @Override
        public String toSymbol() {
            return "↗";
        }

        @Override
        public int[] toMarginalPosition() {
            return new int[]{1, 1};
        }

        @Override
        public Direction toOpposite() {
            return DOWNLEFT;
        }
    },
    DOWNLEFT {
        @Override
        public String toString(){
            return "Lower left";
        }

        @Override
        public String toSymbol() {
            return "↙";
        }

        @Override
        public int[] toMarginalPosition() {
            return new int[]{-1, -1};
        }

        @Override
        public Direction toOpposite() {
            return UPRIGHT;
        }
    },
    DOWNRIGHT {
        @Override
        public String toString(){
            return "Lower right";
        }

        @Override
        public String toSymbol() {
            return "↘";
        }

        @Override
        public int[] toMarginalPosition() {
            return new int[]{1, -1};
        }

        @Override
        public Direction toOpposite() {
            return UPLEFT;
        }
    };

    /**
     * Direction to relative margin of the worker
     *
     * @return an array MP, MP[0] represents the horizontal margin
     * and MP[1] represents the vertical margin.
     */
    public abstract int[] toMarginalPosition();

    /**
     * Use symbols to represent the directions
     * ← ↑ → ↓ ↖ ↗ ↘ ↙
     */
    public abstract String toSymbol();

    public abstract Direction toOpposite();
}
