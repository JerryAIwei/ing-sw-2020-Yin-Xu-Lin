package it.polimi.ingsw.xyl.view.gui;

import java.io.IOException;
import java.util.ArrayList;

import it.polimi.ingsw.xyl.model.GameStatus;
import it.polimi.ingsw.xyl.model.GodPower;
import it.polimi.ingsw.xyl.model.PlayerStatus;
import it.polimi.ingsw.xyl.model.VirtualGame;
import it.polimi.ingsw.xyl.model.message.AskPlayerNameMessage;
import it.polimi.ingsw.xyl.model.message.Message;
import it.polimi.ingsw.xyl.model.message.NameOKMessage;
import it.polimi.ingsw.xyl.network.client.Client;
import it.polimi.ingsw.xyl.util.ColorSetter;
import it.polimi.ingsw.xyl.view.ViewInterface;
import it.polimi.ingsw.xyl.view.cli.IslandBoardCLI;
import it.polimi.ingsw.xyl.view.gui.controller.*;
import it.polimi.ingsw.xyl.view.gui.model.Person;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GUI extends Application implements ViewInterface {


    private final int PREF_MIN_WIDTH = 1080;
    private final int PREF_MIN_HEIGHT = 800;

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

    private AskLoginController askLoginController;
    private Stage loginStage;

    private ObservableList<Person> personData = FXCollections.observableArrayList();

    private Stage primaryStage;
    private BorderPane rootLayout;


    public void initClient(String IP) {
        client.init(IP);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public GUI() {

        client = new Client(this);

        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));


    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Santorini");
        this.primaryStage.getIcons().add(new Image(
                GUI.class.getResourceAsStream("/img/icon.png")));
        this.primaryStage.setMinWidth(PREF_MIN_WIDTH);
        this.primaryStage.setMinHeight(PREF_MIN_HEIGHT);
        System.out.println(Screen.getPrimary().getBounds());
        initRootLayout();
        askLogin();
        //showPersonOverview();
    }
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            BackgroundImage myBI = new BackgroundImage(new Image("/img/background.jpeg", 1080, 720, false, true),
                    BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT);
//then you set to your node
            rootLayout.setBackground(new Background(myBI));
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("GUI.class.getResource(\"\")" + GUI.class.getResource(""));
            //e.printStackTrace();
        }
    }


    /**
     * ask player to input server IP and player name
     * */
    private void askLogin() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/AskLogin.fxml"));
            BorderPane loginLayout = loader.load();

            loginStage = new Stage();
            loginStage.setResizable(false);
            loginStage.setTitle("Login");
            loginStage.setAlwaysOnTop(true);
            loginStage.initOwner(primaryStage);
            Scene scene = new Scene(loginLayout);
            loginStage.setScene(scene);

            // Give the controller access to the main app.
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
        Platform.runLater(() -> {
            loginStage.close();
        });
        ObservableList<NameOKMessage.Games> games = FXCollections.observableArrayList();
        for (var game : nameOKMessage.getGames()) {
            games.add(game);
        }
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/GameLobby.fxml"));
            AnchorPane layout = (AnchorPane) loader.load();
            // Give the controller access to the main app.
            GameLobbyController controller = loader.getController();
            controller.setMainApp(this, games);
            Scene scene = new Scene(layout);
            Platform.runLater(() -> {
                primaryStage.setScene(scene);
                primaryStage.show();
            });
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public void setUpGame(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/SetUpGame.fxml"));
            BorderPane page = (BorderPane) loader.load();
            Platform.runLater(()->{
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Choice Player Number");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            // Set the person into the controller.
            SetUpGameController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this,gameId);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();});
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/PersonOverview.fxml"));
            rootLayout = (BorderPane) loader.load();
            // Give the controller access to the main app.
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    private void playGame() {

    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void update(Exception e) {

    }

    @Override
    public void update(Message message) {
        if (message instanceof AskPlayerNameMessage) {
            askLoginController.setUserName();
        } else if (message instanceof NameOKMessage) {
            joinOrCreate((NameOKMessage) message);
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
                    setUpGame();
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
    public void sendMessage(Message message) {
        client.sendMessage(message);
    }
}
