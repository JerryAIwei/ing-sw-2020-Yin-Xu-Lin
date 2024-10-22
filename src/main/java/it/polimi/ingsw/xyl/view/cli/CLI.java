package it.polimi.ingsw.xyl.view.cli;

import it.polimi.ingsw.xyl.model.*;
import it.polimi.ingsw.xyl.model.message.*;
import it.polimi.ingsw.xyl.network.client.Client;
import it.polimi.ingsw.xyl.util.ColorSetter;
import it.polimi.ingsw.xyl.view.ViewInterface;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Class for Display CLI messages.
 *
 * @author yaw
 */

public class CLI extends Thread implements ViewInterface {
    private IslandBoardCLI islandBoardCLI = new IslandBoardCLI();
    private Client client;
    private String userName;
    private int id = -1;
    private int gameId = -1;
    private int currentPlayerId;
    private ArrayList<GodPower> availableGodPowers;
    private PlayerStatus playerStatus;
    private String nextAction;
    private int workerInAction = -1;
    private VirtualGame vGame;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        System.out.println("new a cli");
        CLI cli = new CLI();
        cli.launch();
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.err.println(e.toString());
                return;
            }
        }
    }

    public CLI() {
        client = new Client(this);
    }

    /**
     * update and show what to do next
     *
     * @param virtualGame virtualGame received from server
     */
    @Override
    public void update(VirtualGame virtualGame) {
        this.vGame = virtualGame;
        final GameStatus gameStatus = virtualGame.getGameStatus();

        islandBoardCLI.setPlayers(virtualGame);
        //GameStatus gameStatus = virtualGame.getGameStatus();

        if (id == -1 && gameId == -1) {
            for (Integer id : islandBoardCLI.getPlayers().keySet()) {
                if (islandBoardCLI.getPlayers().get(id).getPlayerName().equals(this.userName)) {
                    this.id = id;
                    System.out.println("My ID: " + id);
                }
            }
            this.gameId = virtualGame.getGameId();
            System.out.println("Game ID: " + gameId);
        }
        islandBoardCLI.setMaps(virtualGame.getSpaces(), id);


        playerStatus = virtualGame.getVPlayers().get(id).getPlayerStatus();
        availableGodPowers = virtualGame.getAvailableGodPowers();
        nextAction = virtualGame.getVPlayers().get(id).getNextAction();
        workerInAction = virtualGame.getVPlayers().get(id).getWorkerInAction();
        currentPlayerId = virtualGame.getCurrentPlayerId();
        System.out.println("Player: " + currentPlayerId + " is playing");
        switch (gameStatus) {
            case WAITINGINIT:
                if (id == 0 && virtualGame.getCurrentPlayerId() == 0) {
                    setPlayNum();
                } else {//do nothing
                    System.err.println(ColorSetter.FG_RED.setColor("Wrong gameStatus"));
                }
                break;
            case WAITINGPLAYER:
                islandBoardCLI.showPlayers();
                System.out.println(ColorSetter.FG_BLUE.setColor("Please wait for other players join the game."));
                break;
            case WAITINGSTART:
                islandBoardCLI.showPlayers();
                if (currentPlayerId == id)
                    setUpGame();
                else
                    System.out.println(ColorSetter.FG_BLUE.setColor("Waiting for Start"));
                break;

            case INGAME:
                islandBoardCLI.showMaps();
                islandBoardCLI.showPlayers();
                if (playerStatus == PlayerStatus.LOSE)
                    gameEnd();
                else if (currentPlayerId == id)
                    playGame();
                else
                    System.out.println(ColorSetter.FG_BLUE.setColor("Waiting for other player's operations."));
                break;
            case GAMEENDED:
                gameEnd();
                break;
        }

    }

    @Override
    public void update(Exception e) {
        if (e instanceof ConnectException) {
            System.err.println("Connection refused");
            //askLogin();
        } else if (e instanceof EOFException) {
            System.err.println("Connection failed! If you want to rejoin the game,\n" +
                    "please restart the game and login with the same username!");
            System.exit(0);
        } else {
            e.printStackTrace();
        }
    }

    /**
     * used at the begin of the game
     *
     * @param message message about set name and join game
     */
    public void update(Message message) {
        if (message instanceof AskPlayerNameMessage) {
            setUserName();
        } else if (message instanceof NameOKMessage) {
            joinOrCreate((NameOKMessage) message);
        } else if (message instanceof WaitingReconnectionMessage) {
            System.out.println("Please waiting for other players' reconnection.");
        } else if (message instanceof ConnectionDroppedMessage) {
            System.err.println(((ConnectionDroppedMessage) message).playerName + " dropped the connection with " +
                    "server, " +
                    "the game stopped.");
            System.exit(0);
        } else {
            System.err.println("Wrong Message Received:" + message.getClass().toString());
        }

    }

    @Override
    public void sendMessage(Message message) {
        //debugView.update(message);
        client.sendMessage(message);
    }


    public void launch() {
        splashScreen();
        askLogin();
    }

    /**
     * show welcome when start a new CLI
     */
    private void splashScreen() {

        System.out.println();

        System.out.println(ColorSetter.FG_BLUE.setColor(
                "   _____         _   _ _______ ____  _____  _____ _   _ _____ \n" +
                        "  / ____|  /\\   | \\ | |__   __/ __ \\|  __ \\|_   _| \\ | |_   _|\n" +
                        " | (___   /  \\  |  \\| |  | | | |  | | |__) | | | |  \\| | | |  \n" +
                        "  \\___ \\ / /\\ \\ | . ` |  | | | |  | |  _  /  | | | . ` | | |  \n" +
                        "  ____) / ____ \\| |\\  |  | | | |__| | | \\ \\ _| |_| |\\  |_| |_ \n" +
                        " |_____/_/    \\_\\_| \\_|  |_|  \\____/|_|  \\_\\_____|_| \\_|_____|\n" +
                        "                                                              \n" +
                        "                                                              "
        ));
        System.out.println();
        System.out.println("Made by" + ColorSetter.FG_GREEN.setColor(" XU Shaoxun,") + ColorSetter.FG_RED.setColor(" YIN Aiwei") + " and" + ColorSetter.FG_YELLOW.setColor(" LIN Xin"));
    }

    /**
     * user interface
     * Set ip and username
     * connect to server
     * called when start a new CLI
     */
    private void askLogin() {
        System.out.println(ColorSetter.FG_BLUE.setColor("Please Enter Server IP, press enter to use localhost"));
        String ip;
        Scanner scanner = new Scanner(System.in);
        ip = scanner.nextLine();
        if (ip.trim().isEmpty())
            ip = "127.0.0.1";
        System.out.println(ColorSetter.FG_BLUE.setColor("Please Enter Port Number, press enter to use default 7777"));
        String port;
        port = scanner.nextLine();
        if (port.trim().isEmpty())
            port = "7777";
        client.init(ip, port);
    }

    /**
     * user interface
     * Set username
     */
    private void setUserName() {
        System.out.println(ColorSetter.FG_BLUE.setColor("Please Enter Login Name"));
        executor.execute(() -> {
            userName = new Scanner(System.in).nextLine();
            sendMessage(new PlayerNameMessage(userName));
        });
    }

    /**
     * user interface
     * set the player number when create a new game
     */
    private void setPlayNum() {
        executor.execute(() -> {
            int playNum;
            do {
                System.out.println(ColorSetter.FG_BLUE.setColor("Please set number of players, 2 or 3"));
                try {
                    playNum = new Scanner(System.in).nextInt();
                } catch (Exception ignored) {
                    playNum = -1;
                }
            } while (playNum != 2 && playNum != 3);
            System.out.println("Play Number:" + playNum);
            sendMessage(new SetPlayerNumberMessage(gameId, playNum));
        });
    }

    /**
     * user interface
     * choose join a game or create a new game
     */
    private void joinOrCreate(NameOKMessage nameOKMessage) {
        var games = nameOKMessage.getGames();
        for (NameOKMessage.Games game : games) {
            int playerNum = game.getPlayerNumber();
            int currentNum = game.getCurrentPlayers().size();
            GameStatus gameStatus = game.getGameStatus();
            if (gameStatus == GameStatus.GAMEENDED)
                System.out.println(ColorSetter.FG_RED.setColor("=======game ID:" + game.getGameID() +
                        " (Game Ended)" + "======"));
            else if (playerNum == currentNum) {
                System.out.println(ColorSetter.FG_RED.setColor("==========game ID:" + game.getGameID() +
                        " (" + currentNum + "/" + playerNum + ")" + "=========="));
            } else {
                System.out.println(ColorSetter.BG_BLUE.setColor("==========game ID:" + game.getGameID() +
                        " (" + currentNum + "/" + playerNum + ")" + "=========="));
            }
            var players = game.getCurrentPlayers();
            System.out.println("Players:");
            for (int i = 0; i < players.size(); i++)
                System.out.print("\t" + players.get(i));
            System.out.println("\n");
        }

        executor.execute(() -> {
            int input;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            do {
                if (!games.isEmpty())
                    System.out.println(ColorSetter.FG_BLUE.setColor("Input game ID to join the game"));
                System.out.println(ColorSetter.FG_BLUE.setColor("Input -1 to create a new game, input -2 to refresh " +
                        "the game lobby."));
                try {
                    input = new Scanner(System.in).nextInt();
                } catch (Exception ignored) {
                    input = -3;
                }
            } while (input < -2 || input >= games.size()
                    || (input != -1 && input != -2 && games.get(input).getPlayerNumber() == games.get(input).getCurrentPlayers().size()));

            if (input == -1) {
                sendMessage(new CreateNewGameMessage(userName));
            } else if (input == -2) {
                sendMessage(new RefreshMessage(userName));
            } else {
                sendMessage(new JoinGameMessage(userName, input));
            }
        });


    }

    /**
     * user interface
     * choose available god powers in this game
     */
    private void setAvailableGodPowers() {

        int playNum = islandBoardCLI.getPlayers().size();
        executor.execute(() -> {
            int[] godPowers = {-1, -1, -1};
            int countDown = playNum;
            System.out.println(ColorSetter.FG_BLUE.setColor("Input number to select available god power"));
            System.out.println(ColorSetter.FG_RED.setColor("If you choose 0 Anonymous, your will start a game" +
                    "without God Powers."));
            do {
                System.out.println(countDown + " need to choose");
                for (int i = 0; i < GodPower.values().length; i++) {
                    if (i != godPowers[0] && i != godPowers[1] && i != godPowers[2])
                        System.out.println(i + ":" + GodPower.values()[i].toString() + ":");
                    System.out.println("\t" + GodPower.values()[i].description());
                }
                int input;
                try {
                    input = new Scanner(System.in).nextInt();
                } catch (Exception ignored) {
                    input = 1000;
                }
                if (input >= 0 && input < GodPower.values().length &&
                        input != godPowers[0] && input != godPowers[1]
                        && input != godPowers[2]
                ) {
                    if (input == 0) {
                        for (int i = 0; i < playNum; i++) {
                            godPowers[i] = input;
                        }
                        countDown = 0;
                    } else {
                        godPowers[countDown - 1] = input;
                        countDown--;
                    }
                }
            } while (countDown > 0);
            if (playNum == 2)
                sendMessage(new AvailableGodPowersMessage
                        (gameId, GodPower.values()[godPowers[0]],
                                GodPower.values()[godPowers[1]]));
            else if (playNum == 3) {
                sendMessage(new AvailableGodPowersMessage
                        (gameId, GodPower.values()[godPowers[0]],
                                GodPower.values()[godPowers[1]],
                                GodPower.values()[godPowers[2]]));
            } else {
                System.err.println("in setAvailableGodPowers(): playNum!=2&&playNum!=3");
            }
        });

    }

    /**
     * user interface
     * choose my god power
     */
    private void setGodPower() {
        if (availableGodPowers.get(0) == GodPower.ANONYMOUS) {
            System.out.println(ColorSetter.FG_BLUE.setColor("This is a no-GodPowers game!"));
            sendMessage(new PlayerChooseGodPowerMessage
                    (gameId, id, availableGodPowers.get(0)));
        } else {
            executor.execute(() -> {
                int input = 0;
                do {
                    System.out.println(ColorSetter.FG_BLUE.setColor("Input number to select your god power"));
                    int i = 0;
                    for (GodPower godPower : availableGodPowers) {
                        System.out.println(i + ":" + godPower.toString() + ":");
                        System.out.println("\t" + godPower.description());
                        i++;
                    }
                    try {
                        input = new Scanner(System.in).nextInt();
                    } catch (Exception ignored) {
                        input = -1;
                    }
                } while (input < 0 || input >= availableGodPowers.size());
                sendMessage(new PlayerChooseGodPowerMessage
                        (gameId, id, availableGodPowers.get(input)));
            });
        }
    }
    /**
     * user interface
     * choose who to start
     */
    private void setStartPlayer() {
        int playNum = islandBoardCLI.getPlayers().size();
        executor.execute(() -> {
            int input;
            do {
                System.out.println(ColorSetter.FG_BLUE.setColor("Input number to choose who start first"));
                islandBoardCLI.showPlayers();
                try {
                    input = new Scanner(System.in).nextInt();
                } catch (Exception ignored) {
                    input = -1;
                }
            } while (input < 0 || input >= playNum);
            sendMessage(new StartGameMessage(gameId, userName, input));
        });
    }
    /**
     * user interface
     * choose the initial position of the workers
     */
    private void setInitialWorkPosition() {

        System.out.println(ColorSetter.FG_BLUE.setColor("Set initial worker position"));
        executor.execute(() -> {
            int ax, ay, bx, by;
            do {
                try {
                    System.out.println(ColorSetter.FG_BLUE.setColor("First worker x, please input 0 - 4"));
                    ax = new Scanner(System.in).nextInt();
                    System.out.println(ColorSetter.FG_BLUE.setColor("First worker y, please input 0 - 4"));
                    ay = new Scanner(System.in).nextInt();
                } catch (Exception ignored) {
                    ax = -1;
                    ay = -1;
                }
                if (ax != -1 && ay != -1 && vGame.getSpaces()[ax][ay].isOccupiedBy() != -1) {
                    System.out.println(ColorSetter.FG_RED.setColor("The position is occupied! Please choose another " +
                            "position."));
                }
            } while (ax == -1 || ay == -1 || vGame.getSpaces()[ax][ay].isOccupiedBy() != -1);
            do {
                try {
                    System.out.println(ColorSetter.FG_BLUE.setColor("Second worker x, please input 0 - 4"));
                    bx = new Scanner(System.in).nextInt();
                    System.out.println(ColorSetter.FG_BLUE.setColor("Second worker y, please input 0 - 4"));
                    by = new Scanner(System.in).nextInt();
                } catch (Exception ignored) {
                    bx = -1;
                    by = -1;
                }
                if (bx != -1 && by != -1 && vGame.getSpaces()[bx][by].isOccupiedBy() != -1) {
                    System.out.println(ColorSetter.FG_RED.setColor("The position is occupied! Please choose another " +
                            "position."));
                }
            } while (bx == -1 || by == -1 || vGame.getSpaces()[bx][by].isOccupiedBy() != -1);
            sendMessage(new SetInitialWorkerPositionMessage(gameId, id, ax, ay, bx, by));
            System.out.println(ColorSetter.FG_RED.setColor("The red numbers represent your Workers," +
                    "the most right-hand digit of the red numbers represents the Worker ID."));
        });
    }
    /**
     * user interface
     * choose the direction of move and build
     */
    private Direction chooseDirection(String action, int workerId) {
        ArrayList<Direction> available = vGame.getVPlayers().get(id).getAvailable(action, workerId);
        if (available.isEmpty()) {
            throw new RuntimeException("No available of" + action + "error!");
        }
        int directionInput;
        do {
            System.out.println
                    (ColorSetter.FG_BLUE.setColor("Please input number to select direction"));
            for (int i = 0; i < available.size(); i++) {
                System.out.println(i + " " + available.get(i).toSymbol() + " " + available.get(i).toString());
            }
            try {
                directionInput = new Scanner(System.in).nextInt();
            } catch (Exception ignored) {
                directionInput = -1;
            }
        } while (directionInput < 0 || directionInput >= available.size());
        return available.get(directionInput);
    }
    /**
     * user interface
     * choose who and where to move
     */
    private void move() {
        executor.execute(() -> {
            int workerId;
            if (workerInAction != -1) {
                System.out.println(ColorSetter.FG_BLUE.setColor("You should use worker " + workerInAction + " to move."));
                workerId = workerInAction;
            } else {

                do {
                    System.out.println
                            (ColorSetter.FG_BLUE.setColor("Input 0 or 1 to choose your worker to move"));
                    try {
                        workerId = new Scanner(System.in).nextInt();
                    } catch (Exception ignored) {
                        workerId = -1;
                    }
                    if (vGame.getVPlayers().get(id).getAvailable("Move", workerId).isEmpty())
                        System.out.println(ColorSetter.FG_RED.setColor("No available action of worker " + workerId +
                                "."));
                } while ((workerId != 1 && workerId != 0) || vGame.getVPlayers().get(id).getAvailable("Move",
                        workerId).isEmpty());
            }
            Direction direction = chooseDirection("Move", workerId);
            sendMessage(new MoveMessage
                    (gameId, id, workerId, direction));
        });
    }
    /**
     * user interface
     * choose who and where to build
     */
    private void build() {
        executor.execute(() -> {
            int workerId;

            if (workerInAction != -1) {
                System.out.println(ColorSetter.FG_BLUE.setColor("You should use worker " + workerInAction + " to build."));
                workerId = workerInAction;
            } else {
                do {
                    System.out.println
                            (ColorSetter.FG_BLUE.setColor("Input 0 or 1 to choose your worker to build"));
                    try {
                        workerId = new Scanner(System.in).nextInt();
                    } catch (Exception ignored) {
                        workerId = -1;
                    }
                    if (vGame.getVPlayers().get(id).getAvailable("Build", workerId).isEmpty())
                        System.out.println(ColorSetter.FG_RED.setColor("No available action of worker " + workerId +
                                "."));
                } while ((workerId != 1 && workerId != 0) || vGame.getVPlayers().get(id).getAvailable("Build",
                        workerId).isEmpty());
            }
            Direction direction = chooseDirection("Build", workerId);
            boolean isDome = false;
            int input;
            //special for Atlas
            if (islandBoardCLI.getPlayers().get(id).getGodPower()
                    == GodPower.ATLAS)
                do {
                    System.out.println
                            ("Please input 1 for building a dome," +
                                    " 0 for normal building");
                    try {
                        input = new Scanner(System.in).nextInt();
                    } catch (Exception ignored) {
                        input = -1;
                    }
                    if (input == 1) isDome = true;
                } while (input != 1 && input != 0);


            sendMessage(new BuildMessage
                    (gameId, id, workerId, direction, isDome));

        });
    }
    /**
     * user interface
     * for god power, choose move or build
     */
    private void chooseMoveOrBuild() {
        executor.execute(() -> {
            int input;
            do {
                System.out.println
                        (ColorSetter.FG_BLUE.setColor("Please input 1 for moving," +
                                " 0 for building"));
                try {
                    input = new Scanner(System.in).nextInt();
                } catch (Exception ignored) {
                    input = -1;
                }
            } while (input != 0 && input != 1);
            if (input == 1)
                move();
            else
                build();
        });
    }
    /**
     * user interface
     * for god power, choose build or end this turn
     */
    private void chooseBuildOrEnd() {
        executor.execute(() -> {
            int input;
            do {
                System.out.println
                        (ColorSetter.FG_BLUE.setColor("Please input 1 for building," +
                                " 0 for end your turn"));
                try {
                    input = new Scanner(System.in).nextInt();
                } catch (Exception ignored) {
                    input = -1;
                }
            } while (input != 0 && input != 1);

            if (input == 1)
                build();
            else
                sendMessage(new MyTurnFinishedMessage(gameId, id));
        });
    }

    /**
     * Method used to set god power and choose who to start first
     */
    private void setUpGame() {
        switch (playerStatus) {
            case INGAMEBOARD:
                if (availableGodPowers.isEmpty()) {
                    if (id == 0)
                        setAvailableGodPowers();
                    else {
                        System.out.println(ColorSetter.FG_BLUE.setColor("Waiting for setting Available God Power"));
                    }
                } else {
                    setGodPower();
                }
                break;
            case GODPOWERED://do nothing
                break;
            case WAITING4START:
                setStartPlayer();
                break;
        }

    }

    /**
     * my turn, play game based on playerStatus and nextAction
     */
    private void playGame() {
        switch (playerStatus) {
            case WAITING4INIT:
                setInitialWorkPosition();
                break;
            case WORKERPREPARED:
                moveOrBuild();
                break;
            case LOSE:
                gameEnd();
                break;
        }
    }

    /**
     * deside what to do based on nextAction
     */
    private void moveOrBuild() {
        switch (nextAction) {
            case "MOVE":
                move();
                break;
            case "BUILD":
                build();
                break;
            case "MOVEORBUILD":
                chooseMoveOrBuild();
                break;
            case "BUILDOREND":
                chooseBuildOrEnd();
                break;
        }
    }
    /**
     * show the game result
     */
    private void gameEnd() {
        System.out.println("Game End");
        PlayerStatus playerStatus = vGame.getVPlayers().get(id).getPlayerStatus();
        if (playerStatus == PlayerStatus.WIN)
            System.out.println("You win!");
        else
            System.out.println("You lose!");
        gameId = -1;
        id = -1;
    }
}
