package it.polimi.ingsw.xyl.view.gui.controller;

import it.polimi.ingsw.xyl.view.gui.GameBoardGUI;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class GameBoardController {
    private GameBoardGUI gameBoardGUI;
    private Stage stage;
    //Variants for rotation
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private Boolean isMove = false;//true:move, false:build
    private Boolean isTurn = false;//true:you turn, can move or build
    private Boolean isDome = true;//special for a god power
    private AtomicInteger status = new AtomicInteger(0);//0:nothing select 1:select builder
    AtomicReference<GameBoardGUI.Builder> selectBuilder = new AtomicReference<>();

    public GameBoardController(GameBoardGUI gameBoardGUI, Stage stage) {
        this.gameBoardGUI = gameBoardGUI;
        this.stage = stage;
        setStageEvent();
        setBuilderEvent();
        testPosition();
        setTargetEvent();
    }

    private void setStageEvent() {
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

        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
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
                case M: isMove = true;break;
                case B: isMove = false;break;
                case O: isDome = true;break;
                case L:isDome = false;break;
            }
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            //Get how much scroll was done in Y axis.
            double delta = event.getDeltaY() / 10;
            //Add it to the Z-axis location.
            gameBoardGUI.getObjs().translateYProperty().set(gameBoardGUI.getObjs().getTranslateY() + delta);
        });
        stage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(event.getButton()== MouseButton.SECONDARY)
                if(status.get()==1){
                    hideTargets();
                    selectBuilder.set(null);
                    status.set(0);
                }
        });
    }

    private void setBuilderEvent() {
        for (int i = 0; i < 3; i++) {
            var builder = gameBoardGUI.getMaleBuilders()[i];
            int finalI = i;
            builder.addEventHandler(MouseEvent.MOUSE_CLICKED, keyEvent -> {
                if (status.get() == 0) {
                    selectBuilder.set(builder);
                    if (true/*isTurn&&gameBoardGUI.getId()== finalI*/) {
                        showTargets();
                        if (isMove) move();
                        else build();
                    }
                }
            });
        }
        for (int i = 0; i < 3; i++) {
            var builder = gameBoardGUI.getFemaleBuilders()[i];
            int finalI = i;
            builder.addEventHandler(MouseEvent.MOUSE_PRESSED, keyEvent -> {
                if (status.get() == 0) {
                    selectBuilder.set(builder);
                    if (true/*isTurn&&gameBoardGUI.getId()== finalI*/) {
                        showTargets();
                        if (isMove) move();
                        else build();
                    }
                }
            });
        }
    }

    private void setTargetEvent() {
        for (var maps : gameBoardGUI.getMaps()) {
            for (var map : maps) {
                map.getTarget().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    var builder = selectBuilder.get();
                    hideTargets();
                    if (isMove) {
                        var oldLocation = gameBoardGUI.getMaps()
                                [builder.getPosition()[0]]
                                [builder.getPosition()[1]];
                        int occupiedBy = oldLocation.getOccupiedBy();
                        oldLocation.setOccupiedBy(-1);
                        map.setOccupiedBy(occupiedBy);
                    } else {
                        map.levelUp(isDome);
                    }
                });
            }
        }
    }

    private void showTargets() {
        status.set(1);
        int x = selectBuilder.get().getPosition()[0];
        int y = selectBuilder.get().getPosition()[1];
        var directions = isMove ? gameBoardGUI.getAvailableMove() : gameBoardGUI.getAvailableBuild();
        for (var direction : directions) {
            switch (direction) {
                case UP:
                    gameBoardGUI.getMaps()[x][y - 1].showTarget();
                    break;
                case UPRIGHT:
                    gameBoardGUI.getMaps()[x + 1][y - 1].showTarget();
                    break;
                case RIGHT:
                    gameBoardGUI.getMaps()[x + 1][y].showTarget();
                    break;
                case DOWNRIGHT:
                    gameBoardGUI.getMaps()[x + 1][y + 1].showTarget();
                    break;
                case DOWN:
                    gameBoardGUI.getMaps()[x][y + 1].showTarget();
                    break;
                case DOWNLEFT:
                    gameBoardGUI.getMaps()[x - 1][y + 1].showTarget();
                    break;
                case LEFT:
                    gameBoardGUI.getMaps()[x - 1][y].showTarget();
                    break;
                case UPLEFT:
                    gameBoardGUI.getMaps()[x - 1][y - 1].showTarget();
                    break;
            }
        }
    }

    private void hideTargets() {
        status.set(0);
        int x = selectBuilder.get().getPosition()[0];
        int y = selectBuilder.get().getPosition()[1];
        var directions = isMove ? gameBoardGUI.getAvailableMove() : gameBoardGUI.getAvailableBuild();
        for (var direction : directions) {
            switch (direction) {
                case UP:
                    gameBoardGUI.getMaps()[x][y - 1].removeTarget();
                    break;
                case UPRIGHT:
                    gameBoardGUI.getMaps()[x + 1][y - 1].removeTarget();
                    break;
                case RIGHT:
                    gameBoardGUI.getMaps()[x + 1][y].removeTarget();
                    break;
                case DOWNRIGHT:
                    gameBoardGUI.getMaps()[x + 1][y + 1].removeTarget();
                    break;
                case DOWN:
                    gameBoardGUI.getMaps()[x][y + 1].removeTarget();
                    break;
                case DOWNLEFT:
                    gameBoardGUI.getMaps()[x - 1][y + 1].removeTarget();
                    break;
                case LEFT:
                    gameBoardGUI.getMaps()[x - 1][y].removeTarget();
                    break;
                case UPLEFT:
                    gameBoardGUI.getMaps()[x - 1][y - 1].removeTarget();
                    break;
            }
        }
    }

    private void testPosition() {
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
                    System.out.println("Q: " + selectBuilder.get().translateYProperty());
                    break;

                case E:
                    selectBuilder.get().translateYProperty().set(selectBuilder.get().getTranslateY() + 0.1);
                    System.out.println("Q: " + selectBuilder.get().translateYProperty());
                    break;

                case C:
                    selectBuilder.get().scaleXProperty().setValue(selectBuilder.get().getScaleX() - 0.005);
                    selectBuilder.get().scaleYProperty().setValue(selectBuilder.get().getScaleY() - 0.005);
                    selectBuilder.get().scaleZProperty().setValue(selectBuilder.get().getScaleZ() - 0.005);
                    System.out.println(selectBuilder.get().scaleXProperty().getValue() + " " + selectBuilder.get().scaleYProperty().getValue() + " " + selectBuilder.get().scaleZProperty().getValue());
                    break;
                case Z:
                    selectBuilder.get().scaleXProperty().setValue(selectBuilder.get().getScaleX() + 0.005);
                    selectBuilder.get().scaleYProperty().setValue(selectBuilder.get().getScaleY() + 0.005);
                    selectBuilder.get().scaleZProperty().setValue(selectBuilder.get().getScaleZ() + 0.005);
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
