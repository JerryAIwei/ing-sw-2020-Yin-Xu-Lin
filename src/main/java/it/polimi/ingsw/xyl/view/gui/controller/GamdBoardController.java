package it.polimi.ingsw.xyl.view.gui.controller;

import it.polimi.ingsw.xyl.view.gui.GameBoardGUI;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

public class GamdBoardController {
    private GameBoardGUI gameBoardGUI;
    private Stage stage;
    //Variants for rotation
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    Boolean isMove = true;//true:move, false:build
    Boolean isTurn = false;//true:you turn, can move or build
    AtomicReference<MeshView> selectBuilder = new AtomicReference<>();

    public GamdBoardController(GameBoardGUI gameBoardGUI, Stage stage) {
        this.gameBoardGUI = gameBoardGUI;
        this.stage = stage;
        setScale();
        setRotation();
        selectBuilder();
        testPosition();
        setBoxEvent();
    }

    private void setScale() {
        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            //Get how much scroll was done in Y axis.
            double delta = event.getDeltaY() / 10;
            //Add it to the Z-axis location.
            gameBoardGUI.getObjs().translateYProperty().set(gameBoardGUI.getObjs().getTranslateY() + delta);
        });
    }

    private void setRotation() {
        Rotate xRotate;
        Rotate yRotate;
        gameBoardGUI.getObjs().getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        stage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                    anchorX = event.getSceneX();
                    anchorY = event.getSceneY();
                    anchorAngleX = angleX.get();
                    anchorAngleY = angleY.get();
                }
        );
        stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        stage.addEventHandler(KeyEvent.KEY_PRESSED,event -> {
            switch (event.getCode()){
                case RIGHT:
                    gameBoardGUI.getObjs().translateXProperty().set(gameBoardGUI.getObjs().getTranslateX() + 0.5);
                    break;
                case LEFT:
                    gameBoardGUI.getObjs().translateXProperty().set(gameBoardGUI.getObjs().getTranslateX() - 0.5);
                    break;
                case DOWN:
                    gameBoardGUI.getObjs().translateZProperty().set(gameBoardGUI.getObjs().getTranslateZ() + 0.5);
                    break;
                case UP:
                    gameBoardGUI.getObjs().translateZProperty().set(gameBoardGUI.getObjs().getTranslateZ() - 0.5);
                    break;
            }
        });
    }

    private void setBoxEvent(){

    }

    private void selectBuilder() {
        for (var builder : gameBoardGUI.getMaleBuilders()) {
            builder.addEventHandler(MouseEvent.MOUSE_PRESSED, keyEvent -> {
                selectBuilder.set(builder);
                if (isTurn) {
                    if (isMove) move();
                    else build();
                }
            });
        }
        for (var builder : gameBoardGUI.getFemaleBuilders()) {
            builder.addEventHandler(MouseEvent.MOUSE_PRESSED, keyEvent -> {
                selectBuilder.set(builder);
                if (isTurn) {
                    if (isMove) move();
                    else build();
                }
            });
        }
    }

    private void testPosition(){
        stage.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
//                case D: example.translateXProperty().set(example.getTranslateX()+0.5);break;
//                case A: example.translateXProperty().set(example.getTranslateX()-0.5);break;
                case A:
                    selectBuilder.get().translateXProperty().set(selectBuilder.get().getTranslateX() - 7);
                    break;
                case D:
                    selectBuilder.get().translateXProperty().set(selectBuilder.get().getTranslateX() + 7);
                    break;
                case S:
                    selectBuilder.get().translateZProperty().set(selectBuilder.get().getTranslateZ() + 7);
                    break;
                case W:
                    selectBuilder.get().translateZProperty().set(selectBuilder.get().getTranslateZ() - 7);

                    break;
                case Q:
                    selectBuilder.get().translateYProperty().set(selectBuilder.get().getTranslateY() - 0.1);
                    System.out.println("Q: "+selectBuilder.get().translateYProperty());
                    break;

                case E:
                    selectBuilder.get().translateYProperty().set(selectBuilder.get().getTranslateY() + 0.1);
                    System.out.println("Q: "+selectBuilder.get().translateYProperty());
                    break;

                case C:
                    selectBuilder.get().scaleXProperty().setValue(selectBuilder.get().getScaleX()-0.005);
                    selectBuilder.get().scaleYProperty().setValue(selectBuilder.get().getScaleY()-0.005);
                    selectBuilder.get().scaleZProperty().setValue(selectBuilder.get().getScaleZ()-0.005);
                    System.out.println(selectBuilder.get().scaleXProperty().getValue() + " " + selectBuilder.get().scaleYProperty().getValue() + " " + selectBuilder.get().scaleZProperty().getValue());
                    break;
                case Z:
                    selectBuilder.get().scaleXProperty().setValue(selectBuilder.get().getScaleX()+0.005);
                    selectBuilder.get().scaleYProperty().setValue(selectBuilder.get().getScaleY()+0.005);
                    selectBuilder.get().scaleZProperty().setValue(selectBuilder.get().getScaleZ()+0.005);
                    System.out.println(selectBuilder.get().scaleXProperty().getValue() + " " + selectBuilder.get().scaleYProperty().getValue() + " " + selectBuilder.get().scaleZProperty().getValue());
                    break;
            }
        });
    }

    private void build() {

    }

    private void move() {

    }

    public void setMove() {
        isMove = true;
    }

    public void setBuild() {
        isMove = false;
    }

    public void moveOrBuild() {
    }

    public void setIsTurn() {
        isTurn = true;
    }
}
