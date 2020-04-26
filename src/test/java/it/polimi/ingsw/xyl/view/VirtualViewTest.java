package it.polimi.ingsw.xyl.view;

import it.polimi.ingsw.xyl.controller.GameController;
import it.polimi.ingsw.xyl.model.*;
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
        v.setTestMode();
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
        CreateNewGameMessage createNewGameMessage = new CreateNewGameMessage("LiMing");
        v.update(createNewGameMessage);
        SetPlayerNumberMessage sMessage = new SetPlayerNumberMessage(0, 2);
        v.update(sMessage);
        PlayerNameMessage message2 = new PlayerNameMessage("LiHua");
        v.update(message2);
        JoinGameMessage joinM = new JoinGameMessage("LiHua",0);
        v.update(joinM);
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
        createNewGameMessage = new CreateNewGameMessage("LiGang");
        v.update(createNewGameMessage);
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

        // LiMing LiHua and LiGang joined and then LiMing set available powers
        PlayerNameMessage liming = new PlayerNameMessage("LiMing");
        v.update(liming);
        CreateNewGameMessage createNewGameMessage = new CreateNewGameMessage("LiMing");
        v.update(createNewGameMessage);
        SetPlayerNumberMessage numberM = new SetPlayerNumberMessage(0, 3);
        v.update(numberM);
        PlayerNameMessage lihua = new PlayerNameMessage("LiHua");
        v.update(lihua);
        PlayerNameMessage ligamg = new PlayerNameMessage("LiGang");
        v.update(ligamg);
        JoinGameMessage joidM = new JoinGameMessage("LiHua",0);
        v.update(joidM);
        joidM = new JoinGameMessage("LiGang",0);
        v.update(joidM);
        AvailableGodPowersMessage powersM = new AvailableGodPowersMessage(0,GodPower.APOLLO,GodPower.ATHENA,
                GodPower.DEMETER);
        v.update(powersM);

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
        v.update(startM);
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
        int currentIdInVGame = v.getvGames().get(0).getCurrentPlayerId();
        assertEquals(id,currentIdInVGame);
        String currentPlayerAction = v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals("MOVE", currentPlayerAction);
        assertEquals(v.getvGames().get(0).getGameStatus(),GameStatus.INGAME);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentStatus(),GameStatus.INGAME);
    }

    @Test
    public void VirtualViewTest_wholeGame_withPowerAtlasApolloPan_PanWins() {
        // playerId playerName Cosplayer
        //    0       LiMing    ATLAS
        //    1       LiHua     APOLLO
        //    2       LiGang    PAN
        PlayerNameMessage liming = new PlayerNameMessage("LiMing0");
        v.update(liming);
        CreateNewGameMessage createNewGameMessage = new CreateNewGameMessage("LiMing0");
        v.update(createNewGameMessage);
        SetPlayerNumberMessage numberM = new SetPlayerNumberMessage(0, 3);
        v.update(numberM);
        PlayerNameMessage lihua = new PlayerNameMessage("LiHua1");
        v.update(lihua);
        PlayerNameMessage ligamg = new PlayerNameMessage("LiGang2");
        v.update(ligamg);
        JoinGameMessage joidM = new JoinGameMessage("LiHua1",0);
        v.update(joidM);
        joidM = new JoinGameMessage("LiGang2",0);
        v.update(joidM);
        AvailableGodPowersMessage powersM = new AvailableGodPowersMessage(0, GodPower.APOLLO, GodPower.ATLAS,
                GodPower.PAN);
        v.update(powersM);
        // LiHua, LiGang and LiMing chose his own power.
        int id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        PlayerChooseGodPowerMessage liHuaAPOLLO = new PlayerChooseGodPowerMessage(0,id,GodPower.APOLLO);
        v.update(liHuaAPOLLO);
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        PlayerChooseGodPowerMessage liGangPAN = new PlayerChooseGodPowerMessage(0,id,GodPower.PAN);
        v.update(liGangPAN);
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        PlayerChooseGodPowerMessage liMingATLAS = new PlayerChooseGodPowerMessage(0, id,GodPower.ATLAS);
        v.update(liMingATLAS);
        // LiMing chose the Start Player
        StartGameMessage startM = new StartGameMessage(0, "LiMing0",0);
        v.update(startM);
        // LiMing set worker position (0,0) (1,1)
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        SetInitialWorkerPositionMessage liMingWM = new SetInitialWorkerPositionMessage(0,id,0,0,1,1);
        v.update(liMingWM);
        // LiHua set worker position (1,3) (2,1)
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        SetInitialWorkerPositionMessage liHuaWM = new SetInitialWorkerPositionMessage(0,id,1,3,2,1);
        v.update(liHuaWM);
        // LiGang set worker position (3,1) (2,3)
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        SetInitialWorkerPositionMessage liGangWM = new SetInitialWorkerPositionMessage(0,id,3,1,2,3);
        v.update(liGangWM);
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        String nextAction =
                gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getCosplayer().getNextAction();
        String nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        // turn to LiMing move
        assertEquals(id,0);
        assertEquals(nextAction,nextActionInVGame);
        assertEquals(nextAction,"MOVE");
        // LiMing moves workerA right
        MoveMessage moveM = new MoveMessage(0,id,0, Direction.RIGHT);
        v.update(moveM);
        // turn to LiMing build
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextAction =
                gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getCosplayer().getNextAction();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,0);
        assertEquals(nextAction,nextActionInVGame);
        assertEquals(nextAction,"BUILD");
        int workerInAction =
                gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getCosplayer().getWorkerInAction();
        int workerInActionInVGame = v.getvGames().get(0).getCurrentPlayerWorkerInAction();
        assertEquals(workerInAction,0);
        assertEquals(workerInActionInVGame,0);
        // LiMing uses workerA to build Right
        BuildMessage buildM = new BuildMessage(0,id,0,Direction.RIGHT,false);
        v.update(buildM);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getPlayers().get(0).getWorkers()[0].getPositionX(),1);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getPlayers().get(0).getWorkers()[0].getPositionY(),0);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[2][0].getLevel().toInt(),1);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[1][0].isOccupiedBy(),0);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[0][0].isOccupiedBy(),-1);
        // turn to LiHua move
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        assertEquals(id,1);
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(nextActionInVGame,"MOVE");
        // LiHua move workerB left(use power, change position with LiMing's workerB(01))
        moveM = new MoveMessage(0,id,1,Direction.LEFT);
        v.update(moveM);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getPlayers().get(1).getWorkers()[1].getPositionX(),1);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getPlayers().get(0).getWorkers()[1].getPositionX(),2);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[1][1].isOccupiedBy(),11); //LiHua's workerB
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[2][1].isOccupiedBy(),1); //LiMing's workerB
        // turn to LiHua build
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,1);
        assertEquals(nextActionInVGame,"BUILD");
        // LiHua use workerB to build downleft
        buildM = new BuildMessage(0,id,1,Direction.DOWNLEFT,false);
        v.update(buildM);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[0][0].getLevel().toInt(),1);
        // turn to LiGang move
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,2);
        assertEquals(nextActionInVGame,"MOVE");
        // LiGang moves workerB down
        moveM = new MoveMessage(0,id,1,Direction.DOWN);
        v.update(moveM);
        // turn to LiGang build
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,2);
        assertEquals(nextActionInVGame,"BUILD");
        // LiGang workerB build left
        buildM = new BuildMessage(0,id,1,Direction.LEFT,false);
        v.update(buildM);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[1][2].getLevel().toInt(),1);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[2][2].isOccupiedBy(),21);  // LiGang's workerB
        // turn to LiMing move
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,0);
        assertEquals(nextActionInVGame,"MOVE");
        // LiMing moves workerB downright and builds a dome left
        moveM = new MoveMessage(0,id,1,Direction.DOWNRIGHT);
        v.update(moveM);
        buildM = new BuildMessage(0,id,1,Direction.LEFT,true);
        v.update(buildM);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[2][0].getLevel().toInt(),4);
        // turn to LiHua move
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,1);
        assertEquals(nextActionInVGame,"MOVE");
        // LiHua moves workerB up and builds down
        moveM = new MoveMessage(0,id,1,Direction.UP);
        v.update(moveM);
        buildM = new BuildMessage(0,id,1,Direction.DOWN,false);
        v.update(buildM);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[1][2].isOccupiedBy(),11);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[1][1].isOccupiedBy(),-1);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[1][1].getLevel().toInt(), 1);
        // turn to LiGang move
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,2);
        assertEquals(nextActionInVGame,"MOVE");
        // LiGang moves workerB downleft and builds right
        moveM = new MoveMessage(0,id,1,Direction.DOWNLEFT);
        v.update(moveM);
        buildM = new BuildMessage(0,id,1,Direction.RIGHT,false);
        v.update(buildM);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[1][1].isOccupiedBy(),21);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[2][1].getLevel().toInt(), 1);
        // turn to LiMing move
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,0);
        assertEquals(nextActionInVGame,"MOVE");
        // LiMing moves workerA upleft and builds down
        moveM = new MoveMessage(0,id,0,Direction.UPLEFT);
        v.update(moveM);
        buildM = new BuildMessage(0,id,0,Direction.DOWN,false);
        v.update(buildM);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[0][0].getLevel().toInt(),2);
        // turn to LiHua move
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,1);
        assertEquals(nextActionInVGame,"MOVE");
        // LiHua moves workerA right and builds right
        moveM = new MoveMessage(0,id,0,Direction.RIGHT);
        v.update(moveM);
        buildM = new BuildMessage(0,id,0,Direction.RIGHT,false);
        v.update(buildM);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[3][3].getLevel().toInt(),1);
        // turn to LiGang move
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,2);
        assertEquals(nextActionInVGame,"MOVE");
        // LiGang moves workerB downleft and builds upright
        moveM = new MoveMessage(0,id,1,Direction.DOWNLEFT);
        v.update(moveM);
        buildM = new BuildMessage(0,id,1,Direction.UPRIGHT,false);
        v.update(buildM);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces()[1][1].getLevel().toInt(),2);
        // turn to LiMing move
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,0);
        assertEquals(nextActionInVGame,"MOVE");
        // LiMing moves workerA up and builds up
        moveM = new MoveMessage(0,id,0,Direction.UP);
        v.update(moveM);
        buildM = new BuildMessage(0,id,0,Direction.UP,false);
        v.update(buildM);
        // turn to LiHua move
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,1);
        assertEquals(nextActionInVGame,"MOVE");
        // LiHua moves workerA right and builds right
        moveM = new MoveMessage(0,id,0,Direction.RIGHT);
        v.update(moveM);
        buildM = new BuildMessage(0,id,0,Direction.RIGHT,false);
        v.update(buildM);
        // turn to LiGang move
        id = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentPlayer().getPlayerId();
        nextActionInVGame =
                v.getvGames().get(0).getCurrentPlayerAction();
        assertEquals(id,2);
        assertEquals(nextActionInVGame,"MOVE");
        // LiGang moves workerB right and wins the game
        moveM = new MoveMessage(0,id,1,Direction.RIGHT);
        v.update(moveM);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getPlayers().get(2).getCurrentStatus(),
                PlayerStatus.WIN);
        assertEquals(gc.getGameMaster().getGameLobby().getGameBoards().get(0).getCurrentStatus(),GameStatus.GAMEENDED);
        // final check
        Space[][] space = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getIslandBoard().getSpaces();
        assertEquals(space[0][0].getLevel(),Level.LEVEL2);
        assertEquals(space[2][0].getLevel(),Level.DOME);
        assertEquals(space[1][1].getLevel(),Level.LEVEL2);
        assertEquals(space[2][1].getLevel(),Level.LEVEL1);
        assertEquals(space[1][2].getLevel(),Level.LEVEL1);
        assertEquals(space[0][3].getLevel(),Level.LEVEL1);
        assertEquals(space[3][3].getLevel(),Level.LEVEL1);
        assertEquals(space[4][3].getLevel(),Level.LEVEL1);
        assertEquals(space[1][0].isOccupiedBy(),21);
        assertEquals(space[3][0].isOccupiedBy(),1);
        assertEquals(space[3][1].isOccupiedBy(),20);
        assertEquals(space[0][2].isOccupiedBy(),0);
        assertEquals(space[1][2].isOccupiedBy(),11);
        assertEquals(space[3][3].isOccupiedBy(),10);
        Player a = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getPlayers().get(0);
        assertEquals(a.getWorkers()[0].getPositionX(),0);
        assertEquals(a.getWorkers()[0].getPositionY(),2);
        assertEquals(a.getWorkers()[1].getPositionX(),3);
        assertEquals(a.getWorkers()[1].getPositionY(),0);
        Player b = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getPlayers().get(1);
        assertEquals(b.getWorkers()[0].getPositionX(),3);
        assertEquals(b.getWorkers()[0].getPositionY(),3);
        assertEquals(b.getWorkers()[1].getPositionX(),1);
        assertEquals(b.getWorkers()[1].getPositionY(),2);
        Player c = gc.getGameMaster().getGameLobby().getGameBoards().get(0).getPlayers().get(2);
        assertEquals(c.getWorkers()[0].getPositionX(),3);
        assertEquals(c.getWorkers()[0].getPositionY(),1);
        assertEquals(c.getWorkers()[1].getPositionX(),1);
        assertEquals(c.getWorkers()[1].getPositionY(),0);
        // By using debug tools, get the same island board as expected.
    }
}
