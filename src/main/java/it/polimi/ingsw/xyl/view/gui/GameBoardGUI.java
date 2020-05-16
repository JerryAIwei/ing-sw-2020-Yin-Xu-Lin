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
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Translate;

import java.io.File;
import java.util.ArrayList;

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

    //GameBoard cube size
    final double CUBE_WIDTH = 6.0;
    final double CUBE_HEIGHT = 2.0;
    //distance between cube
    final double CUBE_DISTANCE = 1.0;
    //ratio of the builder
    final double RATIO_BUILDER = 1.8;
    int id;

    public MeshView[] getMaleBuilders() {
        return maleBuilders;
    }

    public MeshView[] getFemaleBuilders() {
        return femaleBuilders;
    }

    private static final MeshView[] maleBuilders = new MeshView[3];

    private static final MeshView[] femaleBuilders = new MeshView[3];


    static {
        maleBuilders[0] = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/MaleBuilder.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/MaleBuilder_Blue_v001.png");
        maleBuilders[1] = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/MaleBuilder.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/MaleBuilder_Orange_v001.png");
        maleBuilders[2] = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/MaleBuilder.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/MaleBuilder_Pink_v001.png");
        femaleBuilders[0] = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/FemaleBuilder_Blue.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/FemaleBuilder_Blue_v001.png");
        femaleBuilders[1] = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/FemaleBuilder_Blue.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/FemaleBuilder_Orange_v001.png");
        femaleBuilders[2] = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Builders/FemaleBuilder_Blue.obj",
                "src/main/resources/santorini_risorse-grafiche-2/Texture2D/FemaleBuilder_Pink_v001.png");

    }

    SmartGroup objs = new SmartGroup();
    
    ArrayList<Direction> availableMove;
    ArrayList<Direction> availableBuild;

    private static Block[][] maps = new Block[5][5];

    /**
     * Load mesh from file
     * @param meshPath path of the mesh
     * @param pmPath path of the 2D Texture
     */
    private static MeshView setUPMeshView(String meshPath, String pmPath) {
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
    private static MeshView setUPMeshView() {
        MeshView meshView;
        meshView = Loader.loadObj("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/Dome.obj");
        meshView.setMaterial(new PhongMaterial(Color.BLUE));
        return meshView;
    }

    class Block extends Group {

        private Level level = Level.GROUND;
        private int occupiedBy = -1;//-1: Block is empty
        private MeshView building01,building02,building03, dome;
        public Block(){

            building01 = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/BuildingBlock01.obj"
                    ,"src/main/resources/santorini_risorse-grafiche-2/Texture2D/BuildingBlock01_v001.png"
            );
            building02 = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/BuildingBlock02.obj"
                    ,"src/main/resources/santorini_risorse-grafiche-2/Texture2D/BuildingBlock02_v001.png"
            );
            building03 = setUPMeshView("src/main/resources/santorini_risorse-grafiche-2/Mesh/Buildings/BuildingBlock03.obj"
                    ,"src/main/resources/santorini_risorse-grafiche-2/Texture2D/BuildingBlock03_v001.png"
            );
            dome = setUPMeshView();
            building01.getTransforms().addAll(this.getTransforms());
            building01.getTransforms().add(new Translate(0,LEVEL1_HEIGHT,0));
            building02.getTransforms().addAll(this.getTransforms());
            building02.getTransforms().add(new Translate(0,LEVEL2_HEIGHT,0));
            building03.getTransforms().addAll(this.getTransforms());
            building03.getTransforms().add(new Translate(0,LEVEL3_HEIGHT,0));
            dome.getTransforms().addAll(this.getTransforms());
            dome.getTransforms().add(new Translate(0,DOME_HEIGHT,0));


            var box = new Box(CUBE_WIDTH, CUBE_HEIGHT, CUBE_WIDTH);
            box.setMaterial(new PhongMaterial(Color.BURLYWOOD));
            box.getTransforms().addAll(this.getTransforms());
            this.getChildren().add(box);
        }

        public void setLevel(Level level) {
            this.level = level;
        }

        public void setOccupiedBy(int occupiedBy) {
            this.occupiedBy = occupiedBy;
        }

        /**
         * should be used after any update to the Block
         */
        public void update(int id) {
            switch (this.level){
                case GROUND:break;
                case LEVEL1:
                    this.getChildren().addAll(building01);
                    break;
                case LEVEL2:
                    this.getChildren().addAll(building02);
                    break;
                case LEVEL3:
                    this.getChildren().addAll(building03);
                    break;
                case DOME:
                    this.getChildren().addAll(dome);
                    break;
            }
            if(occupiedBy!=-1){
                int playerID = occupiedBy/10;
                var builder = occupiedBy%10==0? maleBuilders[playerID]:femaleBuilders[playerID];
                //builder.getTransforms().addAll(this.getTransforms());
                builder.getTransforms().clear();
                builder.setScaleX(RATIO_BUILDER);
                builder.setScaleY(RATIO_BUILDER);
                builder.setScaleZ(RATIO_BUILDER);
                switch (this.level){
                    case GROUND:
                        builder.getTransforms().add(new Translate(0, BUILDER_GROUND,0));
                        break;
                    case LEVEL1:
                        builder.getTransforms().add(new Translate(0,BUILDER_LEVEL1,0));
                        break;
                    case LEVEL2:
                        builder.getTransforms().add(new Translate(0,BUILDER_LEVEL2,0));
                        break;
                    case LEVEL3:
                        builder.getTransforms().add(new Translate(0,BUILDER_LEVEL3,0));
                        break;
                }


                this.getChildren().add(builder);
            }

        }

    }

    public GameBoardGUI(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                maps[i][j] = new Block();
                maps[i][j].getTransforms().addAll
                        (new Translate((CUBE_WIDTH+CUBE_DISTANCE) * i-(2.5*CUBE_WIDTH+2*CUBE_DISTANCE),
                                0, (CUBE_WIDTH+CUBE_DISTANCE) * j-(2.5*CUBE_WIDTH+2*CUBE_DISTANCE)));
                objs.getChildren().add(maps[i][j]);
            }
        }
    }

    public void setId(int id){
        this.id = id;
    }

    /**
     * set up one block, used for debug
     * @param x col of the block
     * @param y row of the block
     * @param level set the level of the block
     * @param player play id on the block
     * */
    public void setMap(int x, int y, Level level, int player){
        if(x<0||x>4)return;
        if(y<0||y>4)return;
        maps[x][y].setOccupiedBy(player);
        maps[x][y].setLevel(level);
        maps[x][y].update(player);
    }
/**
 *transfer the game from game to the gameBoard in GUI
 * @param spaces 5*5 space metric from vGame
 * */
    public void setMaps(Space[][]spaces) {
        for(int i = 0;i<5;i++)
            for(int j=0;j<5;j++){
                maps[i][j].setOccupiedBy(spaces[i][j].isOccupiedBy());
                maps[i][j].setLevel(spaces[i][j].getLevel());
            }
    }


    public void setAvailableMove(ArrayList<Direction> availableMove) {
        this.availableMove = availableMove;
    }

    public void setAvailableBuild(ArrayList<Direction> availableBuild) {
        this.availableBuild = availableBuild;
    }

    public SmartGroup getObjs() {
        return objs;
    }


}
