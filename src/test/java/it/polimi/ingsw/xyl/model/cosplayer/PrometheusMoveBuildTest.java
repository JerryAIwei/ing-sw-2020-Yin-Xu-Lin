package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.Level;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * it's a test of cosplayer moving and building (Prometheus)
 * Prometheus' move and build:
 * If your worker does not move up(means to a higher level),
 * it may build both before and after moving.
 *
 * @author Lin Xin
 */
public class PrometheusMoveBuildTest extends GodCosplayerTest {

    @Before
    public void setUp() {
        super.setUp();
        Prometheus prometheus = new Prometheus(playerD);
        playerD.setCosplayer(prometheus);
    }

    /* general test */

    @Test
    public void PrometheusMoveBuildTest_playerDWorkerA_MoveUp_BuildRight_normal() {
        playerD.getCosplayer().move(0, UP);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 2);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[2][2].isOccupiedBy(), playerD.getPlayerId() * 10);

        playerD.getCosplayer().build(0, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[3][2].getLevel(), LEVEL1);
    }

    @Test
    public void PrometheusMoveBuildTest_playerDWorkerA_MoveUp_BuildLeft_Occupied_notAllowed() {
        playerD.getCosplayer().move(0, UP);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 2);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[2][2].isOccupiedBy(), playerD.getPlayerId() * 10);

        playerD.getCosplayer().build(0, LEFT, false);
        assertEquals(islandBoard.getSpaces()[2][3].getLevel(), GROUND);
    }

    @Test
    public void PrometheusMoveBuildTest_playerDWorkerA_MoveLeft_Occupied_notAllowed() {
        playerD.getCosplayer().move(0, LEFT);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(), playerC.getPlayerId() * 10 + 1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), playerD.getPlayerId() * 10);
    }

    /* GodPower Mod test */

    @Test
    public void PrometheusMoveBuildTest_playerDWorkerA_BuildUp_MoveRight_BuildLeft_normal() {
        playerD.getCosplayer().build(0, UP, false);
        assertEquals(islandBoard.getSpaces()[2][2].getLevel(), LEVEL1);

        playerD.getCosplayer().move(0, RIGHT);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 3);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedBy(), playerD.getPlayerId() * 10);

        playerD.getCosplayer().build(0, LEFT, false);
        assertEquals(islandBoard.getSpaces()[2][1].getLevel(), LEVEL1);
    }

    @Test
    public void PrometheusMoveBuildTest_playerDWorkerA_BuildRight_Dome_notAllowed() {
        islandBoard.getSpaces()[3][1].setLevel(DOME);
        playerD.getCosplayer().build(0, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[3][1].getLevel(), DOME);
    }

    @Test
    public void PrometheusMoveBuildTest_playerDWorkerA_BuildUp_MoveUp_notAllowed() {
        playerD.getCosplayer().build(0, UP, false);
        assertEquals(islandBoard.getSpaces()[2][2].getLevel(), LEVEL1);

        playerD.getCosplayer().move(0, UP);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[2][2].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), playerD.getPlayerId() * 10);
    }

    @Test
    public void PrometheusMoveBuildTest_playerDWorkerA_BuildRight_MoveRight_UpOneMoreLevel_notAllowed() {
        islandBoard.getSpaces()[3][1].setLevel(LEVEL1);
        playerD.getCosplayer().build(0, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[3][1].getLevel(), LEVEL2);

        playerD.getCosplayer().move(0, RIGHT);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), playerD.getPlayerId() * 10);
    }

    @Test
    public void PrometheusMoveBuildTest_playerDWorkerA_BuildUp_MoveLeft_Occupied_notAllowed() {
        playerD.getCosplayer().build(0, UP, false);
        assertEquals(islandBoard.getSpaces()[2][2].getLevel(), LEVEL1);

        playerD.getCosplayer().move(0, LEFT);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(), playerC.getPlayerId() * 10 + 1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), playerD.getPlayerId() * 10);
    }

    @Test
    public void PrometheusMoveBuildTest_playerDWorkerA_BuildRight_MoveRight_Dome_notAllowed() {
        islandBoard.getSpaces()[3][1].setLevel(LEVEL3);
        playerD.getCosplayer().build(0, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[3][1].getLevel(), DOME);

        playerD.getCosplayer().move(0, RIGHT);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), playerD.getPlayerId() * 10);
    }

    @Test
    public void PrometheusMoveBuildTest_playerDWorkerA_BuildUp_MoveRight_BuildUpLeft_Dome_notAllowed() {
        islandBoard.getSpaces()[2][2].setLevel(LEVEL3);
        playerD.getCosplayer().build(0, UP, false);
        assertEquals(islandBoard.getSpaces()[2][2].getLevel(), DOME);

        playerD.getCosplayer().move(0, RIGHT);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 3);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedBy(), playerD.getPlayerId() * 10);

        playerD.getCosplayer().build(0, UPLEFT, false);
        assertEquals(islandBoard.getSpaces()[2][2].getLevel(), DOME);
    }
}

