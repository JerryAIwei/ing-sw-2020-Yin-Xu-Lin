package it.polimi.ingsw.xyl.view.gui;


import java.io.File;

import it.polimi.ingsw.xyl.util.Loader;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.*;
import javafx.stage.Stage;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 * Main.java
 * <p>
 * Handles high-level game logic, including game state and handling time
 * dependant loops.
 **/
public class Simple3DBoxApp extends Application {
    private static final boolean obj = true;
    private static final boolean filled = true;
    private Stage primaryStage;
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    /**
     *
     **/
    public Parent createContent() throws Exception {
        /* Import STL model */
        MeshView example, board,innerWall;
        if (obj) {
            example = Loader.loadObj
                    ("src/main/resources/santorini_risorse-grafiche-2/Mesh/Board/Cliff.obj");
            board = Loader.loadObj("src/main/resources/santorini_risorse-grafiche-2/Mesh/Board/Board.obj");
            innerWall = Loader.loadObj("src/main/resources/santorini_risorse-grafiche-2/Mesh/Board/InnerWalls.obj");
        } else {
            example = Loader.loadStl("src/main/resources/models/computer.stl");
        }
        if (filled) {
            PhongMaterial boardPm = new PhongMaterial();
            boardPm.setDiffuseMap(new Image((new File
                    ("src/main/resources/santorini_risorse-grafiche-2/Texture2D/Cliff_v001.png").toURI().toString())));
            boardPm.setSpecularColor(Color.WHITE);
            example.setMaterial(boardPm);


            innerWall.setMaterial(new PhongMaterial(Color.BLUE));

            board.setMaterial(new PhongMaterial(Color.RED));
        } else {
            example.setMaterial(new PhongMaterial(Color.RED));
            example.setDrawMode(DrawMode.LINE);
        }
        //board.getTransforms().add(new Rotate(-90,Rotate.X_AXIS));
        //example.getTransforms().add(new Rotate(180,Rotate.X_AXIS));
        /* Position the camera in the scene */
        PerspectiveCamera camera = new PerspectiveCamera(true);
        if (obj) {
            camera.getTransforms().addAll(
                    new Rotate(0, Rotate.Y_AXIS),
                    new Rotate(0, Rotate.X_AXIS),
                    new Translate(0, 0, -10)
            );
        } else {
            camera.getTransforms().addAll(
                    new Rotate(5, Rotate.Y_AXIS),
                    new Rotate(-110, Rotate.X_AXIS),
                    new Translate(0, 0, -20)
            );
        }

        camera.setNearClip(1);
        camera.setFarClip(1000);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case W:
                    camera.translateZProperty().set(camera.getTranslateZ() + 10);
                    break;
                case S:
                    camera.translateZProperty().set(camera.getTranslateZ() - 10);
                    break;
            }
        });


        innerWall.translateXProperty().set(0);
        innerWall.translateYProperty().set(0);
        innerWall.translateZProperty().set(0);
        board.translateXProperty().set(0);
        board.translateYProperty().set(0);
        board.translateZProperty().set(0);

        innerWall.addEventHandler(KeyEvent.KEY_PRESSED,keyEvent -> {
            switch (keyEvent.getCode()){
                case D: innerWall.translateXProperty().set(innerWall.getTranslateX()+10);break;
                case A: innerWall.translateXProperty().set(innerWall.getTranslateX()-10);break;
                case W: innerWall.translateYProperty().set(innerWall.getTranslateY()+10);break;
                case S: innerWall.translateYProperty().set(innerWall.getTranslateY()-10);break;
                case E: innerWall.translateZProperty().set(innerWall.getTranslateZ()+10);break;
                case Q: innerWall.translateZProperty().set(innerWall.getTranslateZ()-10);break;
            }
        });

        SmartGroup objs = new SmartGroup();
        //objs.getChildren().add(example);

        objs.getChildren().add(board);
        objs.getChildren().add(innerWall);



        /* Build the Scene Graph */
        Group root = new Group();
        root.getChildren().add(camera);
        root.getChildren().add(objs);
        /* Use a SubScene */
        SubScene subScene = new SubScene(root, 1280, 900);
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);

        primaryStage.addEventHandler(ScrollEvent.SCROLL, event -> {
            //Get how much scroll was done in Y axis.
            double delta = event.getDeltaY()/10;
            //Add it to the Z-axis location.
            objs.translateZProperty().set(objs.getTranslateZ() + delta);
        });

        Rotate xRotate;
        Rotate yRotate;
        objs.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        primaryStage.addEventHandler(MouseEvent.MOUSE_PRESSED,event->{
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
                }
        );
        primaryStage.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });


        return group;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        Scene scene = new Scene(createContent(),-1,-1,true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * main()
     * <p>
     * Pass along the command line arguments to an instance of the Main class.
     *
     * @param args The command line arguments.
     **/
    public static void main(String[] args) {
        /* TODO: Make decision about parsing command line arguments. */
        launch(args);
    }

    class SmartGroup extends Group {

        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        void rotateByY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
}