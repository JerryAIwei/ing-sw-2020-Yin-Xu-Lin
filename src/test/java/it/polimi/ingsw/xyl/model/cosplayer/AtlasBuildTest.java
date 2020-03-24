package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.Cosplayer;
import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.IslandBoard;
import it.polimi.ingsw.xyl.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.xyl.model.Direction.RIGHT;
import static it.polimi.ingsw.xyl.model.Level.*;
import static org.junit.Assert.assertEquals;

/**
 *  it's a test of cosplayer building(Atlas)
 *  Atlas' build: worker may build a dome at any level.
 *
 *  @author Shaoxun
 */
public class AtlasBuildTest {
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
        cosplayerC = new Atlas(playerC);
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
        playerC is Atlas
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
    public void AtlasBuildTest_playerCWorkerABuildRight_normal(){
        islandBoard.getSpaces()[1][0].setLevel(LEVEL1);
        playerC.getCosplayer().build(0, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[1][0].getLevel(),LEVEL2);
    }

    @Test
    public void AtlasBuildTest_playerCWorkerBBuildUpRight_occupied_2_1_notAllowed() {
        playerC.getCosplayer().build(1, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[2][1].getLevel(),GROUND);
    }

    @Test
    public void AtlasBuildTest_playerCWorkerBBuildUpRight_odome_2_1_notAllowed() {
        islandBoard.getSpaces()[2][1].setLevel(DOME);
        playerC.getCosplayer().build(1, RIGHT, false);
        assertEquals(islandBoard.getSpaces()[2][1].getLevel(),DOME);
    }

    /* GodPower Mod test */

    @Test
    public void AtlasBuildTest_playerCWorkerABuildRightWithDome_usePower(){
        islandBoard.getSpaces()[1][0].setLevel(LEVEL1);
        playerC.getCosplayer().build(0, RIGHT, true);
        assertEquals(islandBoard.getSpaces()[1][0].getLevel(),DOME);
    }

}
