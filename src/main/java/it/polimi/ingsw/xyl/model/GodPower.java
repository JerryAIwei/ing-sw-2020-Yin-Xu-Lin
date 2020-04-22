package it.polimi.ingsw.xyl.model;

import it.polimi.ingsw.xyl.model.cosplayer.*;

import java.io.Serializable;

/**
 * All simple God cards except Hermes.
 */
public enum GodPower implements Serializable  {
    APOLLO, ARTEMIS, ATHENA, ATLAS, DEMETER, HEPHAESTUS, MINOTAUR, PAN, PROMETHEUS;

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
