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
public class ApolloMoveTest {
    Cosplayer cosplayerC;
    Cosplayer cosplayerD;
    IslandBoard islandBoard;
    GameBoard gameBoard;
    Player playerC;
    Player playerD;

    @Before
    public void setUp() {
        // imitate a real game sequence
        // init playerA and player B
        playerC = new Player(1, "playerC");
        playerD = new Player(2, "playerD");

        // init GameBoard with 2 players
        islandBoard = new IslandBoard();
        gameBoard = new GameBoard(1, 2, islandBoard);
        gameBoard.init();

        // add players
        gameBoard.addPlayer(playerC);
        playerC.setCurrentGameBoard(gameBoard);
        gameBoard.addPlayer(playerD);
        playerD.setCurrentGameBoard(gameBoard);

        // set cosplayers and initial worker positions
        cosplayerC = new Apollo(playerC);
        playerC.setCosplayer(cosplayerC);
        playerC.setWorkers(0, 0, 1, 1);
        islandBoard.getSpaces()[0][0].setOccupiedByPlayer(playerC.getPlayerId());
        islandBoard.getSpaces()[1][1].setOccupiedByPlayer(playerC.getPlayerId());

        cosplayerD = new Cosplayer(playerD);
        playerD.setCosplayer(cosplayerD);
        playerD.setWorkers(2, 1, 1, 2);
        islandBoard.getSpaces()[2][1].setOccupiedByPlayer(playerD.getPlayerId());
        islandBoard.getSpaces()[1][2].setOccupiedByPlayer(playerD.getPlayerId());
        /*
        | 0 | 0 | 0 | 0 | 0 |
        | 0 | 0 | 0 | 0 | 0 |
        | 0 |D'B| 0 | 0 | 0 |
        | 0 |C'B|D'A| 0 | 0 |
        |C'A| 0 | 0 | 0 | 0 |
        playerC is Apollo so that playerC's workerB can move to (2,1) or (1,2),
        which will force playerD's workerA or workerB move to (1,1) respectively.
        */
    }

    @After
    public void tearDown() {
        cosplayerC = null;
        cosplayerD = null;
        playerC = null;
        playerD = null;
        islandBoard = null;
        gameBoard = null;
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
