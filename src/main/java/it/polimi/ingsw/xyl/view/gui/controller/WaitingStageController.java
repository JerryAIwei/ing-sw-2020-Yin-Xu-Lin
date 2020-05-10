package it.polimi.ingsw.xyl.view.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class WaitingStageController {
    @FXML
    private Text text;

    public void setText(String text){
        this.text.setText(text);
    }

}
