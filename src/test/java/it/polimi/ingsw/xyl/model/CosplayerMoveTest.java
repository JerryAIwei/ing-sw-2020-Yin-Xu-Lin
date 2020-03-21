package it.polimi.ingsw.xyl.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static org.junit.Assert.assertEquals;

/**
 *  it's a test of cosplayer moving(without GodPower currently)
 *
 *  @author Shaoxun
 *
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
        // init playerA
        playerA = new Player(1, "playerA", new int[]{0,0}, new int[]{1,1});
        cosplayerA = new Cosplayer(playerA);
        playerA.setCosplayer(cosplayerA);
        // init playerB
        playerB = new Player(2, "playerB", new int[]{3,3}, new int[]{4,4});
        cosplayerB = new Cosplayer(playerB);
        playerB.setCosplayer(cosplayerB);
        // init GameBoard
        islandBoard = new IslandBoard();
        gameBoard = new GameBoard(1,2,islandBoard);
        gameBoard.init();
        gameBoard.addPlayer(playerA);
        gameBoard.addPlayer(playerB);
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
    public void CosplayerMoveTest_playerAWorkerAMoveRight_1_0(){
        playerA.getCosplayer().move('A',RIGHT);
        assertEquals(playerA.getWorkerPosition('A')[0],1);
        assertEquals(playerA.getWorkerPosition('A')[1],0);
    }
    @Test
    public void CosplayerMoveTest_playerBWorkerAMoveRight_4_3(){
        playerB.getCosplayer().move('A',RIGHT);
        assertEquals(playerB.getWorkerPosition('A')[0],4);
        assertEquals(playerB.getWorkerPosition('A')[1],3);
    }
    @Test
    public void CosplayerMoveTest_playerAWorkerBMoveUP_1_2(){
        playerA.getCosplayer().move('B',UP);
        assertEquals(playerA.getWorkerPosition('B')[0],1);
        assertEquals(playerA.getWorkerPosition('B')[1],2);
    }
    @Test
    public void CosplayerMoveTest_playerAWorkerBMoveLeft_0_1(){
        playerA.getCosplayer().move('B',LEFT);
        assertEquals(playerA.getWorkerPosition('B')[0],0);
        assertEquals(playerA.getWorkerPosition('B')[1],1);
    }
}
