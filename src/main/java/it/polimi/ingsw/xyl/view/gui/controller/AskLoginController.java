package it.polimi.ingsw.xyl.view.gui.controller;

import it.polimi.ingsw.xyl.model.message.PlayerNameMessage;
import it.polimi.ingsw.xyl.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class AskLoginController {
    @FXML
    private TextField hostnameTextfield;
    @FXML
    private TextField portTextfield;
    @FXML
    private TextField usernameTextfield;
    @FXML
    private Label hostnameLabel;

    private GUI mainApp;
    private Stage dialogStage;

    boolean isOK =true;

    public AskLoginController() {
    }

    @FXML
    private void initialize() {
        hostnameTextfield.setText("127.0.0.1");
        hostnameTextfield.setPromptText("127.0.0.1");
        portTextfield.setText("7777");
        portTextfield.setPromptText("7777");
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

        if (hostnameTextfield.getText() == null || hostnameTextfield.getText().length() == 0)
            errorMessage += "No valid Input!\n";
        if (Objects.equals(hostnameLabel.getText(), "Hostname: ") && !hostnameTextfield.getText().matches(regex))
            errorMessage += "No valid IP!\n";
        if (usernameTextfield.getText().isEmpty() || usernameTextfield.getText().trim().isEmpty())
            errorMessage += "No valid username!\n";
        if (errorMessage.length() == 0) {
            return true;
        } else {
            System.err.println(errorMessage);
            return false;
        }
    }

    /**
     * Called when the user clicks Start.
     */
    @FXML
    private void handleStart() {
        if (isInputValid()) {
            usernameTextfield.setText("");
            usernameTextfield.setPromptText("Waiting...");
            hostnameTextfield.setText("");
            hostnameTextfield.setPromptText("Waiting...");
            portTextfield.setText("");
            portTextfield.setPromptText("Waiting...");
            isOK = false;
            try {
                System.out.println("initClient: " + hostnameTextfield.getText());
                mainApp.initClient(hostnameTextfield.getText());
            }catch (Exception ignored){
            }
            try{
                System.out.println("setUserName: " + usernameTextfield.getText());
                mainApp.setUserName(usernameTextfield.getText());
                mainApp.sendMessage(new PlayerNameMessage(usernameTextfield.getText()));
            }catch (Exception ignored){}
        }
    }

}
