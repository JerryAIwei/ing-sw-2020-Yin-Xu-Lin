package it.polimi.ingsw.xyl.view.gui;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.LinkedList;

import it.polimi.ingsw.xyl.model.*;
import it.polimi.ingsw.xyl.model.message.*;
import it.polimi.ingsw.xyl.network.client.Client;
import it.polimi.ingsw.xyl.util.ColorSetter;
import it.polimi.ingsw.xyl.view.ViewInterface;
import it.polimi.ingsw.xyl.view.gui.controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * GUI main class
 *
 * @author yaw
 */
public class GUI extends Application implements ViewInterface {

    private Stage primaryStage;
    private final Stage setPlayNumStage = new Stage();
    private Stage loginStage;
    private final Stage godPowerStage = new Stage();
    private GameBoardGUI gameBoardGUI;
    private final Client client;
    private AskLoginController askLoginController;
    private GameBoardController gameBoardController;

    private String userName;
    private int gameId = -1;
    private int id = -1;
    private int currentPlayerId;
    private ArrayList<GodPower> availableGodPowers;
    private PlayerStatus playerStatus;
    private String nextAction;
    private int workerInAction = -1;
    private VirtualGame vGame;


    public int getId() {
        return id;
    }

    public int getGameId() {
        return gameId;
    }

    /**
     * start the connection between client and server
     *
     * @param IP   server ip
     * @param port server port
     */
    public void initClient(String IP, String port) {
        client.init(IP, port);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public GUI() {
        client = new Client(this);
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Santorini");
        this.primaryStage.getIcons().add(new Image(
                GUI.class.getResourceAsStream("/img/icon.png")));
        this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        // load necessary 3D resource and init gameBoardGUI
        new Thread(() -> {
            gameBoardGUI = new GameBoardGUI();
        }).start();
        askLogin();
    }

    /**
     * set primaryStage to game board layout
     */
    public void trans2GameBoard() {
        primaryStage.setScene(gameBoardGUI.toGameBoard());
        primaryStage.show();
    }


    /**
     * ask player to input server IP and player name
     */
    private void askLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/askLogin.fxml"));
            BorderPane loginLayout = loader.load();
            loginStage = new Stage();
            loginStage.setResizable(false);
            loginStage.centerOnScreen();
            loginStage.setTitle("Santorini : Login");
            loginStage.setAlwaysOnTop(true);
            loginStage.initOwner(primaryStage);
            Scene scene = new Scene(loginLayout);
            loginStage.setScene(scene);
            askLoginController = loader.getController();
            askLoginController.setMainApp(this);
            askLoginController.setDialogStage(loginStage);
            // Show the dialog and wait until the user closes it
            loginStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * show existed games, player can choose join a game or create new game
     */
    private void joinOrCreate(NameOKMessage nameOKMessage) {
        ObservableList<NameOKMessage.Games> games = FXCollections.observableArrayList();
        games.addAll(nameOKMessage.getGames());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/GameLobby.fxml"));
            AnchorPane layout = (AnchorPane) loader.load();
            GameLobbyController controller = loader.getController();
            controller.setMainApp(this, games);
            Scene scene = new Scene(layout);
            Platform.runLater(() -> {
                loginStage.close();
                primaryStage.setScene(scene);
                primaryStage.show();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * choose to number of player(2 or 3) in game
     */
    public void setPlayNum() {
        Platform.runLater(() -> {
            gameBoardGUI.setShowStatus("Please set the total player number.");
        });
        try {

            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/SetUpGame.fxml"));
            BorderPane page = loader.load();

            Platform.runLater(() -> {
                // Create the dialog Stage.
                setPlayNumStage.setTitle("Choice Player Number");
                setPlayNumStage.initModality(Modality.WINDOW_MODAL);
                setPlayNumStage.initOwner(primaryStage);
                Scene scene = new Scene(page);
                setPlayNumStage.setScene(scene);
                SetUpGameController controller = loader.getController();
                controller.setDialogStage(setPlayNumStage);
                controller.setMainApp(this, gameId);
                // Show the dialog and wait until the user closes it
                setPlayNumStage.showAndWait();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void update(Exception e) {
        if (e instanceof ConnectException) {
            Platform.runLater(() -> {
                askLoginController.setServerUnreachable();
            });
        } else if (e instanceof EOFException) {
            System.err.println("Connection failed! If you want to rejoin the game,\n" +
                    "please restart the game and login with the same username!");
            System.exit(0);
        } else {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Message message) {
        if (message instanceof AskPlayerNameMessage) {
            Platform.runLater(() -> {
                askLoginController.setUserNameNotAvailable();
            });
        } else if (message instanceof NameOKMessage) {
            joinOrCreate((NameOKMessage) message);
        } else if (message instanceof WaitingReconnectionMessage) {
            Platform.runLater(() -> {
                askLoginController.setWaiting();
            });
        } else if (message instanceof ConnectionDroppedMessage) {
            System.err.println("Some player's connection dropped! If you want to rejoin the game,\n" +
                    "please restart the game and login with the same username!");
            System.exit(0);
        } else {
            System.err.println("Wrong Message Received:" + message.getClass().toString());
        }

    }

    /**
     * update and show what to do next
     *
     * @param virtualGame virtualGame received from server
     */
    @Override
    public void update(VirtualGame virtualGame) {

        this.vGame = virtualGame;
        final GameStatus gameStatus = vGame.getGameStatus();


        if (id == -1 && gameId == -1) {
            gameBoardController = new GameBoardController(gameBoardGUI, primaryStage, this);
            Platform.runLater(() -> {
                if (loginStage.isShowing())
                    loginStage.close();
                trans2GameBoard();
                primaryStage.centerOnScreen();
            });

            for (Integer id : vGame.getVPlayers().keySet()) {
                if (vGame.getVPlayers().get(id).getPlayerName().equals(this.userName)) {
                    this.id = id;
                    gameBoardGUI.setId(id);
                }
            }
            this.gameId = vGame.getGameId();
            Platform.runLater(() -> {
                gameBoardGUI.setUserNameLabel("Username: " + userName);
                gameBoardGUI.setGameIdLabel("Game ID: " + gameId);
            });
        }

        Platform.runLater(() -> {
            gameBoardGUI.setMaps(vGame.getSpaces());
            gameBoardGUI.setGodPower(vGame.getVPlayers().get(id).getGodPower());
            gameBoardGUI.updateVSGodPowers(vGame);
            gameBoardController.setWorkerInAction(vGame.getVPlayers().get(id).getWorkerInAction());
            gameBoardController.refresh();
        });

        playerStatus = vGame.getVPlayers().get(id).getPlayerStatus();
        availableGodPowers = vGame.getAvailableGodPowers();
        nextAction = vGame.getVPlayers().get(id).getNextAction();
        currentPlayerId = vGame.getCurrentPlayerId();
        switch (gameStatus) {
            case WAITINGINIT:
                if (id == 0 && vGame.getCurrentPlayerId() == 0) {
                    setPlayNum();
                } else {//do nothing
                    System.err.println(ColorSetter.FG_RED.setColor("Wrong gameStatus"));
                }
                break;
            case WAITINGPLAYER:
                break;
            case WAITINGSTART:
                if (currentPlayerId == id)
                    setUpGame();
                break;

            case INGAME:
                if (currentPlayerId == id)
                    playGame();
                break;
            case GAMEENDED:
                gameEnd(vGame.getVPlayers().get(id).getPlayerStatus() == PlayerStatus.WIN);
                break;
        }

    }

    /**
     * decide what to do based on playerStatus
     */
    private void setUpGame() {
        switch (playerStatus) {
            case INGAMEBOARD:
                if (availableGodPowers.isEmpty()) {
                    if (id == 0)
                        setAvailableGodPowers();
                    else {
                        Platform.runLater(() -> {
                            gameBoardGUI.setShowStatus("Waiting for setting Available God Power");
                        });
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
     * choose who to start
     */
    private void setStartPlayer() {
        Platform.runLater(() -> {
            gameBoardGUI.setShowStatus("Please choose the start player.");
        });
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/StartPlayer.fxml"));
            BorderPane page = loader.load();
            Platform.runLater(() -> {
                setPlayNumStage.setTitle("Choice Start Player");
                Scene scene = new Scene(page);
                setPlayNumStage.setScene(scene);
                // Set the person into the controller.
                StartPlayerController controller = loader.getController();
                controller.setDialogStage(setPlayNumStage);
                controller.setMainApp(this, gameId, vGame.getPlayerNumber());
                // Show the dialog and wait until the user closes it
                setPlayNumStage.showAndWait();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * choose my god power
     */
    private void setGodPower() {
        Platform.runLater(() -> {
            gameBoardGUI.setShowStatus("Please choose your God Power.");
        });
        if (availableGodPowers.get(0) == GodPower.ANONYMOUS) {
            sendMessage(new PlayerChooseGodPowerMessage
                    (gameId, id, availableGodPowers.get(0)));
        } else {
            try {
                // Load the fxml file and create a new stage for the popup dialog.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(GUI.class.getResource("/GodPower.fxml"));
                BorderPane page = (BorderPane) loader.load();
                Platform.runLater(() -> {
                    // show the dialog Stage.
                    godPowerStage.setTitle("Choice your GodPower");
                    // godPowerStage.initOwner(primaryStage);
                    Scene scene = new Scene(page);
                    godPowerStage.setScene(scene);
                    GodPowerController controller = loader.getController();
                    LinkedList<GodPower> powers = new LinkedList<>();
                    powers.addAll(availableGodPowers);
                    controller.setMainApp(this, powers);
                    controller.setStage(godPowerStage);
                    // Show the dialog and wait until the user closes it
                    godPowerStage.showAndWait();
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * choose god powers for this game
     */
    private void setAvailableGodPowers() {
        Platform.runLater(() -> {
            gameBoardGUI.setShowStatus("Please set Available God Powers.");
        });
        int playNum = vGame.getPlayerNumber();
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/GodPower.fxml"));
            BorderPane page = (BorderPane) loader.load();
            Platform.runLater(() -> {
                // show the dialog Stage.
                godPowerStage.setTitle("Set Available GodPowers");
                godPowerStage.initOwner(primaryStage);
                Scene scene = new Scene(page);
                godPowerStage.setScene(scene);
                GodPowerController controller = loader.getController();
                controller.setMainApp(this, playNum);
                controller.setStage(godPowerStage);
                // Show the dialog and wait until the user closes it
                godPowerStage.showAndWait();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * my turn, play game based on playerStatus and nextAction
     */
    private void playGame() {
        gameBoardController.setIsTurn();
        switch (playerStatus) {
            case WAITING4INIT:
                setInitialWorkerPosition();
                break;
            case WORKERPREPARED:
                moveOrBuild();
                break;
            case LOSE:
                gameEnd(false);
                break;
        }
    }


    /**
     * When you lose or win the game, show the the game result.
     *
     * @param isWin true if win the game; false if lose the game
     */
    private void gameEnd(Boolean isWin) {
        FXMLLoader loader = new FXMLLoader();
        if (isWin) loader.setLocation(GUI.class.getResource("/EndGameWin.fxml"));
        else loader.setLocation(GUI.class.getResource("/EndGameLose.fxml"));
        try {
            AnchorPane layout = loader.load();
            Scene scene = new Scene(layout);
            Platform.runLater(() -> {
                Stage endGameStage = new Stage();
                endGameStage.setScene(scene);
                endGameStage.setAlwaysOnTop(true);
                endGameStage.show();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * at the beginning of the game, set the workers' position
     */
    private void setInitialWorkerPosition() {
        Platform.runLater(() -> {
            gameBoardGUI.setShowStatus("Please choose two position to place your Worker.");
            gameBoardGUI.initialWorkerPosition();
        });
    }

    /**
     * deside what to do based on nextAction
     */
    private void moveOrBuild() {
        gameBoardGUI.setAvailable
                (vGame.getVPlayers().get(id).getAvailable("Build", 0), false, 0);
        gameBoardGUI.setAvailable
                (vGame.getVPlayers().get(id).getAvailable("Build", 1), false, 1);
        gameBoardGUI.setAvailable
                (vGame.getVPlayers().get(id).getAvailable("Move", 0), true, 0);
        gameBoardGUI.setAvailable
                (vGame.getVPlayers().get(id).getAvailable("Move", 1), true, 1);
        switch (nextAction) {
            case "MOVE":
                Platform.runLater(() -> gameBoardController.setMove());
                break;
            case "BUILD":
                Platform.runLater(() -> gameBoardController.setBuild());
                break;
            case "MOVEORBUILD":
                Platform.runLater(() -> gameBoardController.setMoveOrBuild());
                break;
            case "BUILDOREND":
                Platform.runLater(() -> gameBoardController.setBuildOrEnd());
                break;
        }
    }

    @Override
    public void sendMessage(Message message) {
        client.sendMessage(message);
    }
}
