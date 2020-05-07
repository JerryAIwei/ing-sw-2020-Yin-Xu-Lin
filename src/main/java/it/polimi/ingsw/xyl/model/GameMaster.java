package it.polimi.ingsw.xyl.model;

import it.polimi.ingsw.xyl.network.server.PlayerServer;
import it.polimi.ingsw.xyl.view.VirtualView;

import java.io.*;
import java.util.ArrayList;


/**
 * GameMaster is virtual model handle all the other models,
 * just like a game master (person) can handle all the data of a game.
 *
 * @author Shaoxun
 */
public class GameMaster {
    private static final String RECONNECTION = "_RECONNECTION";
    private final GameLobby gameLobby;
    private final ArrayList<VirtualView> observerV = new ArrayList<>();
    private final static int NO_GAME_ID = -1;

    public GameMaster() {
        this.gameLobby = new GameLobby();
    }

    /**
     * This method is only used for test, it should never be used in anywhere else.
     *
     * @return the game lobby.
     */
    public GameLobby getGameLobby() {
        return gameLobby;
    }

    public void register(VirtualView observer){
        synchronized (observerV) {
            observerV.add(observer);
        }
    }

    public void deregister(VirtualView observer){
        synchronized (observerV) {
            observerV.remove(observer);
        }
    }


    /**
     * This method is used by controller to handle with new player's connect.
     *
     * @param ps PlayerServer of a player, used to send messages
     * @param playerName the nickname of a player.
     */
    public synchronized void addPlayer(PlayerServer ps, String playerName){
        String result = gameLobby.add2AllPlayers(playerName, NO_GAME_ID);
        if (RECONNECTION.equals(result)){
            int gameId = gameLobby.getAllPlayers().get(playerName);
            GameBoard gameBoard = gameLobby.getGameBoards().get(gameId);
            gameBoard.getPlayer(playerName).setReconnecting(false);
            boolean allReconnected = true;
            int playerSize = gameBoard.getPlayers().size();
            for (int i = 0; i < playerSize; i++){
                if (gameBoard.getPlayers().get(i).getReconnecting()) {
                    allReconnected = false;
                }
            }
            if(allReconnected) {
                gameLobby.getGameBoards().get(gameId).setReconnecting(false);
            }
            notify(ps,playerName,gameId);
        }else{
            notify(ps, result);
        }
    }

    /**
     * To create a new game
     * @param playerName player name
     */
    public synchronized void createNewGame(String playerName){
        int gameId = gameLobby.getGameBoards().size();
        GameBoard gameBoard = new GameBoard(gameId);
        // set the game status to "waiting other players"
        gameBoard.setCurrentStatus(GameStatus.WAITINGINIT);
        Player player = new Player(0, playerName);
        // set the player's status "in gameBoard"
        player.setCurrentStatus(PlayerStatus.INGAMEBOARD);
        gameBoard.addPlayer(player);
        player.setCurrentGameBoard(gameBoard);
        gameLobby.getAllPlayers().replace(playerName,gameId);
        gameLobby.addGameBoard(gameBoard);
        notify(gameId);
    }

    /**
     * To join a player into a game
     *
     * @param playerName player name
     * @param gameId game ID
     */
    public synchronized void joinGame(String playerName, int gameId){
        GameBoard gameBoard = gameLobby.getGameBoards().get(gameId);
        if(gameBoard != null && gameBoard.getPlayers().size() < gameBoard.getPlayerNumber()) {
            int playerId = gameBoard.getPlayers().size();
            Player player = new Player(playerId, playerName);
            // set the player's status "in gameBoard"
            player.setCurrentStatus(PlayerStatus.INGAMEBOARD);
            gameLobby.getAllPlayers().replace(playerName,gameId);
            gameBoard.addPlayer(player);
            player.setCurrentGameBoard(gameBoard);
            // if the number of players joined to the game equals to playerNumber
            // set the game status "waiting start"
            if (gameBoard.getPlayerNumber() == gameBoard.getPlayers().size())
                gameBoard.setCurrentStatus(GameStatus.WAITINGSTART);
            notify(gameId);
        }
    }

    /**
     * To set total player number
     *
     * @param gameId       game ID.
     * @param playerNumber how may players are there in this gameBoard.
     */
    public void setPlayerNumber(int gameId, int playerNumber) {
        gameLobby.getGameBoards().get(gameId).setPlayerNumber(playerNumber);
        gameLobby.getGameBoards().get(gameId).setCurrentStatus(GameStatus.WAITINGPLAYER);
        notify(gameId);
    }

    /**
     * This method sets available God powers of gameBoard.
     *
     * @param gameId             game ID.
     * @param availableGodPowers all available powers.
     */
    public void setAvailableGodPowers(int gameId, ArrayList<GodPower> availableGodPowers) {
        for (GodPower godPower : availableGodPowers)
            gameLobby.getGameBoards().get(gameId).addAvailableGodPowers(godPower);
        gameLobby.getGameBoards().get(gameId).toNextPlayer();
        notify(gameId);
    }

    /**
     * This method sets a player's God Player.
     *
     * @param gameId   game ID.
     * @param playerId player ID.
     * @param godPower the God power player chose.
     */
    public void setPower4Player(int gameId, int playerId, GodPower godPower) {
        // chosen God power should be available
        ArrayList<GodPower> availableGodPowers = gameLobby.getGameBoards().get(gameId).getAvailableGodPowers();
        if (availableGodPowers.contains(godPower)) {
            Player player = gameLobby.getGameBoards().get(gameId).getPlayers().get(playerId);
            Cosplayer cosplayer = godPower.cosplay(player);
            player.setCosplayer(cosplayer);
            gameLobby.getGameBoards().get(gameId).getAvailableGodPowers().remove(godPower);
            player.setCurrentStatus(PlayerStatus.GODPOWERED);
            if (gameLobby.getGameBoards().get(gameId).getCurrentPlayer().getPlayerId() == 0)
                player.setCurrentStatus(PlayerStatus.WAITING4START);
            else
                gameLobby.getGameBoards().get(gameId).toNextPlayer();
            notify(gameId);
        }
    }

    /**
     * This method changes the turn id of a gameBoard to the "Start Player" which is chosen by
     * the "Challenger", in our case, the "owner" of the gameBoard.
     *
     * @param gameId        game ID.
     * @param messageFrom   which player this message is form
     * @param startPlayerId the Start Player Id
     */
    public void startGame(int gameId, String messageFrom, int startPlayerId) {
        // only the "owner" of the gameBoard can decide from whom the game will start.
        if (gameLobby.getGameBoards().get(gameId).getPlayers().get(0).getPlayerName().equals(messageFrom)) {
            gameLobby.getGameBoards().get(gameId).getPlayers().forEach((key, value) -> value.setCurrentStatus(PlayerStatus.WAITING4INIT));
            gameLobby.getGameBoards().get(gameId).toNextPlayer(startPlayerId);
            gameLobby.getGameBoards().get(gameId).setCurrentStatus(GameStatus.INGAME);
            notify(gameId);
        }
    }

    /**
     * To set workers' initial position of the player.
     *
     * @param gameId   game ID.
     * @param playerId player ID.
     * @param ax       the x coordinate of worker 0(A)
     * @param ay       the y coordinate of worker 0
     * @param bx       the x coordinate of worker 1(B)
     * @param by       the y coordinate of worker 1(B)
     */
    public void setInitialWorkerPosition(int gameId, int playerId, int ax, int ay, int bx, int by) {
        GameBoard gameBoard = gameLobby.getGameBoards().get(gameId);
        Player player = gameBoard.getPlayers().get(playerId);
        IslandBoard islandBoard = gameBoard.getIslandBoard();
        if (islandBoard.getSpaces()[ax][ay].isOccupiedBy() == -1
                && islandBoard.getSpaces()[bx][by].isOccupiedBy() == -1) {
            // set worker position
            player.setWorkers(ax, ay, bx, by);
            // set occupied
            islandBoard.getSpaces()[ax][ay].setOccupiedBy(playerId * 10);
            islandBoard.getSpaces()[bx][by].setOccupiedBy(playerId * 10 +1);

            gameBoard.toNextPlayer();
            notify(gameId);
        }
    }

    /**
     * To end one's turn when no more want to use God power
     *
     * @param gameId   game ID.
     * @param playerId player ID.
     * @param finish   end my turn
     */
    public void endTurn(int gameId, int playerId, boolean finish) {
        int currentPlayerId = gameLobby.getGameBoards().get(gameId).getCurrentPlayer().getPlayerId();
        if (currentPlayerId == playerId && finish) {
            gameLobby.getGameBoards().get(gameId).toNextPlayer();
            notify(gameId);
        }
    }

    /**
     * To handle player's "move" action
     *
     * @param gameId    game ID.
     * @param playerId  player ID.
     * @param workerId  worker ID.
     * @param direction direction
     */
    public void handleMove(int gameId, int playerId, int workerId, Direction direction) {
        ArrayList<Direction> availableMoves =
                gameLobby.getGameBoards().get(gameId).getPlayers().get(playerId).getCosplayer().getAvailableMoves(workerId);
        if (availableMoves.contains(direction)) {
            gameLobby.getGameBoards().get(gameId).getPlayers().get(playerId).getCosplayer().move(workerId, direction);
            notify(gameId);
        }
    }

    /**
     * To handle player's "Build" action
     *
     * @param gameId    game ID.
     * @param playerId  player ID.
     * @param workerId  worker ID.
     * @param direction direction.
     * @param buildDome whether build dome directly (only for Atlas).
     */
    public void handleBuild(int gameId, int playerId, int workerId, Direction direction, boolean buildDome) {
        gameLobby.getGameBoards().get(gameId).getPlayers().get(playerId).getCosplayer().build(workerId, direction,
                buildDome);
        notify(gameId);
    }

    /**
     * loadData is for persistence, the server will firstly
     * load previous saved data file in ./data to restore the
     * previous status of the server after the unexpected
     * crash of the server
     */
    public void loadData(){
        int gameId = 0;
        VirtualGame vGame = loadVirtualGame(gameId);
        while (vGame != null){
            IslandBoard islandBoard = new IslandBoard(vGame.getSpaces());
            islandBoard.setNoLevelUp(vGame.isNoLevelUp());
            GameBoard gameBoard = new GameBoard(gameId,vGame.getPlayerNumber(),islandBoard);
            VirtualGame.VPlayer vPlayer;
            for (int i = 0; i < vGame.getVPlayers().size(); i++){
                vPlayer = vGame.getVPlayers().get(i);
                Player player = new Player(vPlayer.playerId, vPlayer.playerName);
                player.setCosplayer(vPlayer.getGodPower().cosplay(player));
                player.restoreWorkers(vPlayer.getWorkers().clone());
                if (vPlayer.getNextAction() != null)
                    player.getCosplayer().restoreNextAction(vPlayer.getNextAction());
                player.getCosplayer().restoreWorkerInAction(vPlayer.getWorkerInAction());
                player.setCurrentGameBoard(gameBoard);
                player.setReconnecting(true);
                gameBoard.addPlayer(player);  // addPlayer will set player status to INGAMEBOARD !!
                player.setCurrentStatus(vPlayer.getPlayerStatus());
                gameLobby.add2AllPlayers(vPlayer.playerName, gameId);
            }
            if (!vGame.getAllGodPowers().isEmpty()) {
                for (GodPower godPower : vGame.getAllGodPowers()) {
                    gameBoard.addAvailableGodPowers(godPower);
                }
            }
            gameBoard.setCurrentStatus(vGame.getGameStatus());
            gameBoard.setReconnecting(true);
            gameBoard.restoreNextPlayer(vGame.getCurrentPlayerId());
            gameLobby.addGameBoard(gameBoard);
            System.out.println("Previous data loaded from GameID "+ gameId);
            notify(vGame);
            gameId += 1;
            vGame = loadVirtualGame(gameId);
        }
    }

    /**
     * It's used by loadData to restore VirtualGame Object from data file
     *
     * @param gameId game Id
     * @return a VirtualGame object or null
     */
    private VirtualGame loadVirtualGame(int gameId){
        VirtualGame vGame = null;
        File vGameFile = new File("./data/virtualGame_" + gameId + ".ser");
        if (vGameFile.exists()){
            try{
                FileInputStream fileIn = new FileInputStream("./data/virtualGame_" + gameId + ".ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                vGame = (VirtualGame) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return vGame;
    }

    // to set player name
    // if player name is ok, show him available games
    public void notify(PlayerServer ps, String playerName){
            synchronized (observerV) {
                observerV.get(0).update(ps, playerName, gameLobby);
            }
    }

    public void notify(PlayerServer ps, String playerName, int gameId){
        synchronized (observerV) {
            observerV.get(0).update(ps, playerName, gameLobby.getGameBoards().get(gameId));
        }
    }

    // in game, update specific vGame
    public void notify(int gameId){
        synchronized (observerV) {
            observerV.get(0).update(gameLobby.getGameBoards().get(gameId));
        }
    }

    public void notify(VirtualGame vGame){
        synchronized (observerV) {
            observerV.get(0).restoreVGames(vGame);
        }
    }
}
