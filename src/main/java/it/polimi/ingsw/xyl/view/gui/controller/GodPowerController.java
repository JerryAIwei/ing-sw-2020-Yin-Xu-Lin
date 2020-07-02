package it.polimi.ingsw.xyl.view.gui.controller;

import it.polimi.ingsw.xyl.model.GodPower;
import it.polimi.ingsw.xyl.model.message.AvailableGodPowersMessage;
import it.polimi.ingsw.xyl.model.message.PlayerChooseGodPowerMessage;
import it.polimi.ingsw.xyl.view.gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.File;
import java.util.*;

/**
 * UI controller for choosing godpowers
 *
 * @author yaw
 */
public class GodPowerController {
    @FXML
    private TableView<GodPower> godPowerTable;
    @FXML
    private TableColumn<GodPower, String> godPower;
    @FXML
    private TextArea textArea;

    @FXML
    private ImageView imageView;

    private ObservableList<GodPower> availableGodPowers = FXCollections.observableArrayList();
    private int playerNum = -1;
    private GUI mainApp;
    private LinkedList<GodPower> godPowers = new LinkedList<>();
    private static HashMap<GodPower, Image> godImages = new HashMap<>();
    private ListIterator<GodPower> currentGodIt;
    private GodPower currentGod;
    private Stage stage;

    private boolean isSelect = false;
    private boolean isSend = false;
    private boolean isNext = true;


    private void initGodPowers(LinkedList<GodPower> godPowers) {
        currentGod = godPowers.get(0);
        for (var god : GodPower.values()) {
            var image = new Image(
                    (getClass().getResourceAsStream("/santorini_risorse-grafiche-2" +
                            "/Sprite/Cards/Full/" +
                            god.getGodPower() + ".png")));
            godImages.put(god, image);
        }
        imageView.setImage(godImages.get(currentGod));
        textArea.setText(currentGod.getGodPower()
                + ":\n\n" + currentGod.description());
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        godPower.setCellValueFactory(new PropertyValueFactory<>("godPower"));
        textArea.setWrapText(true);
    }

    /**
     * Is called by setGodPower.
     *
     * @param mainApp   gui
     * @param godPowers all available godPowers from sever
     */
    public void setMainApp(GUI mainApp, LinkedList<GodPower> godPowers) {
        this.mainApp = mainApp;
        this.godPowers = godPowers;
        availableGodPowers.addAll(godPowers);
        godPowerTable.setItems(availableGodPowers);
        currentGodIt = godPowers.listIterator();
        initGodPowers(this.godPowers);
//        // Add observable list data to the table
//        godPowerTable.setItems(godPowers);
    }

    /**
     * Is called by setAvailableGodPowers.
     *
     * @param mainApp gui
     */
    public void setMainApp(GUI mainApp, int playerNum) {
        this.mainApp = mainApp;
        this.godPowers.addAll(Arrays.asList(GodPower.values()));
        currentGodIt = godPowers.listIterator();
        this.playerNum = playerNum;
        initGodPowers(this.godPowers);

        //        // Add observable list data to the table
//        godPowerTable.setItems(godPowers);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleNext() {
        if (currentGodIt.hasNext()) {
            isSelect = false;
            currentGod = currentGodIt.next();
            imageView.setImage(godImages.get(currentGod));
            textArea.setText(currentGod.getGodPower()
                    + ":\n\n" + currentGod.description());
        }
    }

    @FXML
    private void handlePrev() {

        if (currentGodIt.hasPrevious()) {
            isSelect = false;
            currentGod = currentGodIt.previous();
            imageView.setImage(godImages.get(currentGod));
            textArea.setText(currentGod.getGodPower()
                    + ":\n\n" + currentGod.description());
        }
    }

    @FXML
    private void handleSelect() {
        if (isSelect || isSend) return;
        isSelect = true;
        if (playerNum == -1) {//setGodPower
            mainApp.sendMessage(new PlayerChooseGodPowerMessage
                    (mainApp.getGameId(), mainApp.getId(), currentGod));
            isSend = true;
            stage.close();
        } else {
            availableGodPowers.add(currentGod);
            godPowerTable.setItems(availableGodPowers);
            if (currentGod != GodPower.ANONYMOUS)
                currentGodIt.remove();
            if (availableGodPowers.size() == playerNum) {
                if (playerNum == 2)
                    mainApp.sendMessage(
                            new AvailableGodPowersMessage(
                                    mainApp.getGameId(),
                                    availableGodPowers.get(0),
                                    availableGodPowers.get(1)));
                else
                    mainApp.sendMessage(
                            new AvailableGodPowersMessage(
                                    mainApp.getGameId(),
                                    availableGodPowers.get(0),
                                    availableGodPowers.get(1),
                                    availableGodPowers.get(2)));
                isSend = true;
                stage.close();
            }
        }

    }


}

