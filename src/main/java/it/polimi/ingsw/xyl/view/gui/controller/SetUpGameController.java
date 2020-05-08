package it.polimi.ingsw.xyl.view.gui.controller;

import it.polimi.ingsw.xyl.model.message.SetPlayerNumberMessage;
import it.polimi.ingsw.xyl.view.gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class SetUpGameController {

    ObservableList list = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<Integer> cb;

    private Stage dialogStage;
    private GUI gui;
    private int gameID;

    boolean isOK = false;

    @FXML
    public void initialize() {
        cb.getItems().addAll(2, 3);
    }


    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isOK) return;
        var select = cb.getValue();
        if (select != null) {
            gui.sendMessage(new SetPlayerNumberMessage(gameID, select));
            dialogStage.close();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(GUI gui, int gameID) {
        this.gui = gui;
        this.gameID = gameID;
    }
}
