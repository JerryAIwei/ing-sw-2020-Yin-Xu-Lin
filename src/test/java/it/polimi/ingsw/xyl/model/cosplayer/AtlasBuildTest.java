package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.IslandBoard;
import it.polimi.ingsw.xyl.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.RIGHT;
import static it.polimi.ingsw.xyl.model.Level.*;
import static org.junit.Assert.assertEquals;

/**
 *  it's a test of cosplayer building(Atlas)
 *  Atlas' build: worker may build a dome at any level.
 *
 *  @author Shaoxun
 */
public class AtlasBuildTest extends GodCosplayerTest{

    @Before
    public void setUp() {
        super.setUp();
        Atlas atlas = new Atlas(playerC);
        playerC.setCosplayer(atlas);
    }

    /* general test */

    @Test
    public void AtlasBuildTest_playerCWorkerABuildRight_normal(){
        islandBoard.getSpaces()[1][0].setLevel(LEVEL1);
        playerC.getCosplayer().only_for_test_setWorkerInAction(0);
        playerC.getCosplayer().build(0, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[1][0].getLevel(),LEVEL2);
    }

    @Test(expected=RuntimeException.class)
    public void AtlasBuildTest_playerCWorkerBBuildUpRight_occupied_2_1_notAllowed() {
        playerC.getCosplayer().only_for_test_setWorkerInAction(1);
        playerC.getCosplayer().build(1, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[2][1].getLevel(),GROUND);
    }

    @Test(expected=RuntimeException.class)
    public void AtlasBuildTest_playerCWorkerBBuildUpRight_dome_2_1_notAllowed() {
        islandBoard.getSpaces()[2][1].setLevel(DOME);
        playerC.getCosplayer().only_for_test_setWorkerInAction(1);
        playerC.getCosplayer().build(1, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[2][1].getLevel(),DOME);
    }

    /* GodPower Mod test */

    @Test
    public void AtlasBuildTest_playerCWorkerABuildRightWithDome_usePower(){
        islandBoard.getSpaces()[1][0].setLevel(LEVEL1);
        playerC.getCosplayer().only_for_test_setWorkerInAction(0);
        playerC.getCosplayer().build(0, RIGHT, true);
        assertEquals(islandBoard.getSpaces()[1][0].getLevel(),DOME);
    }

}
