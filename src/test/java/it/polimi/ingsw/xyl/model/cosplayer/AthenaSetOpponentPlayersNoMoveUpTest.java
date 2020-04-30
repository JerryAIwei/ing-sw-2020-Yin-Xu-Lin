package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Level;
import it.polimi.ingsw.xyl.model.Player;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static org.junit.Assert.*;

/**
 * it's a test of Athena godpower interacts
 * with Prometheus and Apollo.
 *
 * Athena's power:
 * if one of your workers moved up on your last turn,
 * opponent workers cannot move up this turn.
 * @author Lin Xin
 */
public class AthenaSetOpponentPlayersNoMoveUpTest extends GodCosplayerTest {
    Cosplayer cosplayerE;
    Player playerE;

    @Before
    public void setUp() {
        super.setUp();

        playerE = new Player(2, "playerE");
        gameBoard.addPlayer(playerE);
        playerE.setCurrentGameBoard(gameBoard);

        cosplayerE = new Cosplayer(playerE);
        playerE.setCosplayer(cosplayerE);
        playerE.setWorkers(3, 0, 4, 2);
        islandBoard.getSpaces()[3][0].setOccupiedBy(playerE.getPlayerId() * 10);
        islandBoard.getSpaces()[4][2].setOccupiedBy(playerE.getPlayerId() * 10 + 1);
        /*
        | 0 | 0 | 0 | 0 | 0 |
        | 0 | 0 | 0 | 0 | 0 |
        | 0 |D'B| 0 | 0 |E'B|
        | 0 |C'B|D'A| 0 | 0 |
        |C'A| 0 | 0 |E'A| 0 |
        */

        Athena athena = new Athena(playerC);
        playerC.setCosplayer(athena);
        Prometheus prometheus = new Prometheus(playerD);
        playerD.setCosplayer(prometheus);
        Apollo apollo = new Apollo(playerE);
        playerE.setCosplayer(apollo);
    }

    /* general test */

    @Test
    public void AthenaSetOpponentPlayersNoMoveUpTest_playerCWorkerAMoveRight_noLevelUp() {
        playerC.getCosplayer().move(0, RIGHT);
        assertFalse(gameBoard.getIslandBoard().isNoLevelUp());
        assertEquals(playerC.getWorkers()[0].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[1][0].isOccupiedBy(), playerC.getPlayerId() * 10);
    }

    @Test(expected=RuntimeException.class)
    public void AthenaSetOpponentPlayersNoMoveUpTest_playerCWorkerAMoveLeft_notAllowed() {
        playerC.getCosplayer().move(0, LEFT);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(), playerC.getPlayerId() * 10);
    }

    @Test
    public void AthenaSetOpponentPlayersNoMoveUpTest_playerCWorkerAMoveUp_noLevelUp() {
        playerC.getCosplayer().move(0, UP);
        assertFalse(gameBoard.getIslandBoard().isNoLevelUp());
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(), playerC.getPlayerId() * 10);
    }

    /* GodPower Mod test */

    @Test
    public void AthenaSetOpponentPlayersNoMoveUpTest_playerCWorkerAMoveUp_levelUp() {
        gameBoard.getIslandBoard().getSpaces()[0][1].setLevel(Level.LEVEL1);
        playerC.getCosplayer().move(0, UP);
        assertTrue(playerD.getCurrentGameBoard().getIslandBoard().isNoLevelUp());
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(), playerC.getPlayerId() * 10);
    }
}