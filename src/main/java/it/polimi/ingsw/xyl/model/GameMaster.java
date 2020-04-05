package it.polimi.ingsw.xyl.model;

import it.polimi.ingsw.xyl.view.VirtualView;

import java.util.Vector;

/**
 * GameMaster is virtual model handle all the other models,
 * just like a game master (person) can handle all the data of a game.
 *
 * @author Shaoxun
 */
public class GameMaster {
    private GameLobby gameLobby;
    private VirtualView observerV;

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
        this.observerV = observer;
    }

    /**
     * This method is used by controller to handle with new player's connect.
     *
     * @param playerName the nickname of a player.
     * @return response code -1, 0, 1, 2, 3 // TODO: decide codes
     */
    public int addPlayer(String playerName) {
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
            gameBoard.setCurrentStatus(GameStatus.WAITINGPLAYER);
            gameLobby.addGameBoard(gameBoard);
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
     * @return response code
     */
    public int setPlayerNumber(int gameId, int playerNumber) {
        gameLobby.getGameBoards().get(gameId).setPlayerNumber(playerNumber);
        notify(gameId);
        return 1;
    }

    /**
     * This method sets available God powers of gameBoard.
     *
     * @param gameId             game ID.
     * @param availableGodPowers all available powers.
     * @return response code
     */
    public int setAvailableGodPowers(int gameId, Vector<GodPower> availableGodPowers) {
        for (GodPower godPower : availableGodPowers)
            gameLobby.getGameBoards().get(gameId).addAvailableGodPowers(godPower);
        gameLobby.getGameBoards().get(gameId).toNextPlayer();
        notify(gameId);
        return 1;
    }

    /**
     * This method sets a player's God Player.
     *
     * @param gameId   game ID.
     * @param playerId player ID.
     * @param godPower the God power player chose.
     * @return response code
     */
    public int setPower4Player(int gameId, int playerId, GodPower godPower) {
        // chosen God power should be available
        Vector<GodPower> availableGodPowers = gameLobby.getGameBoards().get(gameId).getAvailableGodPowers();
        if (availableGodPowers.contains(godPower)) {
            Player player = gameLobby.getGameBoards().get(gameId).getPlayers().get(playerId);
            Cosplayer cosplayer = godPower.cosplay(player);
            player.setCosplayer(cosplayer);
            player.setCurrentStatus(PlayerStatus.GODPOWERED);
            gameLobby.getGameBoards().get(gameId).toNextPlayer();
            notify(gameId);
            if (gameLobby.getGameBoards().get(gameId).getCurrentPlayer().getPlayerId() == 0)
                return 2; // 2 for every player of the game have set God power
            return 1; // 1 for set God power OK
        }
        return -1;
    }

    /**
     * This method changes the turn id of a gameBoard to the "Start Player" which is chosen by
     * the "Challenger", in our case, the "owner" of the gameBoard.
     *
     * @param gameId        game ID.
     * @param messageFrom   which player this message is form
     * @param startPlayerId the Start Player Id
     * @return response code
     */
    public int startGame(int gameId, String messageFrom, int startPlayerId) {
        // only the "owner" of the gameBoard can decide from whom the game will start.
        if (gameLobby.getGameBoards().get(gameId).getPlayers().get(0).getPlayerName().equals(messageFrom)) {
            gameLobby.getGameBoards().get(gameId).toNextPlayer(startPlayerId);
            notify(gameId);
            return 1;
        }
        return 0;
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
     * @return response code
     */
    public int setInitialWorkerPosition(int gameId, int playerId, int ax, int ay, int bx, int by) {
        GameBoard gameBoard = gameLobby.getGameBoards().get(gameId);
        Player player = gameBoard.getPlayers().get(playerId);
        IslandBoard islandBoard = gameBoard.getIslandBoard();
        if (islandBoard.getSpaces()[ax][ay].isOccupiedBy() == 0
                && islandBoard.getSpaces()[bx][by].isOccupiedBy() == 0) {
            // set worker position
            player.setWorkers(ax, ay, bx, by);
            // set occupied
            islandBoard.getSpaces()[ax][ay].setOccupiedBy(playerId * 10);
            islandBoard.getSpaces()[bx][by].setOccupiedBy(playerId * 10 +1);
            notify(gameId);
            return 1;
        }
        return -1;
    }

    /**
     * To end one's turn when no more want to use God power
     *
     * @param gameId   game ID.
     * @param playerId player ID.
     * @param finish   end my turn
     * @return response code
     */
    public int endTurn(int gameId, int playerId, boolean finish) {
        int currentPlayerId = gameLobby.getGameBoards().get(gameId).getCurrentPlayer().getPlayerId();
        if (currentPlayerId == playerId && finish) {
            gameLobby.getGameBoards().get(gameId).toNextPlayer();
            notify(gameId);
        }
        return -1;
    }

    /**
     * To handle player's "move" action
     *
     * @param gameId    game ID.
     * @param playerId  player ID.
     * @param workerId  worker ID.
     * @param direction direction
     * @return response code
     */
    public int handleMove(int gameId, int playerId, int workerId, Direction direction) {
        Vector<Direction> availableMoves =
                gameLobby.getGameBoards().get(gameId).getPlayers().get(playerId).getCosplayer().getAvailableMoves(workerId);
        if (availableMoves.contains(direction)) {
            gameLobby.getGameBoards().get(gameId).getPlayers().get(playerId).getCosplayer().move(workerId, direction);
            notify(gameId);
            return 1;
        }
        return -1;
    }

    /**
     * To handle player's "Build" action
     *
     * @param gameId    game ID.
     * @param playerId  player ID.
     * @param workerId  worker ID.
     * @param direction direction.
     * @param buildDome whether build dome directly (only for Atlas).
     * @return response code
     */
    public int handleBuild(int gameId, int playerId, int workerId, Direction direction, boolean buildDome) {
        gameLobby.getGameBoards().get(gameId).getPlayers().get(playerId).getCosplayer().build(workerId, direction,
                buildDome);
        notify(gameId);
        return 1;
    }

    public void notify(int gameId){
        synchronized(observerV) {
            observerV.update(gameLobby.getGameBoards().get(gameId));
        }
    }
}
