package it.polimi.ingsw.xyl.view.gui.controller;

import it.polimi.ingsw.xyl.model.message.CreateNewGameMessage;
import it.polimi.ingsw.xyl.model.message.JoinGameMessage;
import it.polimi.ingsw.xyl.model.message.NameOKMessage;
import it.polimi.ingsw.xyl.view.gui.GUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Vector;


public class GameLobbyController {
    @FXML
    private TableView<NameOKMessage.Games> gamesTable;
    @FXML
    private TableColumn<NameOKMessage.Games, Integer> gameID;
    @FXML
    private TableColumn<NameOKMessage.Games, Integer> CurrentNum;
    @FXML
    private TableColumn<NameOKMessage.Games, Integer> TotalNum;

    @FXML
    private Label firstPlayerLabel;
    @FXML
    private Label secondPlayerLabel;
    @FXML
    private Label thirdPlayerLabel;

    private Vector<Label> labels = new Vector<>();

    private boolean isNewGame = false;
    private boolean isJoinGame = false;

    // Reference to the main application.
    private GUI mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public GameLobbyController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        gameID.setCellValueFactory(new PropertyValueFactory<NameOKMessage.Games, Integer>("gameID"));
        CurrentNum.setCellValueFactory(new PropertyValueFactory<NameOKMessage.Games, Integer>("currentNumber"));
        TotalNum.setCellValueFactory(new PropertyValueFactory<NameOKMessage.Games, Integer>("playerNumber"));

        labels.add(firstPlayerLabel);
        labels.add(secondPlayerLabel);
        labels.add(thirdPlayerLabel);

        // Clear person details.
        showGameDetails(null);

        // Listen for selection changes and show the person details when changed.
        gamesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showGameDetails(newValue));
    }

    /**
     * Fills all text fields to show details about the game.
     * If the specified person is null, all text fields are cleared.
     *
     * @param game the game or null
     */
    private void showGameDetails(NameOKMessage.Games game) {
        for (var label : labels)
            label.setText("");
        if (game != null) {
            // Fill the labels with info from the person object.
            var players = game.getCurrentPlayers();
            for (int i = 0; i < players.size(); i++) {
                labels.get(i).setText(players.get(i));
            }
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewGame() {
        if(isNewGame) return;
        isNewGame = true;
        mainApp.sendMessage(new CreateNewGameMessage(mainApp.getUserName()));
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleJoinGame() {
        if(isJoinGame) return;
        isJoinGame = true;
        var selectedGame = gamesTable.getSelectionModel().getSelectedItem();
        if (selectedGame != null&&selectedGame.getCurrentNumber()!=selectedGame.getPlayerNumber()) {
            mainApp.sendMessage(new JoinGameMessage(mainApp.getUserName(), selectedGame.getGameID()));
        } else {
            // Nothing selected.
        }
    }


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(GUI mainApp, ObservableList<NameOKMessage.Games> games) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        gamesTable.setItems(games);
    }
}