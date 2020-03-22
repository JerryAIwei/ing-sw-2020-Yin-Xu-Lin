package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Player;

import static it.polimi.ingsw.xyl.model.GodPower.PAN;

public class Pan extends Cosplayer {
    public Pan(Player player) {
        super(player);
        super.godPower = PAN;
    }
    // Pan doesn't affect the move and build method.
    // do nothing here
}
