package it.polimi.ingsw.xyl.view.gui.controller;

import it.polimi.ingsw.xyl.view.gui.GUI;
import javafx.fxml.FXML;

public class AskLoginController {


    private GUI mainApp;

    @FXML
    private void initialize() {
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(GUI mainApp) {
        this.mainApp = mainApp;

    }
}
