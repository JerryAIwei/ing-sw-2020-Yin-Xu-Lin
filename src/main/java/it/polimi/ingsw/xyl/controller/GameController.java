package it.polimi.ingsw.xyl.controller;

import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.GameMaster;
import it.polimi.ingsw.xyl.model.GodPower;
import it.polimi.ingsw.xyl.model.message.*;
import it.polimi.ingsw.xyl.view.VirtualView;

import java.util.Vector;

/**
 * This class represents the controller of MVC pattern
 *
 * @author Shaoxun
 */
public class GameController {
    private volatile static GameController singleton;
    private final GameMaster gameMaster = new GameMaster();

    private GameController() { }

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
        int a = gameMaster.addPlayer(playerName);
        if (a == 0) {
            // notify view to set playerNumber and Available God powers
            // notify view the gameId
        } else if (a == 1) {
            // notify view to wait for other players or game start
            // notify view the gameId
        } else {
            // there is an error
        }
    }

    public void handleMessage(SetPlayerNumberMessage message){
        int gameId = message.getGameId();
        int playerNumber = message.getPlayerNumber();
        if (gameMaster.setPlayerNumber(gameId, playerNumber) == 1)
        {

        }else{

        }
    }

    public void handleMessage(AvailableGodPowersMessage message) {
        int gameId = message.getGameId();
        Vector<GodPower> availableGodPowers = new Vector<>();
        availableGodPowers.add(message.getGodPower('A'));
        availableGodPowers.add(message.getGodPower('B'));
        if (message.getGodPower('C') != null) {
            availableGodPowers.add(message.getGodPower('C'));
        }

        if (gameMaster.setAvailableGodPowers(gameId, availableGodPowers) == 1) {

        } else {

        }
    }

    public void handleMessage(PlayerChooseGodPowerMessage message) {
        int gameId = message.getGameId();
        int playerId = message.getPlayerId();
        GodPower godPower = message.getGodPower();
        gameMaster.setPower4Player(gameId, playerId, godPower);
//        if (gameMaster.setPower4Player(gameId, playerId, godPower) == 1) {
//
//        } else {
//
//        }
    }

    public void handleMessage(StartGameMessage message) {
        int gameId = message.getGameId();
        String from = message.getFromPlayer();
        int startPlayerId = message.getStartPlayerId();
        if (gameMaster.startGame(gameId, from, startPlayerId) == 1) {

        } else {

        }
    }

    public void handleMessage(SetInitialWorkerPositionMessage message) {
        int gameId = message.getGameId();
        int playerId = message.getPlayerId();
        int ax = message.getWorkerPosition(0, 'x');
        int ay = message.getWorkerPosition(0, 'y');
        int bx = message.getWorkerPosition(1, 'x');
        int by = message.getWorkerPosition(1, 'y');
        if (gameMaster.setInitialWorkerPosition(gameId, playerId, ax, ay, bx, by) == 1) {

        } else {

        }
    }

    public void handleMessage(MyTurnFinishedMessage message) {
        int gameId = message.getGameId();
        int playerId = message.getPlayerId();
        boolean finish = message.isFinished();
        if (gameMaster.endTurn(gameId, playerId, finish) == 1) {

        } else {

        }
    }

    public void handleMessage(MoveMessage message) {
        int gameId = message.getGameId();
        int playerId = message.getPlayerId();
        int workerId = message.getWorkerId();
        Direction direction = message.getDirection();
        if (gameMaster.handleMove(gameId, playerId, workerId, direction) == 1) {

        } else {

        }
    }

    public void handleMessage(BuildMessage message) {
        int gameId = message.getGameId();
        int playerId = message.getPlayerId();
        int workerId = message.getWorkerId();
        Direction direction = message.getDirection();
        boolean buildDome = message.isBuildDome();
        if (gameMaster.handleBuild(gameId, playerId, workerId, direction, buildDome) == 1) {

        } else {

        }
    }

    public void update(Message message) {
        if (message.getClass() == PlayerNameMessage.class)
            handleMessage((PlayerNameMessage) message);
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