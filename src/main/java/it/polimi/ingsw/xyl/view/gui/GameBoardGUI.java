package it.polimi.ingsw.xyl.view.gui;

import it.polimi.ingsw.xyl.model.Direction;
import it.polimi.ingsw.xyl.model.Level;
import it.polimi.ingsw.xyl.model.Space;
import it.polimi.ingsw.xyl.util.Loader;
import it.polimi.ingsw.xyl.util.SmartGroup;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Translate;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Coordinate
 * --->x
 * |
 * z
 * Class to show the builder and building
 *
 * @author yaw
 */
public class GameBoardGUI {
    //height of the buiding
    final double LEVEL1_HEIGHT = 1.0;
    final double LEVEL2_HEIGHT = 4.9;
    final double LEVEL3_HEIGHT = 8.0;
    final double DOME_HEIGHT = 10.5;
    //height of the builder
    final double BUILDER_GROUND = 1.4;
    final double BUILDER_LEVEL1 = 3.6;
    final double BUILDER_LEVEL2 = 5.7;
    final double BUILDER_LEVEL3 = 6.6;

    //height of the target
    final double TARGET_GROUND = 2.0;
    final double TARGET_LEVEL1 = 5.5;
    final double TARGET_LEVEL2 = 9.5;
    final double TARGET_LEVEL3 = 11.0;

    //GameBoard cube size
    final double CUBE_WIDTH = 6.0;
    final double CUBE_HEIGHT = 2.0;

    //Target Cylinder size
    final double TARGET_R = 2.5;
    final double TARGET_H = 0.5;


    //distance between cube
    final double CUBE_DISTANCE = 1.0;
    //ratio of the builder
    final double RATIO_BUILDER = 1.8;
    int id;

    public Builder[] getMaleBuilders() {
        return maleBuilders;
    }

    public Builder[] getFemaleBuilders() {
        return femaleBuilders;
    }

    private static final Builder[] maleBuilders = new Builder[3];

    private static final Builder[] femaleBuilders = new Builder[3];


    private SmartGroup objs = new SmartGroup();

    private ArrayList<Direction> availableMove = new ArrayList<>(Arrays.asList(Direction.values()));
    private ArrayList<Direction> availableBuild = new ArrayList<>(Arrays.asList(Direction.values()));

    private Block[][] maps = new Block[5][5];

    /**
     * Load mesh from file
     *
     * @param meshPath path of the mesh
     * @param pmPath   path of the 2D Texture
     */
    private MeshView setUPMeshView(String meshPath, String pmPath) {
        MeshView meshView;
        meshView = Loader.loadObj(meshPath);
        PhongMaterial boardPm = new PhongMaterial();
        boardPm.setDiffuseMap(new Image((new File
                (pmPath).toURI().toString())));
        meshView.setMaterial(boardPm);
        return meshView;
    }

    /**
     * Load dome from file
     */
    private MeshView setUPMeshView() {
        MeshView meshView;
        meshView = Loader.loadObj("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/Dome.obj");
        meshView.setMaterial(new PhongMaterial(Color.BLUE));
        return meshView;
    }

    public class Builder extends MeshView {
        private int[] position = {-1, -1};

        public int[] getPosition() {
            return position;
        }

        public void setPosition(int x, int y) {
            position[0] = x;
            position[1] = y;
        }

        public Builder(String meshPath, String pmPath) {
            super(Loader.loadMesh(meshPath));
            PhongMaterial boardPm = new PhongMaterial();
            boardPm.setDiffuseMap(new Image((new File
                    (pmPath).toURI().toString())));
            this.setMaterial(boardPm);
        }

    }

    public class Block extends Group {

        private Level level = Level.GROUND;
        private int occupiedBy = -1;//-1: Block is empty
        private MeshView building01, building02, building03, dome;
        private Cylinder target;//used for selection the block to move or build
        private int[] position = {-1, -1};
        private boolean domed = false;

        int[] getPosition() {
            return position;
        }

        void setPosition(int x, int y) {
            position[0] = x;
            position[1] = y;
        }

        public Block() {

            building01 = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/BuildingBlock01.obj"
                    , "src/main/resources/santorini_risorse-grafiche-2/Texture2D/BuildingBlock01_v001.png"
            );
            building02 = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/BuildingBlock02.obj"
                    , "src/main/resources/santorini_risorse-grafiche-2/Texture2D/BuildingBlock02_v001.png"
            );
            building03 = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/BuildingBlock03.obj"
                    , "src/main/resources/santorini_risorse-grafiche-2/Texture2D/BuildingBlock03_v001.png"
            );
            dome = setUPMeshView();
            building01.getTransforms().addAll(this.getTransforms());
            building01.getTransforms().add(new Translate(0, LEVEL1_HEIGHT, 0));
            building02.getTransforms().addAll(this.getTransforms());
            building02.getTransforms().add(new Translate(0, LEVEL2_HEIGHT, 0));
            building03.getTransforms().addAll(this.getTransforms());
            building03.getTransforms().add(new Translate(0, LEVEL3_HEIGHT, 0));
            dome.getTransforms().addAll(this.getTransforms());

            target = new Cylinder(TARGET_R, TARGET_H);
            target.setMaterial(new PhongMaterial(Color.ORANGERED));

            var box = new Box(CUBE_WIDTH, CUBE_HEIGHT, CUBE_WIDTH);
            box.setMaterial(new PhongMaterial(Color.BURLYWOOD));
            box.getTransforms().addAll(this.getTransforms());
            this.getChildren().add(box);
        }

        public void setLevel(Level level) {
            if (this.level != level) {
                if (level != Level.DOME)
                    this.level = level;
                else
                    domed = true;
                update(-1, true);
            }
        }

        public void levelUp(boolean isDome){
            if(isDome) {setLevel(Level.DOME);return;}
            switch (this.level){
                case GROUND:setLevel(Level.LEVEL1);break;
                case LEVEL1:setLevel(Level.LEVEL2);break;
                case LEVEL2:setLevel(Level.LEVEL3);break;
                case LEVEL3:setLevel(Level.DOME);break;
            }
        }

        public void setOccupiedBy(int occupiedBy) {
            if (this.occupiedBy != occupiedBy) {
                this.occupiedBy = occupiedBy;
                update(-1, false);
            }
        }

        public int getOccupiedBy() {
            return this.occupiedBy;
        }

        /**
         * should be used after any update to the Block
         */
        private void update(int id, boolean isLevel) {
            if (isLevel) {
                switch (this.level) {
                    case GROUND:
                        if(domed) dome.getTransforms().add(new Translate(0,LEVEL1_HEIGHT+0.5, 0));
                        break;
                    case LEVEL1:
                        if(domed) dome.getTransforms().add(new Translate(0,LEVEL2_HEIGHT+0.5, 0));
                        else this.getChildren().addAll(building01);
                        break;
                    case LEVEL2:
                        if(domed) dome.getTransforms().add(new Translate(0,LEVEL3_HEIGHT+1, 0));
                        else this.getChildren().addAll(building02);
                        break;
                    case LEVEL3:
                        if(domed) dome.getTransforms().add(new Translate(0,DOME_HEIGHT, 0));
                        else this.getChildren().addAll(building03);
                        break;
                }
                if(domed)this.getChildren().addAll(dome);
            } else {
                if (occupiedBy != -1) {
                    int playerID = occupiedBy / 10;
                    var builder = occupiedBy % 10 == 0 ? maleBuilders[playerID] : femaleBuilders[playerID];
                    builder.setPosition(position[0], position[1]);
                    //builder.getTransforms().addAll(this.getTransforms());
                    builder.getTransforms().clear();
                    builder.setScaleX(RATIO_BUILDER);
                    builder.setScaleY(RATIO_BUILDER);
                    builder.setScaleZ(RATIO_BUILDER);
                    switch (this.level) {
                        case GROUND:
                            builder.getTransforms().add(new Translate(0, BUILDER_GROUND, 0));
                            break;
                        case LEVEL1:
                            builder.getTransforms().add(new Translate(0, BUILDER_LEVEL1, 0));
                            break;
                        case LEVEL2:
                            builder.getTransforms().add(new Translate(0, BUILDER_LEVEL2, 0));
                            break;
                        case LEVEL3:
                            builder.getTransforms().add(new Translate(0, BUILDER_LEVEL3, 0));
                            break;
                    }


                    this.getChildren().add(builder);
                }
            }

        }

        public void showTarget() {
            target.getTransforms().clear();
            switch (this.level) {
                case GROUND:
                    target.getTransforms().add(new Translate(0, TARGET_GROUND, 0));
                    break;
                case LEVEL1:
                    target.getTransforms().add(new Translate(0, TARGET_LEVEL1, 0));
                    break;
                case LEVEL2:
                    target.getTransforms().add(new Translate(0, TARGET_LEVEL2, 0));
                    break;
                case LEVEL3:
                    target.getTransforms().add(new Translate(0, TARGET_LEVEL3, 0));
                    break;
            }
            try {
                this.getChildren().add(target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void removeTarget() {
            try {
                this.getChildren().remove(target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Cylinder getTarget() {
            return target;
        }
    }

    public GameBoardGUI() {
        maleBuilders[0] = new Builder("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/MaleBuilder.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/MaleBuilder_Blue_v001.png");
        maleBuilders[1] = new Builder("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/MaleBuilder.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/MaleBuilder_Orange_v001.png");
        maleBuilders[2] = new Builder("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/MaleBuilder.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/MaleBuilder_Pink_v001.png");
        femaleBuilders[0] = new Builder("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/FemaleBuilder_Blue.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/FemaleBuilder_Blue_v001.png");
        femaleBuilders[1] = new Builder("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/FemaleBuilder_Blue.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/FemaleBuilder_Orange_v001.png");
        femaleBuilders[2] = new Builder("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/FemaleBuilder_Blue.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/FemaleBuilder_Pink_v001.png");

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                maps[i][j] = new Block();
                maps[i][j].getTransforms().addAll
                        (new Translate((CUBE_WIDTH + CUBE_DISTANCE) * i - (2.5 * CUBE_WIDTH + 2 * CUBE_DISTANCE),
                                0, (CUBE_WIDTH + CUBE_DISTANCE) * j - (2.5 * CUBE_WIDTH + 2 * CUBE_DISTANCE)));
                maps[i][j].setPosition(i, j);
                objs.getChildren().add(maps[i][j]);
            }
        }


    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    /**
     * set up one block, used for debug
     *
     * @param x      col of the block
     * @param y      row of the block
     * @param level  set the level of the block
     * @param player play id on the block
     */
    public void setMap(int x, int y, Level level, int player) {
        if (x < 0 || x > 4) return;
        if (y < 0 || y > 4) return;
        maps[x][y].setLevel(level);
        maps[x][y].setOccupiedBy(player);
    }

    /**
     * transfer the game from game to the gameBoard in GUI
     *
     * @param spaces 5*5 space metric from vGame
     */
    public void setMaps(Space[][] spaces) {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                maps[i][j].setLevel(spaces[i][j].getLevel());
                maps[i][j].setOccupiedBy(spaces[i][j].isOccupiedBy());
            }
    }

    public Block[][] getMaps() {
        return maps;
    }


    public void setAvailableMove(ArrayList<Direction> availableMove) {
        this.availableMove = availableMove;
    }

    public void setAvailableBuild(ArrayList<Direction> availableBuild) {
        this.availableBuild = availableBuild;
    }

    public ArrayList<Direction> getAvailableMove() {
        return availableMove;
    }

    public ArrayList<Direction> getAvailableBuild() {
        return availableBuild;
    }


    public SmartGroup getObjs() {
        return objs;
    }


}
