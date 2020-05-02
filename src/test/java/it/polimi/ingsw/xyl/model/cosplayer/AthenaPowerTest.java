package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Level;
import it.polimi.ingsw.xyl.model.Player;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.DOME;
import static it.polimi.ingsw.xyl.model.Level.LEVEL2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * it's a test of Athena godpower
 * Athena's power:
 * if one of your workers moved up on your last turn,
 * opponent workers cannot move up this turn.
 *
 * @author Lin Xin
 * @author Shaoxun
 */
public class AthenaPowerTest extends GodCosplayerTest {
    Cosplayer cosplayerE;
    Player playerE;

    @Before
    public void setUp() {
        super.setUp();
        // C : Athena
        Athena athena = new Athena(playerC);
        playerC.setCosplayer(athena);
        // D : Prometheus
        Prometheus prometheus = new Prometheus(playerD);
        playerD.setCosplayer(prometheus);
        // E : Apollo
        playerE = new Player(2, "playerE");
        gameBoard.addPlayer(playerE);
        playerE.setCurrentGameBoard(gameBoard);
        Apollo apollo = new Apollo(playerE);
        playerE.setCosplayer(apollo);
        playerE.setWorkers(2, 0, 4, 2);
        islandBoard.getSpaces()[3][0].setOccupiedBy(playerE.getPlayerId() * 10);
        islandBoard.getSpaces()[4][2].setOccupiedBy(playerE.getPlayerId() * 10 + 1);
        /*
        | 0 | 0 | 0 | 0 | 0 |
        | 0 | 0 | 0 | 0 | 0 |
        | 0 |D'B| 0 | 0 |E'B|
        | 0 |C'B|D'A| 0 | 0 |
        |C'A| 0 | 0 |E'A| 0 |
        */


    }

    /* general test */

    @Test
    public void AthenaPowerTest_playerCWorkerBMoveDown_normalMove(){
        playerC.getCosplayer().move(1, DOWN);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[1][0].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);
        assertEquals(playerC.getCosplayer().getNextAction(),"BUILD");
    }

    @Test(expected=RuntimeException.class)
    public void AthenaPowerTest_playerCWorkerAMoveUp_dome_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(DOME);
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(),playerC.getPlayerId() * 10);
    }

    @Test(expected=RuntimeException.class)
    public void AthenaPowerTest_playerCWorkerAMoveUp_level2_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(LEVEL2);
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(),-1);  //not occupied
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(),playerC.getPlayerId() * 10); //playerC's workerA
    }

    /* GodPower Mod test */

    @Test
    public void AthenaPowerTest_playerCWorkerALevelUp_setNoLevelUp(){
        islandBoard.getSpaces()[0][1].setLevel(Level.LEVEL1);
        playerC.getCosplayer().move(0, UP);
        assertTrue(islandBoard.isNoLevelUp());
    }
}

