package it.polimi.ingsw.xyl.view.cli;

import it.polimi.ingsw.xyl.controller.GameController;
import it.polimi.ingsw.xyl.model.*;
import it.polimi.ingsw.xyl.model.message.Message;
import it.polimi.ingsw.xyl.network.client.Client;
import it.polimi.ingsw.xyl.util.ColorSetter;
import it.polimi.ingsw.xyl.view.ViewInterface;
import it.polimi.ingsw.xyl.model.message.*;
import it.polimi.ingsw.xyl.view.VirtualView;

import java.util.Scanner;
import java.util.Vector;

import static it.polimi.ingsw.xyl.model.GodPower.ATLAS;

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
    private Vector<GodPower> availableGodPowers;
    private PlayerStatus playerStatus;
    private String nextAction;
    private VirtualView debugView = VirtualView.getSingleton();
    private GameController debugController = GameController.getSingleton();

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
        debugView.register(debugController);
        debugController.register(debugView);
        debugView.register(this);
    }

    /**
     * update and show what to do next
     *
     * @param virtualGame virtualGame received from server
     */
    @Override
    public void update(VirtualGame virtualGame) {

        GameStatus gameStatus = virtualGame.getGameStatus();
        islandBoardCLI.setMaps(virtualGame.getSpaces());
        islandBoardCLI.setPlayers(virtualGame);
        //GameStatus gameStatus = virtualGame.getGameStatus();

        if (gameStatus == GameStatus.WAITINGINIT && id == -1 && gameId == -1) {
            for (Integer id : islandBoardCLI.getPlayers().keySet()) {
                if (islandBoardCLI.getPlayers().get(id).getPlayerName().equals(this.userName)) {
                    this.id = id;
                    this.gameId = 0;
                }
            }

        }

        playerStatus = virtualGame.getVPlayers().get(id).getPlayerStatus();
        availableGodPowers = virtualGame.getAvailableGodPowers();
        nextAction = virtualGame.getVPlayers().get(id).getNextAction();

        switch (gameStatus) {
            case WAITINGINIT:
                if (id == 0 && virtualGame.getCurrentPlayerId() == 0) {
                    setPlayNum();
                } else {//do nothing
                    System.err.println("Wrong gameStatus");
                }
                break;
            case WAITINGPLAYER:
                islandBoardCLI.showPlayers();
                System.out.println("Waiting for other player");
                break;
            case WAITINGSTART:
                islandBoardCLI.showPlayers();
                if (currentPlayerId == id)
                    setUpGame();
                else
                    System.out.println("Waiting for Start");
                break;

            case INGAME:
                islandBoardCLI.showMaps();
                islandBoardCLI.showPlayers();
                if (currentPlayerId == id)
                    playGame();
                break;
            case GAMEENDED:
                System.out.println("Game Over");
                break;
        }

    }

    @Override
    public void update(Exception e) {

    }

    @Override
    public void sendMessage(Message message) {
        debugView.update(message);
        //client.sendMessage(message);
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
        System.out.println("Please Enter Server IP");
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.next();
        //client.init(ip);
        System.out.println("Please Enter Login Name");
        userName = scanner.next();
        sendMessage(new PlayerNameMessage(userName));
    }

    private void setPlayNum() {
        int playNum;
        do {
            System.out.println("Please set number of players, 2 or 3");
            Scanner sc = new Scanner(System.in);
            playNum
                    = sc.nextInt();
        } while (playNum != 2 && playNum != 3);

        System.out.println("Play Number:" + playNum);
        sendMessage(new SetPlayerNumberMessage(gameId, playNum));
    }

    private void setAvailableGodPowers() {
        int[] godPowers = {-1, -1, -1};
        int playNum = islandBoardCLI.getPlayers().size();
        int countDown = playNum;
        do {
            System.out.println("input number to select available god power");
            System.out.println(playNum + " need to choose");
            for (int i = 0; i < GodPower.values().length; i++) {
                if (i != godPowers[0] && i != godPowers[1] && i != godPowers[2])
                    System.out.println(i + ":" + GodPower.values()[i]);
                int input = new Scanner(System.in).nextInt();
                if (input >= 0 && input < GodPower.values().length &&
                        input != godPowers[0] && input != godPowers[1]
                        && input != godPowers[2]
                ) {
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
    }

    private void setGodPower() {
        int input;
        do {
            System.out.println("input number to select your god power");
            int i = 0;
            for (GodPower godPower : availableGodPowers) {
                System.out.println(i + ":" + godPower);
            }
            input = new Scanner(System.in).nextInt();
        } while (input < 0 || input >= availableGodPowers.size());
        sendMessage(new PlayerChooseGodPowerMessage
                (gameId, id, availableGodPowers.get(input)));
    }

    private void setStartPlayer() {
        int playNum = islandBoardCLI.getPlayers().size();
        int input;
        do {
            System.out.println("input number to choose who start first");
            islandBoardCLI.showPlayers();
            input = new Scanner(System.in).nextInt();
        } while (input < playNum && input > 0);
        sendMessage(new StartGameMessage(gameId, userName, input));
    }

    private void setInitialWorkPosition() {
        int ax, ay, bx, by;
        System.out.println("set initial worker position");
        do {
            System.out.println("first worker x, please input 0 - 4");
            ax = new Scanner(System.in).nextInt();
            System.out.println("first worker y, please input 0 - 4");
            ay = new Scanner(System.in).nextInt();
            System.out.println("second worker x, please input 0 - 4");
            bx = new Scanner(System.in).nextInt();
            System.out.println("second worker y, please input 0 - 4");
            by = new Scanner(System.in).nextInt();
        } while (false/*check available*/);
        sendMessage(new SetInitialWorkerPositionMessage(gameId, id, ax, ay, bx, by));
    }

    private int chooseDirection() {
        int direction;
        do {
            System.out.println
                    ("please input number to select direction");
            for (int i = 0; i < Direction.values().length; i++) {
                System.out.println(i + " " + Direction.values()[i]);
            }
            direction = new Scanner(System.in).nextInt();
        } while (false/*check available*/);
        return direction;
    }

    private void move() {

        int workerId;
        do {
            System.out.println
                    ("input 1 or 2 to choose your worker to move");
            workerId = new Scanner(System.in).nextInt();
        } while (workerId != 1 && workerId != 2);
        int direction = chooseDirection();
        sendMessage(new MoveMessage
                (gameId, id, workerId, Direction.values()[direction]));
    }

    private void build() {
        int workerId;//todo:move and build is the same worker
        do {
            System.out.println
                    ("input 1 or 2 to choose your worker to move");
            workerId = new Scanner(System.in).nextInt();
        } while (workerId != 1 && workerId != 2);
        int direction = chooseDirection();
        boolean isDome = false;
        int input;
        //special for Atlas
        if (islandBoardCLI.getPlayers().get(id).getCosplayer().getGodPower()
                == ATLAS)
            do {
                System.out.println
                        ("please input 1 for building a dome," +
                                " 0 for normal building");
                input = new Scanner(System.in).nextInt();
                if (input == 1) isDome = true;
            } while (input != 1 && input != 0);


        sendMessage(new BuildMessage
                (gameId, id, workerId, Direction.values()[direction], isDome));

    }

    private void chooseMoveOrBuild() {
        int input;
        do {
            System.out.println
                    ("please input 1 for moving," +
                            " 0 for building");
            input = new Scanner(System.in).nextInt();
        } while (input != 0 && input != 1);
        if (input == 1)
            move();
        else
            build();
    }

    private void chooseBuildOrEnd() {
        int input;
        do {
            System.out.println
                    ("please input 1 for building," +
                            " 0 for end your turn");
            input = new Scanner(System.in).nextInt();
        } while (input != 0 && input != 1);

        if (input == 1)
            build();
        else
            sendMessage(new MyTurnFinishedMessage(gameId, id));
    }

    /**
     * Method used to set god power and choose who to start first
     */
    private void setUpGame() {
        switch (playerStatus) {
            case INGAMEBOARD:
                if (id == 0) {
                    if (availableGodPowers.size() == 1)
                        setGodPower();
                    else
                        setAvailableGodPowers();
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
                System.out.println("You Lose");
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
