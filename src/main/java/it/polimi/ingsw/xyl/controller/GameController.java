package it.polimi.ingsw.xyl.controller;

import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.GameMaster;
import it.polimi.ingsw.xyl.model.GodPower;
import it.polimi.ingsw.xyl.model.message.*;
import it.polimi.ingsw.xyl.network.server.PlayerServer;
import it.polimi.ingsw.xyl.view.VirtualView;

import java.util.ArrayList;


/**
 * This class represents the controller of MVC pattern
 *
 * @author Shaoxun
 */
public class GameController {
    private volatile static GameController singleton;
    private final GameMaster gameMaster;

    private GameController() {
        gameMaster = new GameMaster();
    }

    public static GameController getSingleton() {
        if (singleton == null) {
            synchronized (GameController.class) {
                if (singleton == null) {
                    singleton = new GameController();
                }
            }
        }
        return singleton;
    }

    public void destroy(){
        singleton = null;
    }

    public void register(VirtualView v){
        gameMaster.register(v);
    }

    // only for test
    public GameMaster getGameMaster() {
        return gameMaster;
    }

    public void handleMessage(PlayerNameMessage message) {
        String playerName = message.getPlayerName();
        PlayerServer ps = message.getPs();
        gameMaster.addPlayer(ps, playerName);
    }

    public void handleMessage(CreateNewGameMessage createNewGameMessage){
        String playerName = createNewGameMessage.getPlayerName();
        gameMaster.createNewGame(playerName);
    }

    public void handleMessage(JoinGameMessage joinGameMessage){
        String playerName = joinGameMessage.getPlayerName();
        int gameId = joinGameMessage.getGameID();
        gameMaster.joinGame(playerName, gameId);
    }

    public void handleMessage(SetPlayerNumberMessage message){
        int gameId = message.getGameId();
        int playerNumber = message.getPlayerNumber();
        gameMaster.setPlayerNumber(gameId, playerNumber);
    }

    public void handleMessage(AvailableGodPowersMessage message) {
        int gameId = message.getGameId();
        ArrayList<GodPower> availableGodPowers = new ArrayList<>();
        availableGodPowers.add(message.getGodPower('A'));
        availableGodPowers.add(message.getGodPower('B'));
        if (message.getGodPower('C') != null) {
            availableGodPowers.add(message.getGodPower('C'));
        }
        gameMaster.setAvailableGodPowers(gameId, availableGodPowers);
    }

    public void handleMessage(PlayerChooseGodPowerMessage message) {
        int gameId = message.getGameId();
        int playerId = message.getPlayerId();
        GodPower godPower = message.getGodPower();
        gameMaster.setPower4Player(gameId, playerId, godPower);
    }

    public void handleMessage(StartGameMessage message) {
        int gameId = message.getGameId();
        String from = message.getFromPlayer();
        int startPlayerId = message.getStartPlayerId();
        gameMaster.startGame(gameId, from, startPlayerId);
    }

    public void handleMessage(SetInitialWorkerPositionMessage message) {
        int gameId = message.getGameId();
        int playerId = message.getPlayerId();
        int ax = message.getWorkerPosition(0, 'x');
        int ay = message.getWorkerPosition(0, 'y');
        int bx = message.getWorkerPosition(1, 'x');
        int by = message.getWorkerPosition(1, 'y');
        gameMaster.setInitialWorkerPosition(gameId, playerId, ax, ay, bx, by);
    }

    public void handleMessage(MyTurnFinishedMessage message) {
        int gameId = message.getGameId();
        int playerId = message.getPlayerId();
        boolean finish = message.isFinished();
        gameMaster.endTurn(gameId, playerId, finish);
    }

    public void handleMessage(MoveMessage message) {
        int gameId = message.getGameId();
        int playerId = message.getPlayerId();
        int workerId = message.getWorkerId();
        Direction direction = message.getDirection();
        gameMaster.handleMove(gameId, playerId, workerId, direction);
    }

    public void handleMessage(BuildMessage message) {
        int gameId = message.getGameId();
        int playerId = message.getPlayerId();
        int workerId = message.getWorkerId();
        Direction direction = message.getDirection();
        boolean buildDome = message.isBuildDome();
        gameMaster.handleBuild(gameId, playerId, workerId, direction, buildDome);
    }

    public void update(Message message) {
        if (message.getClass() == PlayerNameMessage.class)
            handleMessage((PlayerNameMessage) message);
        else if (message.getClass() == CreateNewGameMessage.class)
            handleMessage((CreateNewGameMessage) message);
        else if (message.getClass() == JoinGameMessage.class)
            handleMessage((JoinGameMessage) message);
        else if (message.getClass() == SetPlayerNumberMessage.class)
            handleMessage((SetPlayerNumberMessage) message);
        else if (message.getClass() == AvailableGodPowersMessage.class)
            handleMessage((AvailableGodPowersMessage) message);
        else if (message.getClass() == PlayerChooseGodPowerMessage.class)
            handleMessage((PlayerChooseGodPowerMessage) message);
        else if (message.getClass() == StartGameMessage.class)
            handleMessage((StartGameMessage) message);
        else if (message.getClass() == SetInitialWorkerPositionMessage.class)
            handleMessage((SetInitialWorkerPositionMessage) message);
        else if (message.getClass() == MyTurnFinishedMessage.class)
            handleMessage((MyTurnFinishedMessage) message);
        else if (message.getClass() == MoveMessage.class)
            handleMessage((MoveMessage) message);
        else if (message.getClass() == BuildMessage.class)
            handleMessage((BuildMessage) message);
    }

}
