package it.polimi.ingsw.xyl.controller;

import it.polimi.ingsw.xyl.model.GodPower;
import it.polimi.ingsw.xyl.model.Player;
import it.polimi.ingsw.xyl.model.message.*;
import it.polimi.ingsw.xyl.view.VirtualView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameControllerTest {
    private GameController gameController;
    private VirtualView virtualView;

    @Before
    public void setUp() {
        gameController = GameController.getSingleton();
        virtualView = VirtualView.getSingleton();
        virtualView.register(gameController);
        gameController.register(virtualView);
    }

    @After
    public void tearDown() {
        gameController.destroy();
        virtualView.destroy();

    }

    @Test
    public void GameControllerTest_addPlayer() {
        // the first player "LiMing" joined
        PlayerNameMessage message = new PlayerNameMessage("LiMing");
        gameController.handleMessage(message);
        Player liMing = gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getPlayers().get(0);
        assertEquals(liMing.getPlayerName(), "LiMing");
        assertEquals(liMing.getPlayerId(), 0);
        assertEquals(liMing.getCurrentGameBoard().getGameId(),0);

        // the first player who joined to the game is the "owner" of the game
        // so he will decide the total player number(2 or 3)
        SetPlayerNumberMessage sMessage = new SetPlayerNumberMessage(0, 2);
        gameController.handleMessage(sMessage);
        // the second player who also wanted to use name "LiMing" joined
        PlayerNameMessage message2 = new PlayerNameMessage("LiMing");
        gameController.handleMessage(message2);
        // not allowed same player name, so he failed to join the game
        assertEquals(GameController.getSingleton().getGameMaster().getGameLobby().getGameBoards()
                .get(0).getPlayers().size(), 1);
        // the second player chose another name "LiHua", he joined
        PlayerNameMessage message3 = new PlayerNameMessage("LiHua");
        gameController.handleMessage(message3);
        // the third player "LiGang" joined, but the game of Id 0 is full,
        // so he will be the "owner" of the second game(game 1)
        PlayerNameMessage message4 = new PlayerNameMessage("LiGang");
        gameController.handleMessage(message4);
        assertEquals(gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getPlayers().size(), 2);
        Player liGang = gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(1).getCurrentPlayer();
        assertEquals(liGang.getPlayerName(), "LiGang");
        assertEquals(liGang.getPlayerId(), 0);
        assertEquals(liGang.getCurrentGameBoard().getGameId(),1);
    }

    @Test
    public void GameControllerTest_playerChoosePower(){
        PlayerNameMessage message = new PlayerNameMessage("LiMing");
        gameController.handleMessage(message);
        // the owner "LiMing" set playerNumber 2
        SetPlayerNumberMessage sMessage = new SetPlayerNumberMessage(0, 2);
        gameController.handleMessage(sMessage);
        PlayerNameMessage message2 = new PlayerNameMessage("LiHua");
        gameController.handleMessage(message2);

        // the owner "LiMing" set available god powers ATHENA and APOLLO
        AvailableGodPowersMessage aMessage = new AvailableGodPowersMessage(0, GodPower.ATHENA, GodPower.APOLLO);
        gameController.handleMessage(aMessage);
        assertTrue(gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getAvailableGodPowers().contains(GodPower.ATHENA));
        assertTrue(gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getAvailableGodPowers().contains(GodPower.APOLLO));
        assertEquals(gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getAvailableGodPowers().size(), 2);
        // since LiHua choose all available powers, he will set his own power last
        int id = gameController.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        PlayerChooseGodPowerMessage cMessage = new PlayerChooseGodPowerMessage(0, id,GodPower.APOLLO);
        gameController.handleMessage(cMessage);
        assertEquals(gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getPlayers().get(1).getCosplayer().getGodPower(), GodPower.APOLLO);
        id = gameController.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        PlayerChooseGodPowerMessage cMessage1 = new PlayerChooseGodPowerMessage(0, id,GodPower.ATHENA);
        gameController.handleMessage(cMessage1);
        assertEquals(gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getPlayers().get(0).getCosplayer().getGodPower(), GodPower.ATHENA);
    }

    @Test
    public void GameControllerTest_startGameAndWorkerPosition() {
        // playerId playerName Cosplayer
        //    0       LiMing    ATHENA
        //    1       LiHua     APOLLO
        PlayerNameMessage message = new PlayerNameMessage("LiMing");
        gameController.handleMessage(message);
        SetPlayerNumberMessage sMessage = new SetPlayerNumberMessage(0, 2);
        gameController.handleMessage(sMessage);
        PlayerNameMessage message2 = new PlayerNameMessage("LiHua");
        gameController.handleMessage(message2);
        AvailableGodPowersMessage aMessage = new AvailableGodPowersMessage(0, GodPower.ATHENA, GodPower.APOLLO);
        gameController.handleMessage(aMessage);
        int id = gameController.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        PlayerChooseGodPowerMessage cMessage = new PlayerChooseGodPowerMessage(0, id,GodPower.APOLLO);
        gameController.handleMessage(cMessage);
        id = gameController.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        PlayerChooseGodPowerMessage cMessage1 = new PlayerChooseGodPowerMessage(0, id,GodPower.ATHENA);
        gameController.handleMessage(cMessage1);
        id = gameController.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        StartGameMessage startM = new StartGameMessage(0,"LiMing",1);
        gameController.handleMessage(startM);

        assertEquals(gameController.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId(), 1);

        SetInitialWorkerPositionMessage workerMessage = new SetInitialWorkerPositionMessage(0,1,2,1,1,2);
        gameController.handleMessage(workerMessage);
        SetInitialWorkerPositionMessage workerMessage2 = new SetInitialWorkerPositionMessage(0,0,0,0,1,1);
        gameController.handleMessage(workerMessage2);

        assertEquals(gameController.getGameMaster().getGameLobby().
                getGameBoards().get(0).getIslandBoard().getSpaces()[0][0].isOccupiedBy(),0);
        assertEquals(gameController.getGameMaster().getGameLobby().
                getGameBoards().get(0).getIslandBoard().getSpaces()[1][1].isOccupiedBy(),1);
        assertEquals(gameController.getGameMaster().getGameLobby().
                getGameBoards().get(0).getIslandBoard().getSpaces()[1][2].isOccupiedBy(),11);
        assertEquals(gameController.getGameMaster().getGameLobby().
                getGameBoards().get(0).getIslandBoard().getSpaces()[2][1].isOccupiedBy(),10);
        assertEquals(gameController.getGameMaster().getGameLobby().
                getGameBoards().get(0).getPlayers().get(0).getWorkers()[0].getPositionX(),0);
        assertEquals(gameController.getGameMaster().getGameLobby().
                getGameBoards().get(0).getPlayers().get(0).getWorkers()[0].getPositionY(),0);
        assertEquals(gameController.getGameMaster().getGameLobby().
                getGameBoards().get(0).getPlayers().get(0).getWorkers()[1].getPositionX(),1);
        assertEquals(gameController.getGameMaster().getGameLobby().
                getGameBoards().get(0).getPlayers().get(0).getWorkers()[1].getPositionY(),1);
    }

}

