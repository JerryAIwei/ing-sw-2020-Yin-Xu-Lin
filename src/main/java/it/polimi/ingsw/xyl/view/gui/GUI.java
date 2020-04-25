package it.polimi.ingsw.xyl.view.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("My First JavaFX App");

        Label label = new Label("Hello World from JavaFX!");
        label.setAlignment(Pos.CENTER);

        // This sets the size of the Scene to be 400px wide, 200px high
        Scene scene = new Scene(label, 400, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
