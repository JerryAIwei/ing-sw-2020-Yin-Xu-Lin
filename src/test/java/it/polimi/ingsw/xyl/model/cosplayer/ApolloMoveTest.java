package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.*;
import static it.polimi.ingsw.xyl.model.Level.DOME;
import static it.polimi.ingsw.xyl.model.Level.LEVEL2;
import static org.junit.Assert.assertEquals;

/**
 *  it's a test of cosplayer moving(Apollo)
 *  Apollo's move: worker may move into an opponent worker's space
 *  by forcing their worker to the space to the space yours just vacated.
 *
 *  @author Shaoxun
 */
public class ApolloMoveTest extends GodCosplayerTest{

    @Before
    public void setUp() {
        super.setUp();
        Apollo apollo = new Apollo(playerC);
        playerC.setCosplayer(apollo);
    }

    /* general test */

    @Test
    public void ApolloMoveTest_playerCWorkerBMoveDown_normalMove(){
        playerC.getCosplayer().move('B', DOWN);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[1][0].isOccupiedByPlayer(),1);
    }

    @Test
    public void ApolloMoveTest_playerCWorkerAMoveUp_dome_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(DOME);
        playerC.getCosplayer().move('A', UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedByPlayer(),1);
    }

    @Test
    public void ApolloMoveTest_playerCWorkerAMoveUp_level2_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(LEVEL2);
        playerC.getCosplayer().move('A', UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedByPlayer(),1);
    }

    /* GodPower Mod test */

    @Test
    public void ApolloMoveTest_playerCWorkerBMoveRight_exchangeWithPlayerDWorkerA(){
        playerC.getCosplayer().move('B', RIGHT);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 2);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 1);
        assertEquals(playerD.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerD.getWorkers()[1].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),2);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedByPlayer(),1);
    }

    @Test
    public void ApolloMoveTest_playerCWorkerBMoveUP_exchangeWithPlayerDWorkerB(){
        playerC.getCosplayer().move('B', UP);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 2);
        assertEquals(playerD.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerD.getWorkers()[1].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),2);
        assertEquals(islandBoard.getSpaces()[1][2].isOccupiedByPlayer(),1);
    }

}
