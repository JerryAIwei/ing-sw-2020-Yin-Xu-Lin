package it.polimi.ingsw.xyl.view;

import it.polimi.ingsw.xyl.controller.GameController;
import it.polimi.ingsw.xyl.model.*;
import it.polimi.ingsw.xyl.model.message.*;
import it.polimi.ingsw.xyl.network.server.PlayerServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.*;

public class VirtualView {
    private static final String PLACEHOLDER = "_PLACEHOLDER";
    private volatile static VirtualView singleton;
    private GameController gameController;
    private final Map<Integer, VirtualGame> vGames;
    private final Map<String, PlayerServer> playerName2PlayerServer;
    private final Map<Integer, Vector<String>> gameID2PlayerNames;
    private static final Logger logger = Logger.getLogger("view.VirtualView");
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

    /**
     * After receiving new player name, send message to view
     * if player name is available to use, send all games;
     * if player name is not available, send a re-choose player name request
     * @param ps player server of the player
     * @param playerName player's nickname choice
     * @param gl the whole game lobby, to get all games
     */
    public void update(PlayerServer ps, String playerName, GameLobby gl) {
        if (playerName != null) {
            // normal player name, update playerName2PlayerServer and send nameOkMessage
            playerName2PlayerServer.put(playerName, ps);
            NameOKMessage nameOkMessage = new NameOKMessage(gl);
            if (!test)
                ps.sendMessage(nameOkMessage);
        } else {
            // name is duplicated, re-ask for another name
            if (!test)
                ps.sendMessage(new AskPlayerNameMessage());
        }
    }

    /**
     * After receiving previously existed player name to re-login the game,
     * send message to the player, if all the previous player rejoined the game,
     * send previous vGame to all players, otherwise send a waitingReconnection message.
     * @param ps new player server of the server
     * @param playerName player's previous nickname
     * @param gameBoard the gameBoard of the player
     */
    public void update(PlayerServer ps, String playerName, GameBoard gameBoard){
        playerName2PlayerServer.put(playerName, ps);
        if (gameBoard.getReconnecting()){
            ps.sendMessage(new WaitingReconnectionMessage());
        }else {
            try {
                update(gameBoard);
                int gameId = gameBoard.getGameId();
                logger.log(java.util.logging.Level.INFO, "game " + gameId + " resumed.");
            } catch (NullPointerException e) {
                logger.log(java.util.logging.Level.WARNING, "Re-connection related error.");
            }
        }
    }

    /**
     * GameMaster calls this method to send all necessary data to client.
     *
     * @param gameBoard gameBoard of the game
     */
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
        vGame.save();
        if (!test)
            sendMessage(gameId, vGame);

        GameStatus gameStatus = vGame.getGameStatus();

        if (vGame.getPlayerNumber() == 3){
           var players = vGame.getVPlayers();
            for(var entry : players.entrySet()) {
                if (entry.getValue().getPlayerStatus() == PlayerStatus.LOSE){
                    String playerName = entry.getValue().getPlayerName();
                    Vector<String> playerNames = gameID2PlayerNames.get(gameId);
                    playerNames.remove(playerName);
                    if (playerNames.size() == 2) {
                        playerNames.add(PLACEHOLDER);
                    }
                    gameController.update(new AfterGameMessage(playerName, gameId));
                    break;
                }
            }
        }
        if(gameStatus == GameStatus.GAMEENDED){
            gameController.update(new AfterGameMessage(PLACEHOLDER, gameId));
        }
    }

    /**
     * Update message from server or player server, which is received from the client
     * or created by these servers
     *
     * @param message message
     */
    public void update(Message message) {
        gameController.update(message);
    }

    /**
     * After the game ended by one player won, show all the players the available games.
     *
     * @param gameId game Id
     * @param gl game lobby, for getting all available games
     */
    public void update(int gameId, GameLobby gl) {
        for (String playerName : gameID2PlayerNames.get(gameId)) {
            try {
                playerName2PlayerServer.get(playerName).sendMessage(new NameOKMessage(gl));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * In a 3-player game, when one player lost the game, show him/her the available games.
     *
     * @param playerName player name
     * @param gl game lobby, for getting all available games
     */
    public void update(String playerName, GameLobby gl){
        playerName2PlayerServer.get(playerName).sendMessage(new NameOKMessage(gl));
    }

    /**
     * After server restarts, restore vGames.
     *
     * @param vGame reloaded virtualGame which previously stored in data file
     */
    public void restoreVGames(VirtualGame vGame) {
        vGames.put(vGame.getGameId(), vGame);
    }

    /**
     * To handle unexpected disconnection or exit game.
     *
     * @param gameId game Id
     * @param from the player who exits
     */
    public void notifyToStopGame(int gameId, String from){
        for (String playerName : gameID2PlayerNames.get(gameId)) {
            try {
                playerName2PlayerServer.get(playerName).sendMessage(new ConnectionDroppedMessage(from));
            }catch (NullPointerException ignored){}
        }
    }

    /**
     * Use player server to send virtual game to all the player of the game.
     *
     * @param gameId game Id
     * @param vGame virtual game of the game
     */
    public void sendMessage(int gameId, VirtualGame vGame) {
        for (String playerName : gameID2PlayerNames.get(gameId)) {
            if (PLACEHOLDER.equals(playerName))
                break;
            try {
                playerName2PlayerServer.get(playerName).sendMessage(vGame);
            }catch (NullPointerException e){
                logger.log(java.util.logging.Level.WARNING, "Re-connection related error.");
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
