package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.IslandBoard;
import it.polimi.ingsw.xyl.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.DOME;
import static it.polimi.ingsw.xyl.model.Level.LEVEL2;
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
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[1][0].isOccupiedByPlayer(),1);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerAMoveUp_dome_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(DOME);
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedByPlayer(),1);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerAMoveUp_level2_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(LEVEL2);
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedByPlayer(),1);
    }

    /* GodPower Mod test */

    @Test
    public void MinotaurMoveTest_playerCWorkerBMoveUp_usePower(){
        playerC.getCosplayer().move(1, UP);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 2);
        assertEquals(playerD.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerD.getWorkers()[1].getPositionY(), 3);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[1][2].isOccupiedByPlayer(),1);
        assertEquals(islandBoard.getSpaces()[1][3].isOccupiedByPlayer(),2);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerBMoveRight_usePower(){
        playerC.getCosplayer().move(1, RIGHT);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 2);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 1);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 3);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedByPlayer(),1);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedByPlayer(),2);
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
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedByPlayer(),1);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedByPlayer(),2);
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
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedByPlayer(),2);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedByPlayer(),0);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerBMoveRight_occupied_3_1_notAllowed(){
        // Minotaur's move: worker may move into an opponent worker's
        // space, if their worker can be forced one space straight backwards
        // to an unoccupied space at ANY level.
        islandBoard.getSpaces()[3][1].setOccupiedByPlayer(3);
        playerC.getCosplayer().move(1, RIGHT);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 1);
        assertEquals(playerD.getWorkers()[0].getPositionX(), 2);
        assertEquals(playerD.getWorkers()[0].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedByPlayer(),2);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedByPlayer(),0);
    }

}
