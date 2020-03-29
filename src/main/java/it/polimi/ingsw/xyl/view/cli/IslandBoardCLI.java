package it.polimi.ingsw.xyl.view.cli;
import it.polimi.ingsw.xyl.model.Level;
import  it.polimi.ingsw.xyl.util.ColorSetter;

import java.util.HashMap;
import java.util.Map;
/**
 * This class show the islandBoard in the consoler
 *
 * @author yaw
 */
public class IslandBoardCLI {
    public class Block{
        private String content= "   ";
        private Level lever = Level.GROUND;
        private int occupiedBy = -1;//-1: Block is empty
        private void upDateContent(){
            if(occupiedBy!=-1)
                content = " "+ occupiedBy +" ";
            else content = "   ";
            switch (lever){
                case GROUND:
                    content= ColorSetter.BG_BLUE.setColor(content);
                    break;

                case LEVEL1:
                    content = ColorSetter.BG_GREEN.setColor(content);
                    break;
                case LEVEL2:
                    content = ColorSetter.BG_YELLOW.setColor(content);
                    break;

                case LEVEL3:
                    content = ColorSetter.BG_RED.setColor(content);
                    break;

                case DOME:
                    content = ColorSetter.BG_RED.setColor(" ")+ColorSetter.FG_RED.setColor("X")+ColorSetter.BG_RED.setColor(" ");
                    break;
            }
            }
        public Block(Level lever, int occupiedBy){
            this.lever = lever;
            this.occupiedBy = occupiedBy;
            upDateContent();
        }

        public boolean setLevel(Level lever){
            if(this.lever==lever) return false;
            else{
                this.lever = lever;
                upDateContent();
                return true;
            }
        }

        public boolean setOccupiedBy(int occupiedBy){
            if(this.occupiedBy==occupiedBy) return false;
            else{
                this.occupiedBy = occupiedBy;
                upDateContent();
                return true;
            }
        }
        public String getContent(){
            return content;
        }

    }
    private static Block[][] maps = new Block[5][5];
    public class Player{
        private int positionX;
        private int positionY;
        private int id;
        public Player(int positionX, int positionY,int id){
            this.positionX = positionX;
            this.positionY = positionY;
            this.id = id;
        }

        public int getPositionX() {
            return positionX;
        }

        public void setPositionX(int positionX) {
            this.positionX = positionX;
        }

        public int getPositionY() {
            return positionY;
        }

        public void setPositionY(int positionY) {
            this.positionY = positionY;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
    private Map<Integer, Player> players = new HashMap<Integer,Player>();
    public IslandBoardCLI(){
        for(int i = 0;i<5;i++)
            for(int j = 0;j<5;j++)
                maps[i][j] = new Block(Level.GROUND,-1);
    }
    public void show(){
        for(Block[] row:maps) {

            System.out.print("\n");
            for (Block block : row)
                System.out.print(block.getContent() + " ");
            System.out.print("\n");
        }
    }
    public void addPlayer(int positionX,int positionY,int id){
        players.put(id,new Player(positionX, positionY, id));
        maps[positionX][positionY].setOccupiedBy(id);
    }
    public void movePlayer(int positionX,int positionY,int id){
        maps[positionX][positionY].setOccupiedBy(id);
        maps[players.get(id).positionX][players.get(id).positionX].setOccupiedBy(-1);
        players.get(id).setPositionX(positionX);
        players.get(id).setPositionY(positionY);
    }
    public void build(int positionX,int positionY,Level level){
        maps[positionX][positionY].setLevel(level);
    }
}
