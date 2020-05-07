package it.polimi.ingsw.xyl.view.cli;

import it.polimi.ingsw.xyl.model.*;
import it.polimi.ingsw.xyl.model.message.Message;
import it.polimi.ingsw.xyl.network.client.Client;
import it.polimi.ingsw.xyl.util.ColorSetter;
import it.polimi.ingsw.xyl.view.ViewInterface;
import it.polimi.ingsw.xyl.model.message.*;

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

        islandBoardCLI.setMaps(virtualGame.getSpaces());
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
                if (currentPlayerId == id)
                    playGame();
                else
                    System.out.println(ColorSetter.FG_BLUE.setColor("Waiting for other player's operations."));
                break;
            case GAMEENDED:
                System.out.println("Game End");
                System.out.println("You " + vGame.getVPlayers().get(id).getPlayerStatus());
                break;
        }

    }

    @Override
    public void update(Exception e)  {
        if (e instanceof ConnectException) {
            System.err.println("Connection refused");
            //askLogin();
        }else if (e instanceof EOFException){
            System.err.println("Connection failed! If you want to rejoin the game,\n" +
                    "please restart the game and login with the same username!");
            System.exit(0);
        }else{
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
        } else if (message instanceof WaitingReconnectionMessage){
            System.out.println("Please waiting for other players' reconnection.");
        } else if (message instanceof ConnectionDroppedMessage){
            System.err.println(((ConnectionDroppedMessage) message).playerName + " dropped the connection with " +
                    "server, " +
                    "the game stopped.");
            System.exit(0);
        }else {
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
     * Set ip and username
     * connect to server
     * called when start a new CLI
     */
    private void askLogin() {
        System.out.println(ColorSetter.FG_BLUE.setColor("Please Enter Server IP"));
//        Scanner scanner = new Scanner(System.in);
//        String ip = scanner.next();
        String ip = "127.0.0.1";
        System.out.println("Use default server ip: localhost");
        client.init(ip);
    }

    private void setUserName() {
        System.out.println(ColorSetter.FG_BLUE.setColor("Please Enter Login Name"));
        executor.execute(() -> {
            userName = new Scanner(System.in).nextLine();
//            String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//            Random random = new Random();
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < 5; i++) {
//                int number = random.nextInt(62);
//                sb.append(str.charAt(number));
//            }
//            userName = sb.toString();
//            System.out.println(ColorSetter.FG_BLUE.setColor("Your player name is " + userName));
            sendMessage(new PlayerNameMessage(userName));
        });
    }

    private void setPlayNum() {
        executor.execute(() -> {
            int playNum;
            do {
                System.out.println(ColorSetter.FG_BLUE.setColor("Please set number of players, 2 or 3"));
                playNum = new Scanner(System.in).nextInt();
            } while (playNum != 2 && playNum != 3);
            System.out.println("Play Number:" + playNum);
            sendMessage(new SetPlayerNumberMessage(gameId, playNum));
        });
    }

    private void joinOrCreate(NameOKMessage nameOKMessage) {
        var games = nameOKMessage.getGames();
        for (NameOKMessage.Games game : games) {
            int playerNum = game.getPlayerNumber();
            int currentNum = game.getCurrentPlayers().size();
            if (playerNum == currentNum) {
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
                System.out.println(ColorSetter.FG_BLUE.setColor("Input -1 to create a new game"));
                input = new Scanner(System.in).nextInt();
            } while (input < -1 || input >= games.size()
                    || (input != -1 && games.get(input).getPlayerNumber() == games.get(input).getCurrentPlayers().size()));

            if (input == -1) {
                sendMessage(new CreateNewGameMessage(userName));
            } else {
                sendMessage(new JoinGameMessage(userName, input));
            }
        });

        
    }

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
                int input = new Scanner(System.in).nextInt();
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
                    input = new Scanner(System.in).nextInt();
                } while (input < 0 || input >= availableGodPowers.size());
                sendMessage(new PlayerChooseGodPowerMessage
                        (gameId, id, availableGodPowers.get(input)));
            });
        }
    }

    private void setStartPlayer() {
        int playNum = islandBoardCLI.getPlayers().size();
        executor.execute(() -> {
            int input;
            do {
                System.out.println(ColorSetter.FG_BLUE.setColor("Input number to choose who start first"));
                islandBoardCLI.showPlayers();
                input = new Scanner(System.in).nextInt();
            } while (input < 0 || input >= playNum);
            sendMessage(new StartGameMessage(gameId, userName, input));
        });
    }

    private void setInitialWorkPosition() {

        System.out.println(ColorSetter.FG_BLUE.setColor("Set initial worker position"));
        executor.execute(() -> {
            int ax, ay, bx, by;
            do {
                System.out.println(ColorSetter.FG_BLUE.setColor("First worker x, please input 0 - 4"));
                ax = new Scanner(System.in).nextInt();
                System.out.println(ColorSetter.FG_BLUE.setColor("First worker y, please input 0 - 4"));
                ay = new Scanner(System.in).nextInt();
                System.out.println(ColorSetter.FG_BLUE.setColor("Second worker x, please input 0 - 4"));
                bx = new Scanner(System.in).nextInt();
                System.out.println(ColorSetter.FG_BLUE.setColor("Second worker y, please input 0 - 4"));
                by = new Scanner(System.in).nextInt();
            } while (false/*todo:check available*/);
            sendMessage(new SetInitialWorkerPositionMessage(gameId, id, ax, ay, bx, by));
        });
    }

    private Direction chooseDirection(String action, int workerId) {
        ArrayList<Direction> available = vGame.getVPlayers().get(id).getAvailable(action, workerId);
        int directionInput;
        do {
            System.out.println
                    (ColorSetter.FG_BLUE.setColor("Please input number to select direction"));
            for (int i = 0; i < available.size(); i++) {
                System.out.println(i + " " + available.get(i).toSymbol() + " " + available.get(i).toString());
            }
            directionInput = new Scanner(System.in).nextInt();
        } while (directionInput < 0 || directionInput >= available.size());
        return available.get(directionInput);
    }

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
                    workerId = new Scanner(System.in).nextInt();
                } while (workerId != 1 && workerId != 0);
            }
            Direction direction = chooseDirection("Move", workerId);
            sendMessage(new MoveMessage
                    (gameId, id, workerId, direction));
        });
    }

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
                    workerId = new Scanner(System.in).nextInt();
                } while (workerId != 1 && workerId != 0);
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
                    input = new Scanner(System.in).nextInt();
                    if (input == 1) isDome = true;
                } while (input != 1 && input != 0);


            sendMessage(new BuildMessage
                    (gameId, id, workerId, direction, isDome));

        });
    }

    private void chooseMoveOrBuild() {
        executor.execute(() -> {
            int input;
            do {
                System.out.println
                        (ColorSetter.FG_BLUE.setColor("Please input 1 for moving," +
                                " 0 for building"));
                input = new Scanner(System.in).nextInt();
            } while (input != 0 && input != 1);
            if (input == 1)
                move();
            else
                build();
        });
    }

    private void chooseBuildOrEnd() {
        executor.execute(() -> {
            int input;
            do {
                System.out.println
                        (ColorSetter.FG_BLUE.setColor("Please input 1 for building," +
                                " 0 for end your turn"));
                input = new Scanner(System.in).nextInt();
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
                System.out.println(ColorSetter.FG_BLUE.setColor("You Lose"));
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
}
