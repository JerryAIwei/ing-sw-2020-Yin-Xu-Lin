package it.polimi.ingsw.xyl.controller;

import it.polimi.ingsw.xyl.model.GodPower;
import it.polimi.ingsw.xyl.model.Player;
import it.polimi.ingsw.xyl.model.message.AvailableGodPowersMessage;
import it.polimi.ingsw.xyl.model.message.PlayerNameMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameControllerTest {
    GameController gameController;

    @Before
    public void setUp() {
        gameController = new GameController();
    }

    @After
    public void tearDown() {
        gameController = null;
    }

    @Test
    public void GameControllerTest_addPlayer() {
        // the first player "LiMing" joined
        PlayerNameMessage message = new PlayerNameMessage("LiMing");
        gameController.handleMessage(message);
        Player liMing = gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getPlayers().get(0);
        assertEquals(liMing.getPlayerName(), "LiMing");
        assertEquals(liMing.getCurrentGameBoard().getGameId(),0);
        // the first player who joined to the game is the "owner" of the game
        // so he will decide the total player number(2 or 3) and the available GodPowers
        AvailableGodPowersMessage aMessage = new AvailableGodPowersMessage(0, GodPower.ATHENA, GodPower.APOLLO);
        gameController.handleMessage(aMessage);
        assertTrue(gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getAvailableGodPowers().contains(GodPower.ATHENA));
        assertTrue(gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getAvailableGodPowers().contains(GodPower.APOLLO));
        assertEquals(gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getAvailableGodPowers().size(), 2);
        // the second player who also wanted to use name "LiMing" joined
        PlayerNameMessage message2 = new PlayerNameMessage("LiMing");
        gameController.handleMessage(message2);
        // not allowed same player name, so he failed to join the game
        assertEquals(gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getPlayers().size(), 1);
        // the second player chose another name "LiHua", he joined
        PlayerNameMessage message3 = new PlayerNameMessage("LiHua");
        gameController.handleMessage(message3);
        // the third player "LiGang" joined, but the game of Id 0 is full,
        // so he will be the "owner" of the second game
        PlayerNameMessage message4 = new PlayerNameMessage("LiGang");
        gameController.handleMessage(message4);
        assertEquals(gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(0).getPlayers().size(), 2);
        Player liGang = gameController.getGameMaster().getGameLobby().getGameBoards()
                .get(1).getPlayers().get(0);
        assertEquals(liGang.getPlayerName(), "LiGang");
        assertEquals(liGang.getCurrentGameBoard().getGameId(),1);
    }


}

