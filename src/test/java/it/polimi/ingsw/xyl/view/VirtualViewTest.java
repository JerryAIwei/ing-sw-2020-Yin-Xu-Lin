package it.polimi.ingsw.xyl.view;

import it.polimi.ingsw.xyl.controller.GameController;
import it.polimi.ingsw.xyl.model.GameStatus;
import it.polimi.ingsw.xyl.model.GodPower;
import it.polimi.ingsw.xyl.model.message.AvailableGodPowersMessage;
import it.polimi.ingsw.xyl.model.message.PlayerNameMessage;
import it.polimi.ingsw.xyl.model.message.SetPlayerNumberMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VirtualViewTest {

    @Test
    public void test(){
        GameController gc = new GameController();
        VirtualView v = new VirtualView(gc);
        gc.getGameMaster().register(v);


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
}
