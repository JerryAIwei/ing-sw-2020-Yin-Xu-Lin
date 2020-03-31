package it.polimi.ingsw.xyl.model.cosplayer;

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
        playerC.getCosplayer().move(1, DOWN);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[1][0].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);
    }

    @Test
    public void ApolloMoveTest_playerCWorkerAMoveUp_dome_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(DOME);
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(),-1);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(),playerC.getPlayerId() * 10);
    }

    @Test
    public void ApolloMoveTest_playerCWorkerAMoveUp_level2_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(LEVEL2);
        playerC.getCosplayer().move(0, UP);
        assertEquals(playerC.getWorkers()[0].getPositionX(), 0);
        assertEquals(playerC.getWorkers()[0].getPositionY(), 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(),-1);  //not occupied
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedBy(),playerC.getPlayerId() * 10); //playerC's workerA
    }

    /* GodPower Mod test */

    @Test
    public void ApolloMoveTest_playerCWorkerBMoveRight_exchangeWithPlayerDWorkerA(){
        playerC.getCosplayer().move(1, RIGHT);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 2);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 1);
        assertEquals(playerD.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerD.getWorkers()[1].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),playerD.getPlayerId() * 10); //playerD's workerA
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(),playerC.getPlayerId() * 10 + 1);  //playerC's workerB
    }

    @Test
    public void ApolloMoveTest_playerCWorkerBMoveUP_exchangeWithPlayerDWorkerB(){
        playerC.getCosplayer().move(1, UP);
        assertEquals(playerC.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerC.getWorkers()[1].getPositionY(), 2);
        assertEquals(playerD.getWorkers()[1].getPositionX(), 1);
        assertEquals(playerD.getWorkers()[1].getPositionY(), 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),playerD.getPlayerId() * 10 + 1); //playerD's workerB
        assertEquals(islandBoard.getSpaces()[1][2].isOccupiedBy(),playerC.getPlayerId() * 10 + 1); //playerC's workerB
    }

}
