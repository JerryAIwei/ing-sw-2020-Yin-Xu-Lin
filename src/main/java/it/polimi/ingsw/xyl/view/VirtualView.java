package it.polimi.ingsw.xyl.view;


import it.polimi.ingsw.xyl.controller.GameController;
import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.GameLobby;
import it.polimi.ingsw.xyl.model.GameStatus;
import it.polimi.ingsw.xyl.model.VirtualGame;
import it.polimi.ingsw.xyl.model.message.Message;
import it.polimi.ingsw.xyl.model.message.NameOKMessage;
import it.polimi.ingsw.xyl.model.message.AskPlayerNameMessage;
import it.polimi.ingsw.xyl.network.server.PlayerServer;
import it.polimi.ingsw.xyl.view.cli.CLI;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class VirtualView {
    private volatile static VirtualView singleton;
    private GameController gameController;
    private final Map<Integer, VirtualGame> vGames = new HashMap<>();

    private final Map<String, PlayerServer> playerName2PlayerServer = new HashMap<>();
    private final Map<Integer, Vector<String>> gameID2PlayerNames = new HashMap<>();
    private CLI cli; //for debug
    private boolean test = false;

    private VirtualView() {
    }

    public static VirtualView getSingleton() {
        if (singleton == null) {
            synchronized (VirtualView.class) {
                if (singleton == null) {
                    singleton = new VirtualView();
                }
            }
        }
        return singleton;
    }

    public void destroy() {
        singleton = null;
    }

    public void register(GameController gc) {
        gameController = gc;
    }

    public void register(CLI cli) {
        this.cli = cli;
        this.test = false;
    }//for debug

    public void register(String ip) {

    }

    public void update(PlayerServer ps, String playerName, GameLobby gl) {
        if (playerName != null) {
            playerName2PlayerServer.put(playerName, ps);
            NameOKMessage nameOkMessage = new NameOKMessage(gl);
            if (!test)
                ps.sendMessage(nameOkMessage);
        } else {
            if (!test)
                ps.sendMessage(new AskPlayerNameMessage());
        }
    }

    public void update(GameBoard gameBoard) {
        int gameId = gameBoard.getGameId();
        gameID2PlayerNames.computeIfAbsent(gameId, k -> new Vector<>());
        if (gameID2PlayerNames.get(gameId).size() < gameBoard.getPlayers().size()) {
            for (String playerName : gameBoard.getAllPlayerNames()) {
                if (!gameID2PlayerNames.get(gameId).contains(playerName))
                    gameID2PlayerNames.get(gameId).add(playerName);
            }
        }
        vGames.computeIfAbsent(gameId, k -> new VirtualGame(gameId));
        VirtualGame vGame = vGames.get(gameBoard.getGameId());
//        VirtualGame vGame = new VirtualGame(gameId);
        vGame.setGameStatus(gameBoard.getCurrentStatus());
        vGame.setCurrentPlayerId(gameBoard.getCurrentPlayer().getPlayerId());
        vGame.setAvailableGodPowers(gameBoard.getAvailableGodPowers());
        vGame.updateVPlayers(gameBoard.getPlayers().values());
        vGame.setSpaces(gameBoard.getIslandBoard().getSpaces());
        if (!test)
            sendMessage(gameId, vGame);

        //cli.update(vGame);//for debug
  /*      if(vGames.get(gameBoard.getGameId()) == null){
            VirtualGame vGame = new VirtualGame();
            vGame.setGameId(gameBoard.getGameId());
            vGame.setGameStatus(gameBoard.getCurrentStatus());
            vGame.setCurrentPlayerId(gameBoard.getCurrentPlayer().getPlayerId());
            vGame.setAvailableGodPowers(gameBoard.getAvailableGodPowers());
            vGame.updateVPlayers(gameBoard.getPlayers().values());
            vGame.setSpaces(gameBoard.getIslandBoard().getSpaces());
            vGames.put(gameBoard.getGameId(), vGame);
            if (debug)
                cli.update(vGame);//for debug
            sendMessage(gameId, vGame);
        }else{
            VirtualGame vGame = vGames.get(gameBoard.getGameId());
            vGame.setGameStatus(gameBoard.getCurrentStatus());
            vGame.setCurrentPlayerId(gameBoard.getCurrentPlayer().getPlayerId());
            vGame.setAvailableGodPowers(gameBoard.getAvailableGodPowers());
            vGame.updateVPlayers(gameBoard.getPlayers().values());
            vGame.setSpaces(gameBoard.getIslandBoard().getSpaces());
            if (debug)
                cli.update(vGame);//for debug
        }
*/
    }

    public void update(Message message) {
        gameController.update(message);
    }


    // only for test
    public Map<Integer, VirtualGame> getvGames() {
        return vGames;
    }

    // only for test
    public void setTestMode() {
        this.test = true;
    }

    public void sendMessage(int gameId, VirtualGame vGame) {
        for (String playerName : gameID2PlayerNames.get(gameId)) {
            playerName2PlayerServer.get(playerName).sendMessage(vGame);
        }
    }
}
