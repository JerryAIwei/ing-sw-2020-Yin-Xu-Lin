package it.polimi.ingsw.xyl.view.gui.controller;

import it.polimi.ingsw.xyl.model.message.PlayerNameMessage;
import it.polimi.ingsw.xyl.view.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class AskLoginController {

    @FXML
    private TextField textField;

    @FXML
    private Label label;

    private GUI mainApp;
    private Stage dialogStage;
    private boolean isStart = false;

    @FXML
    private void initialize() {
        textField.setText("127.0.0.1");
        textField.setPromptText("127.0.0.1");
    }


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(GUI mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    private boolean isInputValid() {
        String errorMessage = "";
        final String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

        if (textField.getText() == null || textField.getText().length() == 0)
            errorMessage += "No valid Input!\n";
        if (Objects.equals(label.getText(), "Server IP") && !textField.getText().matches(regex))
            errorMessage += "No valid IP!\n";
        if (errorMessage.length() == 0) {
            return true;
        } else {
            System.err.println(errorMessage);
            return false;
        }
    }

    public void setUserName() {
        Platform.runLater(() -> {
            label.setText("User Name");
            textField.setText("");
            textField.setPromptText("UserName");
            isStart = false;
        });
    }

    /**
     * Called when the user clicks Start.
     */
    @FXML
    private void handleStart() {
        if (isInputValid() && !isStart) {
            if (label.getText().equals("Server IP")) {
                System.out.println("initClient: "+textField.getText());
                mainApp.initClient(textField.getText());
                isStart = true;
            } else { //User Name
                System.out.println("setUserName: " +textField.getText());
                mainApp.setUserName(textField.getText());
                mainApp.sendMessage(new PlayerNameMessage(textField.getText()));
                isStart = true;
            }
        } else {
            if(isStart) {
                textField.setText("");
                textField.setPromptText("Waiting...");
            }
            else {
                textField.setText("");
                textField.setPromptText("Invalid " + label.getText());
            }
        }
    }

}
