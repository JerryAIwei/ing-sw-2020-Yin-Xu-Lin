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
public class MinotaurMoveTest {
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
        cosplayerC = new Minotaur(playerC);
        playerC.setCosplayer(cosplayerC);
        playerC.setWorkerPosition('A',new int[]{0, 0});
        playerC.setWorkerPosition('B',new int[]{1, 1});
        islandBoard.getSpaces()[0][0].setOccupiedByPlayer(playerC.getPlayerId());
        islandBoard.getSpaces()[1][1].setOccupiedByPlayer(playerC.getPlayerId());

        cosplayerD = new Cosplayer(playerD);
        playerD.setCosplayer(cosplayerD);
        playerD.setWorkerPosition('A',new int[]{2, 1});
        playerD.setWorkerPosition('B',new int[]{1, 2});
        islandBoard.getSpaces()[2][1].setOccupiedByPlayer(playerD.getPlayerId());
        islandBoard.getSpaces()[1][2].setOccupiedByPlayer(playerD.getPlayerId());
        /*
        | 0 | 0 | 0 | 0 | 0 |
        | 0 | 0 | 0 | 0 | 0 |
        | 0 |D'B| 0 | 0 | 0 |
        | 0 |C'B|D'A| 0 | 0 |
        |C'A| 0 | 0 | 0 | 0 |
        playerC is Minotaur so that its workerB can move to (2,1),
        which will force playerD's workerA move to (3,1)
        if (3,1) is neither occupied by another player nor placed a dome.
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
    public void MinotaurMoveTest_playerCWorkerBMoveDown_normalMove(){
        playerC.getCosplayer().move('B', DOWN);
        assertEquals(playerC.getWorkerPosition('B')[0], 1);
        assertEquals(playerC.getWorkerPosition('B')[1], 0);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[1][0].isOccupiedByPlayer(),1);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerAMoveUp_dome_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(DOME);
        playerC.getCosplayer().move('A', UP);
        assertEquals(playerC.getWorkerPosition('A')[0], 0);
        assertEquals(playerC.getWorkerPosition('A')[1], 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedByPlayer(),1);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerAMoveUp_level2_0_1_notAllowed(){
        islandBoard.getSpaces()[0][1].setLevel(LEVEL2);
        playerC.getCosplayer().move('A', UP);
        assertEquals(playerC.getWorkerPosition('A')[0], 0);
        assertEquals(playerC.getWorkerPosition('A')[1], 0);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[0][0].isOccupiedByPlayer(),1);
    }

    /* GodPower Mod test */

    @Test
    public void MinotaurMoveTest_playerCWorkerBMoveUp_usePower(){
        playerC.getCosplayer().move('B', UP);
        assertEquals(playerC.getWorkerPosition('B')[0], 1);
        assertEquals(playerC.getWorkerPosition('B')[1], 2);
        assertEquals(playerD.getWorkerPosition('B')[0], 1);
        assertEquals(playerD.getWorkerPosition('B')[1], 3);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),0);
        assertEquals(islandBoard.getSpaces()[1][2].isOccupiedByPlayer(),1);
        assertEquals(islandBoard.getSpaces()[1][3].isOccupiedByPlayer(),2);
    }

    @Test
    public void MinotaurMoveTest_playerCWorkerBMoveRight_usePower(){
        playerC.getCosplayer().move('B', RIGHT);
        assertEquals(playerC.getWorkerPosition('B')[0], 2);
        assertEquals(playerC.getWorkerPosition('B')[1], 1);
        assertEquals(playerD.getWorkerPosition('A')[0], 3);
        assertEquals(playerD.getWorkerPosition('A')[1], 1);
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
        playerC.getCosplayer().move('B', RIGHT);
        assertEquals(playerC.getWorkerPosition('B')[0], 2);
        assertEquals(playerC.getWorkerPosition('B')[1], 1);
        assertEquals(playerD.getWorkerPosition('A')[0], 3);
        assertEquals(playerD.getWorkerPosition('A')[1], 1);
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
        playerC.getCosplayer().move('B', RIGHT);
        assertEquals(playerC.getWorkerPosition('B')[0], 1);
        assertEquals(playerC.getWorkerPosition('B')[1], 1);
        assertEquals(playerD.getWorkerPosition('A')[0], 2);
        assertEquals(playerD.getWorkerPosition('A')[1], 1);
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
        playerC.getCosplayer().move('B', RIGHT);
        assertEquals(playerC.getWorkerPosition('B')[0], 1);
        assertEquals(playerC.getWorkerPosition('B')[1], 1);
        assertEquals(playerD.getWorkerPosition('A')[0], 2);
        assertEquals(playerD.getWorkerPosition('A')[1], 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedByPlayer(),1);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedByPlayer(),2);
        assertEquals(islandBoard.getSpaces()[3][1].isOccupiedByPlayer(),0);
    }

}
