package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.Level;
import it.polimi.ingsw.xyl.model.PlayerStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 *  it's a test of Pan's Win condition
 *  Pan will win if the worker moves down two or more levels
 *
 *  @author Shaoxun
 */
public class PanWinTest extends GodCosplayerTest {

    @Before
    public void setUp() {
        super.setUp();
        Pan pan = new Pan(playerC);
        playerC.setCosplayer(pan);
    }

    @Test
    public void PanWinTest_fromLevel2Move2Ground_win(){
        /*
        | 0 | 0 | 0 | 0 | 0 |
        | 0 | 0 | 0 | 0 | 0 |
        | 0 |D'B| 0 | 0 | 0 |
        | 0 |C'B|D'A| 0 | 0 |
        |C'A| 0 | 0 | 0 | 0 |
        */
        islandBoard.getSpaces()[1][1].setLevel(Level.LEVEL2);
        playerC.getCosplayer().move(1, Direction.LEFT);
        assertEquals(playerC.getCurrentStatus(), PlayerStatus.WIN);
    }

    @Test
    public void PanWinTest_fromLevel3Move2Ground_win(){
        /*
        | 0 | 0 | 0 | 0 | 0 |
        | 0 | 0 | 0 | 0 | 0 |
        | 0 |D'B| 0 | 0 | 0 |
        | 0 |C'B|D'A| 0 | 0 |
        |C'A| 0 | 0 | 0 | 0 |
        */
        islandBoard.getSpaces()[1][1].setLevel(Level.LEVEL3);
        playerC.getCosplayer().move(1, Direction.LEFT);
        assertEquals(playerC.getCurrentStatus(), PlayerStatus.WIN);
    }

    @Test
    public void PanWinTest_fromLevel3Move2Level1_win(){
        /*
        | 0 | 0 | 0 | 0 | 0 |
        | 0 | 0 | 0 | 0 | 0 |
        | 0 |D'B| 0 | 0 | 0 |
        | 1 |C'B|D'A| 0 | 0 |
        |C'A| 0 | 0 | 0 | 0 |
        */
        islandBoard.getSpaces()[1][1].setLevel(Level.LEVEL3);
        islandBoard.getSpaces()[0][1].setLevel(Level.LEVEL1);
        playerC.getCosplayer().move(1, Direction.LEFT);
        assertEquals(playerC.getCurrentStatus(), PlayerStatus.WIN);
    }

    @Test
    public void PanWinTest_forcedMoveByApollo_notWin(){
        /*
        | 0 | 0 | 0 | 0 | 0 |
        | 0 | 0 | 0 | 0 | 0 |
        | 0 |D'B| 0 | 0 | 0 |
        | 0 |C'B|D'A| 0 | 0 |
        |C'A| 0 | 0 | 0 | 0 |
        */
        Apollo apollo = new Apollo(playerD);
        playerD.setCosplayer(apollo);
        islandBoard.getSpaces()[2][1].setLevel(Level.LEVEL3);
        playerD.getCosplayer().move(0, Direction.LEFT);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),playerD.getPlayerId() * 10); //playerD's workerA
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);  //playerC's workerB
        assertEquals(playerD.getWorkers()[0].getPositionX(),1);
        assertEquals(playerD.getWorkers()[0].getPositionY(),1);
        assertEquals(playerC.getWorkers()[1].getPositionX(),2);
        assertEquals(playerC.getWorkers()[1].getPositionY(),1);
        playerC.getCosplayer().checkWin();
        assertNotEquals(playerC.getCurrentStatus(), PlayerStatus.WIN);
    }

    @Test
    public void PanWinTest_forcedMoveByApolloThen2Level3_notWin(){
        /*
        | 0 | 0 | 0 | 0 | 0 |
        | 0 | 0 | 0 | 0 | 0 |
        | 0 |D'B| 0 | 0 | 0 |
        | 0 |C'B|D'A| 0 | 0 |
        |C'A| 0 | 0 | 0 | 0 |
        */
        Apollo apollo = new Apollo(playerD);
        playerD.setCosplayer(apollo);
        islandBoard.getSpaces()[2][1].setLevel(Level.LEVEL3);
        islandBoard.getSpaces()[3][1].setLevel(Level.LEVEL3);
        playerD.getCosplayer().move(0, Direction.LEFT);
        playerC.getCosplayer().move(1, Direction.RIGHT);
        assertEquals(playerC.getWorkers()[1].getPositionX(),3);
        assertEquals(playerC.getWorkers()[1].getPositionY(),1);
        assertNotEquals(playerC.getCurrentStatus(), PlayerStatus.WIN);
    }

    @Test
    public void PanWinTest_forcedMoveByMinotaur_notWin(){
        /*
        | 0 | 0 | 0 | 0 | 0 |
        | 0 | 0 | 0 | 0 | 0 |
        | 0 |D'B| 0 | 0 | 0 |
        | 0 |C'B|D'A| 0 | 0 |
        |C'A| 0 | 0 | 0 | 0 |
        */
        Minotaur minotaur = new Minotaur(playerD);
        playerD.setCosplayer(minotaur);
        islandBoard.getSpaces()[0][1].setLevel(Level.LEVEL3);
        playerD.getCosplayer().move(0, Direction.LEFT);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),playerD.getPlayerId() * 10); //playerD's workerA
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(),-1);
        assertEquals(playerD.getWorkers()[0].getPositionX(),1);
        assertEquals(playerD.getWorkers()[0].getPositionY(),1);
        assertEquals(playerC.getWorkers()[1].getPositionX(),0);
        assertEquals(playerC.getWorkers()[1].getPositionY(),1);
        playerC.getCosplayer().checkWin();
        assertNotEquals(playerC.getCurrentStatus(), PlayerStatus.WIN);
        playerC.getCosplayer().move(1, Direction.UP);
        assertEquals(playerC.getCurrentStatus(), PlayerStatus.WIN);
    }
}

