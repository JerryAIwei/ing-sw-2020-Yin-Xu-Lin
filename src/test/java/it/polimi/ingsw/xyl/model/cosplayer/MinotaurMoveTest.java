package it.polimi.ingsw.xyl.model.cosplayer;

import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.*;
import static org.junit.Assert.assertEquals;

/**
 *  it's a test of cosplayer moving(Minotaur)
 *  Minotaur's move: worker may move into an opponent worker's
 *  space, if their worker can be forced one space straight backwards
 *  to an unoccupied space at ANY level.
 *
 *  @author Shaoxun
 */
public class MinotaurMoveTest extends GodCosplayerTest{

    @Before
    public void setUp() {
        super.setUp();
        Minotaur minotaur = new Minotaur(playerC);
        playerC.setCosplayer(minotaur);
    }

    /* general test */

    @Test
    public void MinotaurMoveTest_playerCWorkerBMoveDown_normalMove(){
        playerC.getCosplayer().move(1, DOWN);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[1][0].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerAMoveUp_dome_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(DOME);
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(),playerC.getPlayerId() * 10);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerAMoveUp_level2_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(LEVEL2);
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(),playerC.getPlayerId() * 10);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerA_MoveRight_affectedByAthena_noLevelUp_notAllowed() {
        gameBoard.getIslandBoard().getSpaces()[1][0].setLevel(LEVEL1);
        gameBoard.getIslandBoard().setNoLevelUp(true);
        playerC.getCosplayer().move(0, RIGHT);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(), playerC.getPlayerId() * 10);
    }
    /* GodPower Mod test */

    @Test
    public void MinotaurMoveTest_playerCWorkerBMoveUp_usePower(){
        playerC.getCosplayer().move(1, UP);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 2);
        assertEquals(playerD.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerD.getWorkers()[1].getPositionY(), 3);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[1][2].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);
        assertEquals(islandBoard.getSpaces()[1][3].isOccupiedBy(),playerD.getPlayerId() * 10 + 1);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerBMoveRight_usePower(){
        playerC.getCosplayer().move(1, RIGHT);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 2);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 1);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 3);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedBy(),playerD.getPlayerId() * 10);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerBMoveRight_Level2_3_1_usePower(){
        // Minotaur's move: worker may move into an opponent worker's
        // space, if their worker can be forced one space straight backwards
        // to an unoccupied space at ANY level.
        islandBoard.getSpaces()[3][1].setLevel(LEVEL2);
        playerC.getCosplayer().move(1, RIGHT);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 2);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 1);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 3);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedBy(),playerD.getPlayerId() * 10);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerBMoveRight_dome_3_1_notAllowed(){
        // Minotaur's move: worker may move into an opponent worker's
        // space, if their worker can be forced one space straight backwards
        // to an unoccupied space at ANY level.
        islandBoard.getSpaces()[3][1].setLevel(DOME);
        playerC.getCosplayer().move(1, RIGHT);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 1);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(),playerD.getPlayerId() * 10);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedBy(),-1);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerBMoveRight_occupied_3_1_notAllowed(){
        // Minotaur's move: worker may move into an opponent worker's
        // space, if their worker can be forced one space straight backwards
        // to an unoccupied space at ANY level.
        islandBoard.getSpaces()[3][1].setOccupiedBy(3);
        playerC.getCosplayer().move(1, RIGHT);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 1);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(),playerD.getPlayerId() * 10);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedBy(),3);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerB_MoveRight_affectedByAthena_noLevelUp_notAllowed() {
        gameBoard.getIslandBoard().getSpaces()[2][1].setLevel(LEVEL1);
        gameBoard.getIslandBoard().setNoLevelUp(true);
        playerC.getCosplayer().move(1, RIGHT);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(), playerC.getPlayerId() * 10 + 1);
    }

}
