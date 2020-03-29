package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.IslandBoard;
import it.polimi.ingsw.xyl.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.RIGHT;
import static it.polimi.ingsw.xyl.model.Direction.UP;
import static it.polimi.ingsw.xyl.model.Level.*;
import static org.junit.Assert.assertEquals;


/**
 *  it's a test of cosplayer building(Hephaestus)
 *  Hephaestus' build: worker may build one additional block(not dome)
 *  on top of your first block.
 *
 *  @author Shaoxun
 */
public class HephaestusBuildTest extends GodCosplayerTest{

    @Before
    public void setUp() {
        super.setUp();
        Hephaestus hephaestus = new Hephaestus(playerC);
        playerC.setCosplayer(hephaestus);
    }

    /* general test */

    @Test
    public void HephaestusBuildTest_playerCWorkerABuildRight_normal(){
        islandBoard.getSpaces()[1][0].setLevel(LEVEL1);
        playerC.getCosplayer().build('A', RIGHT, false);
        assertEquals(islandBoard.getSpaces()[1][0].getLevel(),LEVEL2);
    }

    @Test
    public void HephaestusBuildTest_playerCWorkerBBuildRight_occupied_2_1_notAllowed(){
        playerC.getCosplayer().build('B', RIGHT, false);
        assertEquals(islandBoard.getSpaces()[2][1].getLevel(),GROUND);
    }

    /* GodPower Mod test */

    @Test
    public void HephaestusBuildTest_playerCWorkerABuildRight_usePower(){
        islandBoard.getSpaces()[1][0].setLevel(LEVEL1);
        playerC.getCosplayer().build('A', RIGHT, false);
        playerC.getCosplayer().build('A', RIGHT, false);
        assertEquals(islandBoard.getSpaces()[1][0].getLevel(),LEVEL3);
    }

    @Test
    public void HephaestusBuildTest_playerCWorkerABuildRight_usePowerButDome_notAllowed(){
        islandBoard.getSpaces()[1][0].setLevel(LEVEL2);
        playerC.getCosplayer().build('A', RIGHT, false);
        playerC.getCosplayer().build('A', RIGHT, false); // dome, not allowed
        assertEquals(islandBoard.getSpaces()[1][0].getLevel(),LEVEL3);
    }

    @Test
    public void HephaestusBuildTest_playerCWorkerABuildRightThenBuildUp__notAllowed(){
        islandBoard.getSpaces()[1][0].setLevel(LEVEL1);
        playerC.getCosplayer().build('A', RIGHT, false);
        playerC.getCosplayer().build('A', UP, false); // different to first build, not allowed
        assertEquals(islandBoard.getSpaces()[1][0].getLevel(),LEVEL2);
        assertEquals(islandBoard.getSpaces()[0][1].getLevel(),GROUND);
    }
}
