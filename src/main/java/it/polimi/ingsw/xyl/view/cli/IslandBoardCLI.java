package it.polimi.ingsw.xyl.view.cli;
import it.polimi.ingsw.xyl.model.*;
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
        private Level level = Level.GROUND;
        private int occupiedBy = -1;//-1: Block is empty
        private void upDateContent(){
            if(occupiedBy!=-1)
                content = " "+ occupiedBy +" ";
            else content = "    ";
            switch (level){
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
                    content = ColorSetter.BG_RED.setColor(" ")+ColorSetter.FG_RED.setColor("()")+ColorSetter.BG_RED.setColor(" ");
                    break;
            }
            }
        public Block(Level lever, int occupiedBy){
            this.level = lever;
            this.occupiedBy = occupiedBy;
            upDateContent();
        }

        public boolean setLevel(Level lever){
            if(this.level ==lever) return false;
            else{
                this.level = lever;
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
        private int id;

        public String getPlayerName() {
            return playerName;
        }

        private String playerName;

        public GodPower getGodPower() {
            return godPower;
        }

        private GodPower godPower;
        private String nextAction;
        private PlayerStatus playerStatus = PlayerStatus.INGAMEBOARD;
        public Player(int id,String playerName,GodPower godPower,String nextAction,PlayerStatus playerStatus){
            this.id = id;
            this.playerName = playerName;
            this.godPower = godPower;
            this.nextAction = nextAction;
            this.playerStatus = playerStatus;
        }
        public void show(){
            System.out.println(id+":"+ godPower +" "+playerName+" "+playerStatus);
        }

    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    private Map<Integer, Player> players = new HashMap<>();
    public IslandBoardCLI(){
        for(int i = 0;i<5;i++)
            for(int j = 0;j<5;j++)
                maps[i][j] = new Block(Level.GROUND,-1);
    }
    public void showMaps(){
        for(Block[] row:maps) {

            System.out.print("\n");
            for (Block block : row)
                System.out.print(block.getContent() + " ");
            System.out.print("\n");
        }
    }
    public void showPlayers(){
        for (Integer id:players.keySet())
            players.get(id).show();
    }

    public void setMaps(Space[][]spaces) {
        for(int i = 0;i<5;i++)
            for(int j=0;j<5;j++){
                maps[i][j].setLevel(spaces[i][j].getLevel());
                maps[i][j].setOccupiedBy(spaces[i][j].isOccupiedBy());
            }
    }
    public void setPlayers(VirtualGame virtualGame){
        players = new HashMap<>();
        for(Integer id:virtualGame.getVPlayers().keySet()){
            VirtualGame.VPlayer vPlayer = virtualGame.getVPlayers().get(id);
            Player player = new Player(id,vPlayer.getPlayerName(),
                    vPlayer.getGodPower(),vPlayer.getNextAction(),vPlayer.getPlayerStatus());
            players.put(id,player);
        }
    }
}
