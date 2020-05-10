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
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[1;31m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";

    public class Block{
        private String content= "   ";
        private Level level = Level.GROUND;
        private int occupiedBy = -1;//-1: Block is empty
        private void upDateContent(){
            if(occupiedBy!=-1){
                if(occupiedBy==0)
                    content = " 0"+ occupiedBy +" ";
                else if(occupiedBy==1)
                    content = " 0"+ occupiedBy +" ";
                else
                    content = " "+ occupiedBy +" ";
            }
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

        private void updateContent(int playerId) {
            if (occupiedBy != -1) {
                String pre = (occupiedBy == playerId * 10 || occupiedBy == playerId * 10 + 1) ? (ANSI_RED) :
                        (ANSI_BLACK);
                if (occupiedBy == 0 || occupiedBy == 1) {
                    content = pre + " 0" + occupiedBy + " " + ANSI_RESET;
                } else
                    content = pre + " " + occupiedBy + " " + ANSI_RESET;
            } else {
                content = "    " + ANSI_RESET;
            }
            switch (level) {
                case GROUND:
                    content = ANSI_GREEN_BACKGROUND + content;
                    break;

                case LEVEL1:
                    content = ANSI_CYAN_BACKGROUND + content;
                    break;
                case LEVEL2:
                    content = ANSI_BLUE_BACKGROUND + content;
                    break;

                case LEVEL3:
                    content = ANSI_PURPLE_BACKGROUND + content;
                    break;

                case DOME:
                    content =
                            ANSI_PURPLE_BACKGROUND + " " + ANSI_BLACK + "|>" + " " + ANSI_RESET;
                    break;
            }
        }

        public Block(Level lever, int occupiedBy){
            this.level = lever;
            this.occupiedBy = occupiedBy;
            updateContent(-1);
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

        public boolean setLevel(Level level, int id){
            if(this.level ==level) return false;
            else{
                this.level = level;
                updateContent(id);
                return true;
            }
        }

        public boolean setOccupiedBy(int occupiedBy, int id){
            if(this.occupiedBy==occupiedBy) return false;
            else{
                this.occupiedBy = occupiedBy;
                updateContent(id);
                return true;
            }
        }
        public String getContent(){
            return content;
        }

    }
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

    public IslandBoardCLI(){
        for(int i = 0;i<5;i++)
            for(int j = 0;j<5;j++)
                maps[i][j] = new Block(Level.GROUND,-1);
    }

    private static Block[][] maps = new Block[5][5];

    private Map<Integer, Player> players = new HashMap<>();

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void showMaps(){


        for(int i = 4;i>=0;i--){
            for(int j=0;j<5;j++){
                System.out.print(maps[j][i].getContent() + " ");
            }
            System.out.println("\n");
        }


    }

    public void showPlayers(){
        for (Integer id:players.keySet())
            players.get(id).show();
    }

    public void setMaps(Space[][]spaces) {
        for(int i = 0;i<5;i++)
            for(int j=0;j<5;j++){
                maps[i][j].setOccupiedBy(spaces[i][j].isOccupiedBy());
                maps[i][j].setLevel(spaces[i][j].getLevel());
            }
    }

    public void setMaps(Space[][]spaces, int id){
        for(int i = 0;i<5;i++)
            for(int j=0;j<5;j++){
                maps[i][j].setOccupiedBy(spaces[i][j].isOccupiedBy(),id);
                maps[i][j].setLevel(spaces[i][j].getLevel(),id);
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
