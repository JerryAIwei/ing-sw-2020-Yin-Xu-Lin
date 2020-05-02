package it.polimi.ingsw.xyl.model.cosplayer;

import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.DOME;
import static it.polimi.ingsw.xyl.model.Level.LEVEL2;
import static org.junit.Assert.assertEquals;

/**
 *  it's a test of cosplayer moving(Artemis)
 *  Artemis' move: worker may move one additional time,
 *  but not back to its initial space.
 *
 *  @author Shaoxun
 */
public class ArtemisMoveTest extends GodCosplayerTest{

    @Before
    public void setUp() {
        super.setUp();
        Artemis artemis = new Artemis(playerC);
        playerC.setCosplayer(artemis);
    }

    /* general test */

    @Test
    public void ArtemisMoveTest_playerCWorkerBMoveDown_normalMove(){
        playerC.getCosplayer().move(1, DOWN);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[1][0].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);
    }

    @Test(expected=RuntimeException.class)
    public void ArtemisMoveTest_playerCWorkerAMoveUp_dome_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(DOME);
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(),playerC.getPlayerId() * 10);
    }

    @Test(expected=RuntimeException.class)
    public void ArtemisMoveTest_playerCWorkerAMoveUp_level2_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(LEVEL2);
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(),playerC.getPlayerId() * 10);
    }

    @Test(expected=RuntimeException.class)
    public void ArtemisMoveTest_playerCWorkerAMoveUp_occupied_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setOccupiedBy(3);
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(),3);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(),playerC.getPlayerId() * 10);
    }

    /* GodPower Mod test */

    @Test
    public void ArtemisMoveTest_playerCWorkerBMoveDownThenMoveRight_usePower(){
        playerC.getCosplayer().move(1, DOWN);
        playerC.getCosplayer().move(1, RIGHT);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 2);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[1][0].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[2][0].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);
    }

    @Test(expected=RuntimeException.class)
    public void ArtemisMoveTest_playerCWorkerBMoveDownThenMoveUp_notAllowed(){
        playerC.getCosplayer().move(1, DOWN);
        playerC.getCosplayer().move(1, UP);  // no go back to its initial space
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 0);
    }

}
