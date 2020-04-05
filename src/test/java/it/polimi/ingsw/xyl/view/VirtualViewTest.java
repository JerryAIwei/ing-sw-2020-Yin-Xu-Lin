package it.polimi.ingsw.xyl.view;

import it.polimi.ingsw.xyl.controller.GameController;
import it.polimi.ingsw.xyl.model.GameStatus;
import it.polimi.ingsw.xyl.model.GodPower;
import it.polimi.ingsw.xyl.model.message.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VirtualViewTest {
    private GameController gc = GameController.getSingleton();
    private VirtualView v = VirtualView.getSingleton();

    @Before
    public void setUp() {
        v.register(gc);
        gc.register(v);
    }

    @After
    public void tearDown() {
        gc.destroy();
        v.destroy();
    }

    @Test
    public void VirtualViewTest_addPlayersAndSetPowers(){
        PlayerNameMessage message = new PlayerNameMessage("LiMing");
        v.update(message);
        SetPlayerNumberMessage sMessage = new SetPlayerNumberMessage(0, 2);
        v.update(sMessage);
        PlayerNameMessage message2 = new PlayerNameMessage("LiHua");
        v.update(message2);
        AvailableGodPowersMessage aMessage = new AvailableGodPowersMessage(0,GodPower.APOLLO,GodPower.ATLAS);
        v.update(aMessage);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getPlayers().get(0).getPlayerName(),
                "LiMing");
        assertEquals(v.getvGames().get(0).getFirstPlayerName(),"LiMing");
        PlayerNameMessage message3 = new PlayerNameMessage("LiHua");
        v.update(message3);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentStatus(),
                GameStatus.WAITINGSTART);
        assertEquals(v.getvGames().size(),1);
        PlayerNameMessage message4 = new PlayerNameMessage("LiGang");
        v.update(message4);
        assertEquals(v.getvGames().size(),2);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(1).getPlayers().get(0).getPlayerName(),
                "LiGang");
        assertEquals(v.getvGames().get(1).getFirstPlayerName(),"LiGang");
    }

    @Test
    public void VirtualViewTest_choosePowerAndSetWorkerPosition() {
        // playerId playerName Cosplayer
        //    0       LiMing    ATHENA
        //    1       LiHua     APOLLO
        //    2       LiGang    DEMETER

        // LiMing LiHua and LiGang joined and LiMing set available powers
        PlayerNameMessage liming = new PlayerNameMessage("LiMing");
        v.update(liming);
        SetPlayerNumberMessage numberM = new SetPlayerNumberMessage(0, 3);
        v.update(numberM);
        PlayerNameMessage lihua = new PlayerNameMessage("LiHua");
        v.update(lihua);
        AvailableGodPowersMessage powersM = new AvailableGodPowersMessage(0,GodPower.APOLLO,GodPower.ATLAS,
                GodPower.DEMETER);
        v.update(powersM);
        PlayerNameMessage ligamg = new PlayerNameMessage("LiGang");
        v.update(ligamg);

        // LiHua, LiGang and LiMing chose his own power.
        int id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        assertEquals(id,1); // LiHua
        PlayerChooseGodPowerMessage liHuaAPOLLO = new PlayerChooseGodPowerMessage(0,id,GodPower.APOLLO);
        v.update(liHuaAPOLLO);
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        assertEquals(id,2);  // LiGang
        PlayerChooseGodPowerMessage liGangDEMETER = new PlayerChooseGodPowerMessage(0,id,GodPower.DEMETER);
        v.update(liGangDEMETER);
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        assertEquals(id,0); //LiMing
        PlayerChooseGodPowerMessage liMingATHENA = new PlayerChooseGodPowerMessage(0, id,GodPower.ATHENA);
        v.update(liMingATHENA);
        // LiMing chose the Start Player
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        assertEquals(id,0);
        StartGameMessage startM = new StartGameMessage(0, "LiMing",0);
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        assertEquals(id,0);
        // LiMing set worker position (0,0) (1,1)
        SetInitialWorkerPositionMessage liMingWM = new SetInitialWorkerPositionMessage(0,id,0,0,1,1);
        v.update(liMingWM);
        // LiHua set worker position (1,3) (2,1)
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        assertEquals(id,1);
        SetInitialWorkerPositionMessage liHuaWM = new SetInitialWorkerPositionMessage(0,id,1,3,2,1);
        v.update(liHuaWM);
        // LiGang set worker position (3,1) (2,3)
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        assertEquals(id,2);
        SetInitialWorkerPositionMessage liGangWM = new SetInitialWorkerPositionMessage(0,id,3,1,2,3);
        v.update(liGangWM);
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        assertEquals(id,0);
    }
}
