package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.Player;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.LEVEL1;
import static org.junit.Assert.assertEquals;

/**
 * it's a test of Athena godpower interacts
 * with Prometheus and Apollo.
 *
 * Athena's power:
 * if one of your workers moved up on your last turn,
 * opponent workers cannot move up this turn.
 * @author Lin Xin
 */
public class AthenaSetOpponentPlayersNoMoveUpTestWithPrometheusAndApollo extends GodCosplayerTest {
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

        Athena athena = new Athena(playerC);
        playerC.setCosplayer(athena);
        Prometheus prometheus = new Prometheus(playerD);
        playerD.setCosplayer(prometheus);
        Apollo apollo = new Apollo(playerE);
        playerE.setCosplayer(apollo);
    }

    /* general test with Prometheus and Apollo*/

    @Test
    public void AthenaSetOpponentPlayersNoMoveUpTest_playerCWorkerAMoveRight_playerDWorkerAMoveLeft_notAllowed() {
        playerC.getCosplayer().move(0, RIGHT);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[1][0].isOccupiedBy(), playerC.getPlayerId() * 10);

        playerD.getCosplayer().move(0, LEFT);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), playerD.getPlayerId() * 10);
    }

    @Test
    public void AthenaSetOpponentPlayersNoMoveUpTest_playerCWorkerAMoveLeft_notAllowed() {
        playerC.getCosplayer().move(0, LEFT);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(), playerC.getPlayerId() * 10);
    }

    @Test
    public void AthenaSetOpponentPlayersNoMoveUpTest_playerCWorkerAMoveRight_playerDWorkerAMoveUp_PlayerEWorkerBMoveUp() {
        playerC.getCosplayer().move(0, RIGHT);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[1][0].isOccupiedBy(), playerC.getPlayerId() * 10);

        playerD.getCosplayer().move(0, UP);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 2);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[2][2].isOccupiedBy(), playerD.getPlayerId() * 10);

        playerE.getCosplayer().move(1, UP);
        assertEquals(playerE.getWorkers()[1].getPositionX(), 4);
        assertEquals(playerE.getWorkers()[1].getPositionY(), 3);
        assertEquals(islandBoard.getSpaces()[4][2].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[4][3].isOccupiedBy(), playerE.getPlayerId() * 10 + 1);
    }

    /* GodPower Mod test with Prometheus and Apollo*/

    @Test
    public void AthenaSetOpponentPlayersNoMoveUpTest_playerCWorkerAMoveUp_playerDWorkerAMoveUp_notAllowed() {
        playerC.getCosplayer().move(0, UP);
        System.out.println(playerD.getCurrentGameBoard().getIslandBoard().isNoMoveUp());
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(), playerC.getPlayerId() * 10);

        System.out.println(playerD.getCurrentGameBoard().getIslandBoard().isNoMoveUp());
        playerD.getCosplayer().move(0, UP);
        System.out.println(playerD.getCurrentGameBoard().getIslandBoard().isNoMoveUp());
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), playerD.getPlayerId() * 10);
    }

    @Test
    public void AthenaSetOpponentPlayersNoMoveUpTest_playerCWorkerAMoveUp_playerDWorkerAMoveRight_playerEWorkerBMoveUp_notAllowed() {
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(), playerC.getPlayerId() * 10);
        System.out.println(playerD.getCurrentGameBoard().getIslandBoard().isNoMoveUp());
        System.out.println(playerE.getCurrentGameBoard().getIslandBoard().isNoMoveUp());

        playerD.getCosplayer().move(0, RIGHT);
        System.out.println(playerD.getCurrentGameBoard().getIslandBoard().isNoMoveUp());
        assertEquals(playerD.getWorkers()[0].getPositionX(), 3);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedBy(), playerD.getPlayerId() * 10);

        playerE.getCosplayer().move(1, UP);
        System.out.println(playerE.getCurrentGameBoard().getIslandBoard().isNoMoveUp());
        assertEquals(playerE.getWorkers()[1].getPositionX(), 4);
        assertEquals(playerE.getWorkers()[1].getPositionY(), 2);
        assertEquals(islandBoard.getSpaces()[4][2].isOccupiedBy(), playerE.getPlayerId() * 10 + 1);
    }

    @Test
    public void AthenaSetOpponentPlayersNoMoveUpTest_playerCWorkerAMoveUp_playerDWorkerAMoveRight_playerEWorkerBMoveDown() {
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(), playerC.getPlayerId() * 10);

        playerD.getCosplayer().move(0, RIGHT);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 3);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(), -1);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedBy(), playerD.getPlayerId() * 10);

        playerE.getCosplayer().move(1, DOWNLEFT);
        assertEquals(playerE.getWorkers()[1].getPositionX(), 3);
        assertEquals(playerE.getWorkers()[1].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[4][2].isOccupiedBy(), playerD.getPlayerId() * 10);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedBy(), playerE.getPlayerId() * 10 + 1);
    }
}

