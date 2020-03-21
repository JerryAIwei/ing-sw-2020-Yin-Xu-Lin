package it.polimi.ingsw.xyl.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.GROUND;
import static it.polimi.ingsw.xyl.model.Level.LEVEL1;
import static org.junit.Assert.assertEquals;

/**
 *  it's a test of cosplayer building(without GodPower currently)
 *
 *  @author Shaoxun
 */
public class CosplayerBuildTest {
    Cosplayer cosplayerA;
    Cosplayer cosplayerB;
    IslandBoard islandBoard;
    GameBoard gameBoard;
    Player playerA;
    Player playerB;

    @Before
    public void setUp() {
        // imitate a real game sequence
        // init playerA and player B
        playerA = new Player(1, "playerA");
        playerB = new Player(2, "playerB");

        // init GameBoard with 2 players
        islandBoard = new IslandBoard();
        gameBoard = new GameBoard(1, 2, islandBoard);
        gameBoard.init();

        // add players
        gameBoard.addPlayer(playerA);
        playerA.setCurrentGameBoard(gameBoard);
        gameBoard.addPlayer(playerB);
        playerB.setCurrentGameBoard(gameBoard);

        // set cosplayers and initial worker positions
        cosplayerA = new Cosplayer(playerA);
        playerA.setCosplayer(cosplayerA);
        playerA.setWorkerPosition('A',new int[]{0, 0});
        playerA.setWorkerPosition('B',new int[]{1, 1});
        islandBoard.getSpaces()[0][0].setOccupiedByPlayer(playerA.getPlayerId());
        islandBoard.getSpaces()[1][1].setOccupiedByPlayer(playerA.getPlayerId());

        cosplayerB = new Cosplayer(playerB);
        playerB.setCosplayer(cosplayerB);
        playerB.setWorkerPosition('A',new int[]{3, 3});
        playerB.setWorkerPosition('B',new int[]{4, 4});
        islandBoard.getSpaces()[3][3].setOccupiedByPlayer(playerB.getPlayerId());
        islandBoard.getSpaces()[4][4].setOccupiedByPlayer(playerB.getPlayerId());
    }

    @After
    public void tearDown() {
        cosplayerA = null;
        cosplayerB = null;
        playerA = null;
        playerB = null;
        islandBoard = null;
        gameBoard = null;
    }

    @Test
    public void CosplayerBuildTest_playerAWorkerABuildRight_LEVEL1_1_0(){
        playerA.getCosplayer().build('A', RIGHT);
        assertEquals(playerA.getCurrentGameBoard().getIslandBoard().getSpaces()[1][0].getLevel(),LEVEL1);
    }

    @Test
    public void CosplayerBuildTest_playerAWorkerABuildUpRight_NoChange() {
        playerA.getCosplayer().build('A', UPRIGHT);
        assertEquals(playerA.getCurrentGameBoard().getIslandBoard().getSpaces()[1][0].getLevel(),GROUND);
    }

    @Test
    public void CosplayerBuildTest_playerBWorkerBBuildDownLeft_NoChange() {
        playerB.getCosplayer().build('B', DOWNLEFT);
        assertEquals(playerB.getCurrentGameBoard().getIslandBoard().getSpaces()[3][3].getLevel(),GROUND);
    }

    @Test (expected = Exception.class)
    public void CosplayerBuildTest_playerAWorkerABuildLeft_Error() {
        playerA.getCosplayer().build('A', LEFT);
    }

    @Test
    public void CosplayerBuildTest_playerABuildAtTurnId_1_turnId_0() {
        gameBoard.setTurnId(1);
        playerA.getCosplayer().build('A', RIGHT);
        assertEquals(gameBoard.getTurnId(),0);
    }

}
