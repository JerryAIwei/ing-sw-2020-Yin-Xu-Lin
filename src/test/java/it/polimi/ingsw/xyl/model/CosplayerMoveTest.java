package it.polimi.ingsw.xyl.model;

import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.*;
import static org.junit.Assert.assertEquals;

/**
 *  it's a test of cosplayer moving(without GodPower currently)
 *
 *  @author Shaoxun
 */
public class CosplayerMoveTest extends CosplayerTest{

    @Test
    public void CosplayerMoveTest_playerAWorkerAMoveRight_1_0() {
        playerA.getCosplayer().move(0, RIGHT);
        assertEquals(playerA.getWorkers()[0].getPositionX(), 1);
        assertEquals(playerA.getWorkers()[0].getPositionY(), 0);
    }

    @Test(expected=RuntimeException.class)
    public void CosplayerMoveTest_playerAWorkerAMoveLeft_notAvailable() {
        playerA.getCosplayer().move(0, LEFT);
        // got a println "Your move is not available!"
    }

    @Test
    public void CosplayerMoveTest_playerBWorkerAMoveRight_4_3() {
        playerB.getCosplayer().move(0, RIGHT);
        assertEquals(playerB.getWorkers()[0].getPositionX(), 4);
        assertEquals(playerB.getWorkers()[0].getPositionY(), 3);
    }

    @Test
    public void CosplayerMoveTest_playerAWorkerBMoveUP_1_2() {
        playerA.getCosplayer().move(1, UP);
        assertEquals(playerA.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerA.getWorkers()[1].getPositionY(), 2);
    }

    @Test
    public void CosplayerMoveTest_playerAWorkerBMoveLeft_0_1() {
        playerA.getCosplayer().move(1, LEFT);
        assertEquals(playerA.getWorkers()[1].getPositionX(), 0);
        assertEquals(playerA.getWorkers()[1].getPositionY(), 1);
    }

    @Test(expected=RuntimeException.class)
    public void CosplayerMoveTest_playerAWorkerBMoveLeft_occupied_0_1_notAllowed() {
        islandBoard.getSpaces()[0][1].setOccupiedBy(1);
        playerA.getCosplayer().move(1, LEFT);
        assertEquals(playerA.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerA.getWorkers()[1].getPositionY(), 1);
    }

    @Test(expected=RuntimeException.class)
    public void CosplayerMoveTest_playerAWorkerBMoveLeft_dome_0_1_notAllowed() {
        islandBoard.getSpaces()[0][1].setLevel(DOME);
        playerA.getCosplayer().move(1, LEFT);
        assertEquals(playerA.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerA.getWorkers()[1].getPositionY(), 1);
    }

    @Test(expected=RuntimeException.class)
    public void CosplayerMoveTest_playerAWorkerBMoveLeft_LEVEL2_0_1_notAllowed() {
        islandBoard.getSpaces()[0][1].setLevel(LEVEL2);
        playerA.getCosplayer().move(1, LEFT);
        assertEquals(playerA.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerA.getWorkers()[1].getPositionY(), 1);
    }
}
