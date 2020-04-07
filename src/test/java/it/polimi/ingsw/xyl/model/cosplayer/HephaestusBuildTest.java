package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.IslandBoard;
import it.polimi.ingsw.xyl.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
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
        playerC.getCosplayer().move(0, RIGHT);
        islandBoard.getSpaces()[2][0].setLevel(LEVEL1);
        playerC.getCosplayer().build(0, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[2][0].getLevel(),LEVEL2);
    }

    @Test
    public void HephaestusBuildTest_playerCWorkerBBuildRight_occupied_0_0_notAllowed(){
        playerC.getCosplayer().move(1, DOWN);
        playerC.getCosplayer().build(1, LEFT, false);
        assertEquals(islandBoard.getSpaces()[0][0].getLevel(),GROUND);
    }

    /* GodPower Mod test */

    @Test
    public void HephaestusBuildTest_playerCWorkerABuildRight_usePower(){
        playerC.getCosplayer().move(0, RIGHT);
        islandBoard.getSpaces()[2][0].setLevel(LEVEL1);
        playerC.getCosplayer().build(0, RIGHT, false);
        playerC.getCosplayer().build(0, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[2][0].getLevel(),LEVEL3);
    }

    @Test
    public void HephaestusBuildTest_playerCWorkerABuildRight_usePowerButDome_notAllowed(){
        playerC.getCosplayer().move(0, RIGHT);
        islandBoard.getSpaces()[2][0].setLevel(LEVEL2);
        playerC.getCosplayer().build(0, RIGHT, false);
        playerC.getCosplayer().build(0, RIGHT, false); // dome, not allowed
        assertEquals(islandBoard.getSpaces()[2][0].getLevel(),LEVEL3);
    }

    @Test
    public void HephaestusBuildTest_playerCWorkerABuildRight_differentWorker_notAllowed(){
        playerC.getCosplayer().move(0, RIGHT);
        islandBoard.getSpaces()[2][0].setLevel(LEVEL2);
        playerC.getCosplayer().build(0, RIGHT, false);
        playerC.getCosplayer().build(1, LEFT, false); // different worker, not allowed
        assertEquals(islandBoard.getSpaces()[2][0].getLevel(),LEVEL3);
        assertEquals(islandBoard.getSpaces()[0][1].getLevel(),GROUND);
    }

    @Test
    public void HephaestusBuildTest_playerCWorkerABuildRightThenBuildLeft__notAllowed(){
        playerC.getCosplayer().move(0, RIGHT);
        islandBoard.getSpaces()[2][0].setLevel(LEVEL1);
        playerC.getCosplayer().build(0, RIGHT, false);
        playerC.getCosplayer().build(0, LEFT, false); // different to first build, not allowed
        assertEquals(islandBoard.getSpaces()[2][0].getLevel(),LEVEL2);
        assertEquals(islandBoard.getSpaces()[0][0].getLevel(),GROUND);
    }
}
