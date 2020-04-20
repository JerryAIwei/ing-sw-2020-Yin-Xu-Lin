package it.polimi.ingsw.xyl.model;

import it.polimi.ingsw.xyl.view.VirtualView;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * GameMaster is virtual model handle all the other models,
 * just like a game master (person) can handle all the data of a game.
 *
 * @author Shaoxun
 */
public class GameMaster {
    private final GameLobby gameLobby;
    private final List<VirtualView> observerV = new ArrayList<>();


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

    public synchronized void addPlayer(InetAddress ip, String playerName){
        gameLobby.add2AllPlayers( playerName,1);
        notify(ip,gameLobby.add2AllPlayers( playerName,1));
       // observerV.get(0).update(ip, gameLobby.add2AllPlayers( playerName,1));
    }
    /**
     * This method is used by controller to handle with new player's connect.
     *
     * @param playerName the nickname of a player.
     */
    public synchronized int addPlayer2(String playerName) {
        if (gameLobby.getAllPlayers().get(playerName) != null)
            return 3;
        int gameId = 0;
        // search the first available game
        while (gameLobby.getGameBoards().size() > gameId &&
                gameLobby.getGameBoards().get(gameId).getPlayers().size()
                        == gameLobby.getGameBoards().get(gameId).getPlayerNumber())
            gameId += 1;
        // if cannot find one, create a new gameBoard
        if (gameLobby.getGameBoards().size() == gameId) {
            GameBoard gameBoard = new GameBoard(gameId);
            // set the game status to "waiting other players"
            gameBoard.setCurrentStatus(GameStatus.WAITINGINIT);
            Player player = new Player(0, playerName);
            // set the player's status "in gameBoard"
            player.setCurrentStatus(PlayerStatus.INGAMEBOARD);
            gameBoard.addPlayer(player);
            player.setCurrentGameBoard(gameBoard);
            gameLobby.add2AllPlayers(playerName, gameId);
            gameLobby.addGameBoard(gameBoard);
            notify(gameId);
            return 0;  // 0 for the owner of the GameBoard(the first player of a game)
        } else {
            GameBoard gameBoard = gameLobby.getGameBoards().get(gameId);
            int playerId = gameBoard.getPlayers().size();
            Player player = new Player(playerId, playerName);
            // set the player's status "in gameBoard"
            player.setCurrentStatus(PlayerStatus.INGAMEBOARD);
            gameLobby.add2AllPlayers(playerName, gameId);
            gameBoard.addPlayer(player);
            player.setCurrentGameBoard(gameBoard);
            // if the number of players joined to the game equals to playerNumber
            // set the game status "waiting start"
            if (gameBoard.getPlayerNumber() == gameBoard.getPlayers().size())
                gameBoard.setCurrentStatus(GameStatus.WAITINGSTART);
            notify(gameId);
            return 1; // 1 for other players of the GameBoard(not the first one)
        }
    }

    /**
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
    public void setAvailableGodPowers(int gameId, Vector<GodPower> availableGodPowers) {
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
        Vector<GodPower> availableGodPowers = gameLobby.getGameBoards().get(gameId).getAvailableGodPowers();
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
        Vector<Direction> availableMoves =
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

    public void notify(int gameId){
        synchronized (observerV) {
            observerV.get(0).update(gameId, gameLobby.getGameBoards().get(gameId));
        }
    }
    public void notify(InetAddress ip, boolean ok){
        synchronized (observerV) {
            observerV.get(0).update(ip,ok,gameLobby);
        }
    }
}
