package it.polimi.ingsw.xyl.model.cosplayer;


import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.*;
import static org.junit.Assert.assertEquals;

/**
 *  it's a test of cosplayer building(Demeter)
 *  Demeter's build: worker may build one additional time,
 *  but not on the same space.
 *
 *  @author Shaoxun
 */
public class DemeterBuildTest extends GodCosplayerTest{

    @Before
    public void setUp() {
        super.setUp();
        Demeter demeter = new Demeter(playerC);
        playerC.setCosplayer(demeter);
    }

    /* general test */

    @Test
    public void DemeterBuildTest_playerCWorkerABuildRight_normal(){
        islandBoard.getSpaces()[1][0].setLevel(LEVEL1);
        playerC.getCosplayer().only_for_test_setWorkerInAction(0);
        playerC.getCosplayer().build(0, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[1][0].getLevel(),LEVEL2);
    }

    @Test
    public void DemeterBuildTest_playerCWorkerBBuildRight_occupied_2_1_notAllowed(){
        playerC.getCosplayer().only_for_test_setWorkerInAction(1);
        playerC.getCosplayer().build(1, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[2][1].getLevel(),GROUND);
    }

    /* GodPower Mod test */

    @Test
    public void DemeterBuildTest_playerCWorkerAMoveRightBuildRightThenBuildLeft_usePower(){
        playerC.getCosplayer().move(0,RIGHT);
        islandBoard.getSpaces()[2][0].setLevel(LEVEL1);
        playerC.getCosplayer().build(0, RIGHT, false);
        playerC.getCosplayer().build(0, LEFT, false);
        assertEquals(islandBoard.getSpaces()[2][0].getLevel(),LEVEL2);
        assertEquals(islandBoard.getSpaces()[0][0].getLevel(),LEVEL1);
    }

    @Test
    public void DemeterBuildTest_playerCWorkerABuildRightThenBuildRight_notAllowed(){
        playerC.getCosplayer().move(0, RIGHT);
        islandBoard.getSpaces()[2][0].setLevel(LEVEL1);
        playerC.getCosplayer().build(0, RIGHT, false);
        playerC.getCosplayer().build(0, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[2][0].getLevel(),LEVEL2);
    }

}
