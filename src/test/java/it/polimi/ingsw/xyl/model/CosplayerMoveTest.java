package it.polimi.ingsw.xyl.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.DOME;
import static it.polimi.ingsw.xyl.model.Level.LEVEL2;
import static org.junit.Assert.assertEquals;

/**
 *  it's a test of cosplayer moving(without GodPower currently)
 *
 *  @author Shaoxun
 */
public class CosplayerMoveTest {

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
        playerA.setWorkers(0, 0, 1, 1);
        islandBoard.getSpaces()[0][0].setOccupiedByPlayer(playerA.getPlayerId());
        islandBoard.getSpaces()[1][1].setOccupiedByPlayer(playerA.getPlayerId());

        cosplayerB = new Cosplayer(playerB);
        playerB.setCosplayer(cosplayerB);
        playerB.setWorkers(3, 3, 4, 4);
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
    public void CosplayerMoveTest_playerOverNumber(){
        Player playerC = new Player(3, "playerC");
        gameBoard.addPlayer(playerC);
        Player playerD = new Player(4, "playerD");
        gameBoard.addPlayer(playerD);
        assertEquals(gameBoard.getPlayers().size(),2);
    }

    @Test
    public void CosplayerMoveTest_playerNameTest() {
        assertEquals(playerA.getPlayerName(),"playerA");
        assertEquals(playerB.getPlayerName(),"playerB");
    }

    @Test
    public void CosplayerMoveTest_playerAWorkerAMoveRight_1_0() {
        playerA.getCosplayer().move(0, RIGHT);
        assertEquals(playerA.getWorkers()[0].getPositionX(), 1);
        assertEquals(playerA.getWorkers()[0].getPositionY(), 0);
    }

    @Test (expected = Exception.class)
    public void CosplayerMoveTest_playerAWorkerAMoveLeft_Error() {
        playerA.getCosplayer().move(0, LEFT);
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

    @Test
    public void CosplayerMoveTest_playerAWorkerBMoveLeft_occupied_0_1_notAllowed() {
        islandBoard.getSpaces()[0][1].setOccupiedByPlayer(1);
        playerA.getCosplayer().move(1, LEFT);
        assertEquals(playerA.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerA.getWorkers()[1].getPositionY(), 1);
    }

    @Test
    public void CosplayerMoveTest_playerAWorkerBMoveLeft_dome_0_1_notAllowed() {
        islandBoard.getSpaces()[0][1].setLevel(DOME);
        playerA.getCosplayer().move(1, LEFT);
        assertEquals(playerA.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerA.getWorkers()[1].getPositionY(), 1);
    }

    @Test
    public void CosplayerMoveTest_playerAWorkerBMoveLeft_LEVEL2_0_1_notAllowed() {
        islandBoard.getSpaces()[0][1].setLevel(LEVEL2);
        playerA.getCosplayer().move(1, LEFT);
        assertEquals(playerA.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerA.getWorkers()[1].getPositionY(), 1);
    }
}
