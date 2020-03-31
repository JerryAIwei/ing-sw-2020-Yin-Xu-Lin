package it.polimi.ingsw.xyl.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CosplayerTest {
    Cosplayer cosplayerA;
    Cosplayer cosplayerB;
    IslandBoard islandBoard;
    GameBoard gameBoard;
    Player playerA;
    Player playerB;

    @Before
    public void setUp() {
        // imitate a real game sequence
        // init playerA and player B
        playerA = new Player(0, "playerA");
        playerB = new Player(1, "playerB");

        // init GameBoard with 2 players
        islandBoard = new IslandBoard();
        gameBoard = new GameBoard(1, 2, islandBoard);

        // add players
        gameBoard.addPlayer(playerA);
        playerA.setCurrentGameBoard(gameBoard);
        gameBoard.addPlayer(playerB);
        playerB.setCurrentGameBoard(gameBoard);

        // set cosplayers and initial worker positions
        cosplayerA = new Cosplayer(playerA);
        playerA.setCosplayer(cosplayerA);
        playerA.setWorkers(0, 0, 1, 1);
        islandBoard.getSpaces()[0][0].setOccupiedBy(playerA.getPlayerId() * 10);
        islandBoard.getSpaces()[1][1].setOccupiedBy(playerA.getPlayerId() * 10 + 1);

        cosplayerB = new Cosplayer(playerB);
        playerB.setCosplayer(cosplayerB);
        playerB.setWorkers(3, 3, 4, 4);
        islandBoard.getSpaces()[3][3].setOccupiedBy(playerB.getPlayerId() * 10);
        islandBoard.getSpaces()[4][4].setOccupiedBy(playerB.getPlayerId() * 10 +1);
        /*
        | 0 | 0 | 0 | 0 |B'B|
        | 0 | 0 | 0 |B'A| 0 |
        | 0 | 0 | 0 | 0 | 0 |
        | 0 |A'B| 0 | 0 | 0 |
        |A'A| 0 | 0 | 0 | 0 |
        */
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
    public void CosplayerTest_playerIdNameTest() {
        assertEquals(playerA.getPlayerId(),0);
        assertEquals(playerB.getPlayerId(),1);
        assertEquals(playerA.getPlayerName(),"playerA");
        assertEquals(playerB.getPlayerName(),"playerB");
    }

    @Test
    public void CosplayerTest_playerBFinishedTurnId_1_turnId_0() {
        gameBoard.toNextPlayer(1);
        assertEquals(gameBoard.getCurrentPlayer().getPlayerId(),1);
        gameBoard.getCurrentPlayer().getCosplayer().build(0, Direction.RIGHT,false);
        assertEquals(gameBoard.getCurrentPlayer().getPlayerId(),0);
    }

    @Test
    public void CosplayerTest_getAvailableMoves() {
        islandBoard.getSpaces()[1][0].setLevel(Level.LEVEL2);
        islandBoard.getSpaces()[0][1].setLevel(Level.LEVEL3);
        islandBoard.getSpaces()[2][2].setLevel(Level.DOME);
        islandBoard.getSpaces()[2][1].setLevel(Level.LEVEL1);
        // imitate Athena's power no move up
        islandBoard.setNoMoveUp(true);
        /*
        | 0 | 0 | 0 | 0 |B'B|
        | 0 | 0 | 0 |B'A| 0 |
        | 0 | 0 | @ | 0 | 0 |
        | 3 |A'B| 1 | 0 | 0 |
        |A'A| 2 | 0 | 0 | 0 |
        */
        assertEquals(playerA.getCosplayer().getAvailableMoves(0).size(),0);
        assertEquals(playerA.getCosplayer().getAvailableMoves(1).size(),3);
        ArrayList<Direction> availableMoves = new ArrayList<>();
        availableMoves.add(Direction.UP);
        availableMoves.add(Direction.UPLEFT);
        availableMoves.add(Direction.DOWNRIGHT);
        assertTrue(playerA.getCosplayer().getAvailableMoves(1).containsAll(availableMoves));
    }

    @Test
    public void CosplayerTest_checkWinAfterMove_playerAWin(){
        islandBoard.getSpaces()[1][1].setLevel(Level.LEVEL2);
        islandBoard.getSpaces()[1][0].setLevel(Level.LEVEL2);
        islandBoard.getSpaces()[0][1].setLevel(Level.LEVEL3);
        islandBoard.getSpaces()[2][2].setLevel(Level.DOME);
        /*
        | 0 | 0 | 0 | 0 |B'B|
        | 0 | 0 | 0 |B'A| 0 |
        | 0 | 0 | @ | 0 | 0 |
        | 3 |A'B| 0 | 0 | 0 |
        |A'A| 2 | 0 | 0 | 0 |

        the level of (1,1) is 2
        */
        playerA.getCosplayer().move(1,Direction.LEFT);
        assertEquals(playerA.getCurrentStatus(),PlayerStatus.WIN);
    }

    @Test
    public void CosplayerTest_checkWinAfterTurnIdChange(){

        islandBoard.getSpaces()[1][0].setLevel(Level.LEVEL2);
        islandBoard.getSpaces()[1][2].setLevel(Level.LEVEL2);
        islandBoard.getSpaces()[0][1].setLevel(Level.LEVEL3);
        islandBoard.getSpaces()[0][2].setLevel(Level.LEVEL2);
        islandBoard.getSpaces()[2][0].setLevel(Level.LEVEL2);
        islandBoard.getSpaces()[2][1].setLevel(Level.LEVEL2);
        islandBoard.getSpaces()[2][2].setLevel(Level.LEVEL1);
        /*
        | 0 | 0 | 0 | 0 |B'B|
        | 0 | 0 | 0 |B'A| 0 |
        | 2 | 2 | 1 | 0 | 0 |
        | 3 |A'B| 2 | 0 | 0 |
        |A'A| 2 | 2 | 0 | 0 |
        */
        gameBoard.toNextPlayer(1); // B's turn
        assertEquals(gameBoard.getPlayers().get(1).getPlayerName(),"playerB");
        gameBoard.getPlayers().get(1).getCosplayer().move(0,Direction.DOWN);
        gameBoard.getPlayers().get(1).getCosplayer().build(0,Direction.LEFT,false);
        assertEquals(islandBoard.getSpaces()[3][2].isOccupiedBy(),
                gameBoard.getPlayers().get(1).getPlayerId() * 10);
        assertEquals(islandBoard.getSpaces()[2][2].getLevel(),Level.LEVEL2);
        /*
        | 0 | 0 | 0 | 0 |B'B|
        | 0 | 0 | 0 | 0 | 0 |
        | 2 | 2 | 2 |B'A| 0 |
        | 3 |A'B| 2 | 0 | 0 |
        |A'A| 2 | 2 | 0 | 0 |
        */
        assertEquals(gameBoard.getCurrentPlayer().getPlayerId(),0);
        gameBoard.getCurrentPlayer().getCosplayer().checkWin(); // imitate turn
        // controller
        assertEquals(gameBoard.getCurrentPlayer().getCurrentStatus(),PlayerStatus.LOSE);
        // assertEquals(gameBoard.getPlayers().get(1).getCurrentStatus(),PlayerStatus.WIN); // should be
    }

    @Test
    public void CosplayerTest_checkWinAfterForced2Level3(){
        // unset initial setUp
        islandBoard.getSpaces()[3][3].setOccupiedBy(-1);
        islandBoard.getSpaces()[4][4].setOccupiedBy(-1);
        // new playerB workers' position
        playerB.setWorkers(2, 1, 4, 4);
        islandBoard.getSpaces()[2][1].setOccupiedBy(playerB.getPlayerId() * 10);
        islandBoard.getSpaces()[4][4].setOccupiedBy(playerB.getPlayerId() * 10 + 1);

        islandBoard.getSpaces()[1][0].setLevel(Level.LEVEL2);
        islandBoard.getSpaces()[1][2].setLevel(Level.LEVEL2);
        islandBoard.getSpaces()[0][1].setLevel(Level.LEVEL3);
        islandBoard.getSpaces()[0][2].setLevel(Level.LEVEL3);
        islandBoard.getSpaces()[2][0].setLevel(Level.LEVEL2);
        islandBoard.getSpaces()[2][2].setLevel(Level.LEVEL1);
        /*
        | 0 | 0 | 0 | 0 |B'B|
        | 0 | 0 | 0 | 0 | 0 |
        | 3 | 2 | 1 | 0 | 0 |
        | 3 |A'B|B'A| 0 | 0 |
        |A'A| 2 | 2 | 0 | 0 |
        */

        // suppose B is Minotaur
        // imitate Minotaur's move B'A -> (1,1)
        //    Minotaur's move: worker may move into an opponent worker's
        //    space, if their worker can be forced one space straight backwards
        //    to an unoccupied space at ANY level.
        playerA.getWorkers()[1].setFromLevel(3);
        islandBoard.getSpaces()[1][1].setLevel(Level.LEVEL3); // add to escape *Not Allowed*
        playerA.getCosplayer().move(1,Direction.LEFT);
        islandBoard.getSpaces()[1][1].setLevel(Level.GROUND); // reset to initial value
        assertNotEquals(playerA.getCurrentStatus(),PlayerStatus.WIN);
        playerB.getCosplayer().move(0,Direction.LEFT);

        assertNotEquals(playerA.getCurrentStatus(),PlayerStatus.WIN);
        assertNotEquals(playerB.getCurrentStatus(),PlayerStatus.WIN);
        assertEquals(islandBoard.getSpaces()[0][1].isOccupiedBy(),playerA.getPlayerId() * 10 + 1);
        assertEquals(islandBoard.getSpaces()[1][1].isOccupiedBy(),playerB.getPlayerId() * 10);
        assertEquals(islandBoard.getSpaces()[2][1].isOccupiedBy(),-1);
        /* now the islandBoard is like:
        | 0  | 0 | 0 | 0 |B'B|
        | 0  | 0 | 0 | 0 | 0 |
        | 3  | 2 | 1 | 0 | 0 |
        |A'B3|B'A| 0 | 0 | 0 |
        |A'A | 2 | 2 | 0 | 0 |
        */

        // turn to A, A moves workerB UP, not win because "no move up to level 3"
        playerA.getCosplayer().move(1,Direction.UP);
        assertNotEquals(playerA.getCurrentStatus(),PlayerStatus.WIN);
        /* now the islandBoard is like:
        | 0  | 0 | 0 | 0 |B'B|
        | 0  | 0 | 0 | 0 | 0 |
        |A'B3| 2 | 1 | 0 | 0 |
        | 3  |B'A| 0 | 0 | 0 |
        |A'A | 2 | 2 | 0 | 0 |
        */
        // if A move workerB to (0,4), it will set workerB's formLevel3 to false
        playerA.getCosplayer().move(1,Direction.UP);
        assertEquals(playerA.getWorkers()[1].fromLevel(),3);
        playerA.getCosplayer().move(1,Direction.UP);
        assertEquals(playerA.getWorkers()[1].fromLevel(),0);
    }

}
