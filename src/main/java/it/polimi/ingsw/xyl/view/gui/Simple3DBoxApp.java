package it.polimi.ingsw.xyl.view.gui;


import java.io.File;

import it.polimi.ingsw.xyl.util.Loader;
import it.polimi.ingsw.xyl.util.SmartGroup;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

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

    private MeshView setUPMeshView(String meshPath, String pmPath){
        MeshView meshView;
        meshView = Loader.loadObj(meshPath);
        PhongMaterial boardPm = new PhongMaterial();
        boardPm.setDiffuseMap(new Image((new File
                (pmPath).toURI().toString())));
        meshView.setMaterial(boardPm);
        return meshView;
    }
    public Parent createContent() throws Exception {
        /* Import STL model */
        MeshView builder,building01,building02,building03, dome;
        builder = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/MaleBuilder.obj"
        ,"src/main/resources/santorini_risorse-grafiche-2/Texture2D/MaleBuilder_Blue_v001.png"
        );
        building01 = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/BuildingBlock01.obj"
                ,"src/main/resources/santorini_risorse-grafiche-2/Texture2D/BuildingBlock01_v001.png"
        );
        building02 = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/BuildingBlock02.obj"
                ,"src/main/resources/santorini_risorse-grafiche-2/Texture2D/BuildingBlock02_v001.png"
        );
        building03 = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/BuildingBlock03.obj"
                ,"src/main/resources/santorini_risorse-grafiche-2/Texture2D/BuildingBlock03_v001.png"
        );
        dome = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/Dome.obj"
                ,"src/main/resources/santorini_risorse-grafiche-2/Texture2D/BuildingBlock03_v001.png"
        );
        dome.setMaterial(new PhongMaterial(Color.BLUE));
//        if (obj) {
//            builder = Loader.loadObj
//                    ("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/MaleBuilder.obj");
//            building01 = Loader.loadObj("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/BuildingBlock01.obj");
//            board = Loader.loadObj("src/main/resources/santorini_risorse-grafiche-2/Mesh/Board/Board.obj");
//            innerWall = Loader.loadObj("src/main/resources/santorini_risorse-grafiche-2/Mesh/Board/InnerWalls.obj");
//        } else {
//            builder = Loader.loadStl("src/main/resources/models/computer.stl");
//        }
//        if (filled) {
//            PhongMaterial boardPm = new PhongMaterial();
//            boardPm.setDiffuseMap(new Image((new File
//                    ("src/main/resources/santorini_risorse-grafiche-2/Texture2D/MaleBuilder_Blue_v001.png").toURI().toString())));
//            boardPm.setSpecularColor(Color.GREEN);
//            builder.setMaterial(boardPm);
//
//            PhongMaterial building01Pm = new PhongMaterial();
//            building01Pm.setDiffuseMap(new Image((new File
//                    ("src/main/resources/santorini_risorse-grafiche-2/Texture2D/BuildingBlock01_v001.png").toURI().toString())));
//            building01Pm.setSpecularColor(Color.GREEN);
//            building01.setMaterial(building01Pm);
//
//            innerWall.setMaterial(new PhongMaterial(Color.BLUE));
//            board.setMaterial(new PhongMaterial(Color.RED));
//
//            innerWall.setDepthTest(DepthTest.ENABLE);
//            board.setDepthTest(DepthTest.ENABLE);
//        } else {
//            builder.setMaterial(new PhongMaterial(Color.RED));
//            builder.setDrawMode(DrawMode.LINE);
//        }
        //board.getTransforms().add(new Rotate(-90,Rotate.X_AXIS));
        //builder.getTransforms().add(new Rotate(180,Rotate.X_AXIS));
        /* Position the camera in the scene */
        PerspectiveCamera camera = new PerspectiveCamera(true);
        if (obj) {
            camera.getTransforms().addAll(
                    new Rotate(0, Rotate.Y_AXIS),
                    new Rotate(0, Rotate.X_AXIS),
                    new Translate(5, 5, -10)
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

//        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//            switch (event.getCode()) {
//                case W:
//                    camera.translateZProperty().set(camera.getTranslateZ() + 10);
//                    break;
//                case S:
//                    camera.translateZProperty().set(camera.getTranslateZ() - 10);
//                    break;
//            }
//        });

        AtomicReference<MeshView> selectObj = new AtomicReference<>(builder);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
//                case D: example.translateXProperty().set(example.getTranslateX()+0.5);break;
//                case A: example.translateXProperty().set(example.getTranslateX()-0.5);break;
                case A:
                    selectObj.get().translateXProperty().set(selectObj.get().getTranslateX() - 7);
                    break;
                case D:
                    selectObj.get().translateXProperty().set(selectObj.get().getTranslateX() + 7);
                    break;
                case S:
                    selectObj.get().translateZProperty().set(selectObj.get().getTranslateZ() + 7);
                    break;
                case W:
                    selectObj.get().translateZProperty().set(selectObj.get().getTranslateZ() - 7);

                    break;
                case Q:
                    selectObj.get().translateYProperty().set(selectObj.get().getTranslateY() - 0.1);
                    System.out.println("Q: "+selectObj.get().translateYProperty());
                    break;

                case E:
                    selectObj.get().translateYProperty().set(selectObj.get().getTranslateY() + 0.1);
                    System.out.println("Q: "+selectObj.get().translateYProperty());
                    break;

                case C:
                    selectObj.get().scaleXProperty().setValue(selectObj.get().getScaleX()-0.005);
                    selectObj.get().scaleYProperty().setValue(selectObj.get().getScaleY()-0.005);
                    selectObj.get().scaleZProperty().setValue(selectObj.get().getScaleZ()-0.005);
                    System.out.println(selectObj.get().scaleXProperty().getValue() + " " + selectObj.get().scaleYProperty().getValue() + " " + selectObj.get().scaleZProperty().getValue());
                    break;
                case Z:
                    selectObj.get().scaleXProperty().setValue(selectObj.get().getScaleX()+0.005);
                    selectObj.get().scaleYProperty().setValue(selectObj.get().getScaleY()+0.005);
                    selectObj.get().scaleZProperty().setValue(selectObj.get().getScaleZ()+0.005);
                    System.out.println(selectObj.get().scaleXProperty().getValue() + " " + selectObj.get().scaleYProperty().getValue() + " " + selectObj.get().scaleZProperty().getValue());
                    break;
            }
        });


        Box box1 = new Box(2, 2, 2);
        Box box2 = new Box(2, 2, 2);
        box1.setMaterial(new PhongMaterial(Color.BLUE));
        box2.setMaterial(new PhongMaterial(Color.RED));
        box2.getTransforms().addAll(new Translate(0, 0, -2));
        builder.getTransforms().addAll(new Translate(0, 1.9, 0));
//        building01.getTransforms().addAll(new Translate(0, 0, 0));
//        building01.scaleXProperty().setValue(0.5);
//        building01.scaleYProperty().setValue(0.5);
//        building01.scaleZProperty().setValue(0.5);



        SmartGroup objs = new SmartGroup();
        //objs.getChildren().add(example);

//        objs.getChildren().add(board);
//        objs.getChildren().add(innerWall);
        objs.getChildren().addAll(builder, building01,building02,building03,dome);

        for(var obj:objs.getChildren()){
            obj.addEventHandler(MouseEvent.MOUSE_PRESSED, keyEvent -> {
                selectObj.set((MeshView) obj);
            });
        }

        Box[][] boxes = new Box[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boxes[i][j] = new Box(6, 2, 6);
                boxes[i][j].setMaterial(new PhongMaterial(Color.BLUE));
                boxes[i][j].getTransforms().addAll(new Translate(7 * i, 0, 7 * j));
                objs.getChildren().add(boxes[i][j]);
            }
        }

        /* Build the Scene Graph */
        Group root = new Group();
        root.getChildren().add(camera);
        root.getChildren().add(objs);
        /* Use a SubScene */
        SubScene subScene = new SubScene(root, 1920, 1080, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);

        primaryStage.addEventHandler(ScrollEvent.SCROLL, event -> {
            //Get how much scroll was done in Y axis.
            double delta = event.getDeltaY() / 10;
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
        primaryStage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
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
        primaryStage.setResizable(true);
        Scene scene = new Scene(createContent(), -1, -1, true);
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

}