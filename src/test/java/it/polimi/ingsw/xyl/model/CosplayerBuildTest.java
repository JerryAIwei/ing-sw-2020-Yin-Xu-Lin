package it.polimi.ingsw.xyl.model;

import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.*;
import static org.junit.Assert.assertEquals;

/**
 *  it's a test of cosplayer building(without GodPower currently)
 *
 *  @author Shaoxun
 */
public class CosplayerBuildTest extends CosplayerTest{

    @Test
    public void CosplayerBuildTest_playerAWorkerABuildRight_LEVEL1_1_0(){
        playerA.getCosplayer().only_for_test_setWorkerInAction(0);
        playerA.getCosplayer().build(0, RIGHT,false);
        assertEquals(islandBoard.getSpaces()[1][0].getLevel(),LEVEL1);
    }

    @Test
    public void CosplayerBuildTest_playerAWorkerABuildUpRight_NoChange() {
        playerA.getCosplayer().only_for_test_setWorkerInAction(0);
        playerA.getCosplayer().build(0, UPRIGHT, false);
        assertEquals(islandBoard.getSpaces()[1][0].getLevel(),GROUND);
    }

    @Test
    public void CosplayerBuildTest_playerBWorkerBBuildDownLeft_NoChange() {
        playerB.getCosplayer().only_for_test_setWorkerInAction(1);
        playerB.getCosplayer().build(1, DOWNLEFT, false);
        assertEquals(islandBoard.getSpaces()[3][3].getLevel(),GROUND);
    }

    @Test
    public void CosplayerBuildTest_playerBWorkerBBuildDown_dome_4_3() {
        islandBoard.getSpaces()[4][3].setLevel(DOME);
        playerB.getCosplayer().only_for_test_setWorkerInAction(1);
        playerB.getCosplayer().build(1, DOWN, false);
        assertEquals(islandBoard.getSpaces()[4][3].getLevel(),DOME);
    }

    @Test (expected = Exception.class)
    public void CosplayerBuildTest_playerAWorkerABuildLeft_Error() {
        playerA.getCosplayer().only_for_test_setWorkerInAction(0);
        playerA.getCosplayer().build(0, LEFT, false);
    }

}
