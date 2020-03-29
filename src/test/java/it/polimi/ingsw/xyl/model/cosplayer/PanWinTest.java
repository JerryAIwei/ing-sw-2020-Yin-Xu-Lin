package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.Level;
import it.polimi.ingsw.xyl.model.PlayerStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
