package it.polimi.ingsw.xyl.model;

import it.polimi.ingsw.xyl.model.cosplayer.*;

import java.io.Serializable;

/**
 * All simple God cards except Hermes.
 */
public enum GodPower implements Serializable  {
    ANONYMOUS{
        @Override
        public String toString(){
            return "Anonymous";
        }

        @Override
        public String description(){
            return "This is a normal game without God Powers,\n" +
                    "\tyou are a normal player.";
        }
    },
    APOLLO{
        @Override
        public String toString(){
            return "Apollo";
        }

        @Override
        public String description(){
            return "Your worker may move into an opponent worker's\n" +
                    "\tspace by forcing their worker to the space to the\n" +
                    "\tspace yours just vacated.";
        }
    },
    ARTEMIS{
        @Override
        public String toString(){
            return "Artemis";
        }

        @Override
        public String description(){
            return "Your worker may move one additional time,\n" +
                    "\tbut not back to its initial space.";
        }
    },
    ATHENA{
        @Override
        public String toString(){
            return "Athena";
        }

        @Override
        public String description(){
            return "If one of your workers moved up(means to\n" +
                    "\ta higher level) on your last turn,\n" +
                    "\topponent workers cannot move up this turn.";
        }
    },
    ATLAS{
        @Override
        public String toString(){
            return "Atlas";
        }

        @Override
        public String description(){
            return "Your worker may build a dome at any level.";
        }
    },
    DEMETER{
        @Override
        public String toString(){
            return "Demeter";
        }

        @Override
        public String description(){
            return "Your worker may build one additional time,\n" +
                    "\tbut not on the same space.";
        }
    },
    HEPHAESTUS{
        @Override
        public String toString(){
            return "Hephaestus";
        }

        @Override
        public String description(){
            return "Your worker may build one additional\n" +
                    "\tblock(not dome) on top of your first block.";
        }
    },
    MINOTAUR{
        @Override
        public String toString(){
            return "Minotaur";
        }

        @Override
        public String description(){
            return "Your worker may move into an opponent\n" +
                    "\tworker's space, if their worker can be\n" +
                    "\tforced one space straight backwards to\n" +
                    "\tan unoccupied space at any level.";
        }
    },
    PAN{
        @Override
        public String toString(){
            return "Pan";
        }

        @Override
        public String description(){
            return "You also win if your worker moves down two or more levels.";
        }
    },
    PROMETHEUS{
        @Override
        public String toString(){
            return "Prometheus";
        }

        @Override
        public String description(){
            return "If your worker does not move up(means\n" +
                    "\tto a higher level), it may build both\n" +
                    "\tbefore and after moving.";
        }
    };

    public abstract String description();

    public Cosplayer cosplay(Player player){
        switch(this){
            case APOLLO: return new Apollo(player);
            case ARTEMIS: return new Artemis(player);
            case ATHENA: return new Athena(player);
            case ATLAS: return new Atlas(player);
            case DEMETER: return new Demeter(player);
            case HEPHAESTUS: return new Hephaestus(player);
            case MINOTAUR: return new Minotaur(player);
            case PAN: return new Pan(player);
            case PROMETHEUS: return new Prometheus(player);
            default: return new Cosplayer(player);
        }
    }

}
