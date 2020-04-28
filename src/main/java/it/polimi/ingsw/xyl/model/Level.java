package it.polimi.ingsw.xyl.model;

/**
 * Different levels of a specific space
 *
 * @author Shaoxun
 */
public enum Level {
    GROUND { // the ground

        @Override
        public int toInt() {
            return 0;
        }
    },
    LEVEL1 { // the first level block

        @Override
        public int toInt() {
            return 1;
        }
    },
    LEVEL2 { // the second level block

        @Override
        public int toInt() {
            return 2;
        }
    },
    LEVEL3 {// the third level block

        @Override
        public int toInt() {
            return 3;
        }
    },
    DOME { // usually a dome can only be placed on the top of LEVEL3 blocks

        @Override
        public int toInt() {
            return 4;
        }
    };

    public abstract int toInt();
}
