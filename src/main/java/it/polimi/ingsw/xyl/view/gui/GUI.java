package it.polimi.ingsw.xyl.view.gui;

import java.io.IOException;

import it.polimi.ingsw.xyl.view.gui.controller.AskLoginController;
import it.polimi.ingsw.xyl.view.gui.controller.PersonEditDialogController;
import it.polimi.ingsw.xyl.view.gui.controller.PersonOverviewController;
import it.polimi.ingsw.xyl.view.gui.model.Person;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GUI extends Application {


    private final int PREF_MIN_WIDTH = 1080;
    private final int PREF_MIN_HEIGHT = 800;

    private ObservableList<Person> personData = FXCollections.observableArrayList();
    private Stage primaryStage;
    private Stage loginStage;
    private BorderPane rootLayout;
    private BorderPane loginLayout;

    public GUI() {
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

        initRootLayout();
        askLogin();
        //showPersonOverview();
    }

    private void askLogin() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("/AskLogin.fxml"));
            loginLayout = loader.load();

            // Give the controller access to the main app.
            AskLoginController controller = loader.getController();
            controller.setMainApp(this);

            loginStage = new Stage();
            loginStage.setResizable(false);
            loginStage.setTitle("Login");
            loginStage.setAlwaysOnTop(true);
            loginStage.initOwner(primaryStage);
            Scene scene = new Scene(loginLayout);
            loginStage.setScene(scene);
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }


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

    public static void main(String[] args) {
        launch(args);
    }
}
