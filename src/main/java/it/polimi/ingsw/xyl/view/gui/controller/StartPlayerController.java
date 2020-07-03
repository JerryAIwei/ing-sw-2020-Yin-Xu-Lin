package it.polimi.ingsw.xyl.view.gui.controller;

import it.polimi.ingsw.xyl.model.message.SetPlayerNumberMessage;
import it.polimi.ingsw.xyl.model.message.StartGameMessage;
import it.polimi.ingsw.xyl.view.gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 * UI controller for choose which player to start
 *
 * @author yaw
 */
public class StartPlayerController {

    ObservableList list = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<Integer> cb;

    private Stage dialogStage;
    private GUI gui;
    private int gameID;

    boolean isOK = false;

    @FXML
    public void initialize() {

    }


    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isOK) return;
        var select = cb.getValue();
        if (select != null) {
            gui.sendMessage(new StartGameMessage(gameID, gui.getUserName(), select));
            dialogStage.close();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(GUI gui, int gameID, int playerNumer) {
        this.gui = gui;
        this.gameID = gameID;
        if (playerNumer == 2)
            cb.getItems().addAll(0, 1);
        else
            cb.getItems().addAll(0, 1, 2);
    }
}
