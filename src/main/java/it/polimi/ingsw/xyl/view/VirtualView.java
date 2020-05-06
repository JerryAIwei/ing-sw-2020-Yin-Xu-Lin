package it.polimi.ingsw.xyl.view;

import it.polimi.ingsw.xyl.controller.GameController;
import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.GameLobby;
import it.polimi.ingsw.xyl.model.VirtualGame;
import it.polimi.ingsw.xyl.model.message.AskPlayerNameMessage;
import it.polimi.ingsw.xyl.model.message.Message;
import it.polimi.ingsw.xyl.model.message.NameOKMessage;
import it.polimi.ingsw.xyl.network.server.PlayerServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class VirtualView {
    private volatile static VirtualView singleton;
    private GameController gameController;
    private final Map<Integer, VirtualGame> vGames;
    private final Map<String, PlayerServer> playerName2PlayerServer;
    private final Map<Integer, Vector<String>> gameID2PlayerNames;
    private boolean test = false;

    private VirtualView() {
        this.vGames = new HashMap<>();
        this.playerName2PlayerServer = new HashMap<>();
        this.gameID2PlayerNames = new HashMap<>();
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
        vGame.setNoLevelUp(gameBoard.getIslandBoard().isNoLevelUp());
        vGame.setPlayerNumber(gameBoard.getPlayerNumber());
        vGame.setGameStatus(gameBoard.getCurrentStatus());
        vGame.setCurrentPlayerId(gameBoard.getCurrentPlayer().getPlayerId());
        vGame.setAvailableGodPowers(gameBoard.getAvailableGodPowers());
        vGame.updateVPlayers(gameBoard.getPlayers().values());
        vGame.setSpaces(gameBoard.getIslandBoard().getSpaces());
        if (!test)
            sendMessage(gameId, vGame);
    }

    public void update(Message message) {
        gameController.update(message);
    }

    public void sendMessage(int gameId, VirtualGame vGame) {
        for (String playerName : gameID2PlayerNames.get(gameId)) {
            try {
                playerName2PlayerServer.get(playerName).sendMessage(vGame);
            }catch (NullPointerException e){
                System.out.println("Re-connection related error");
            }
        }
    }

    // only for test
    public Map<Integer, VirtualGame> getvGames() {
        return vGames;
    }

    // only for test
    public void setTestMode() {
        this.test = true;
    }
}
