package it.polimi.ingsw.xyl.view.cli;

import it.polimi.ingsw.xyl.model.Level;
import it.polimi.ingsw.xyl.model.Space;
import it.polimi.ingsw.xyl.util.ColorSetter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IslandBoardCLICLITest {

    @Test
    public void IslandBoardCLITest_AllCases(){
        // try to find out a proper color scheme
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLACK = "\u001B[30m";
        String ANSI_RED = "\u001B[1;31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_PURPLE = "\u001B[35m";
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_WHITE = "\u001B[37m";
        String ANSI_BLACK_BACKGROUND = "\u001B[40m";
        String ANSI_RED_BACKGROUND = "\u001B[41m";
        String ANSI_GREEN_BACKGROUND = "\u001B[42m";
        String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
        String ANSI_BLUE_BACKGROUND = "\u001B[44m";
        String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
        String ANSI_CYAN_BACKGROUND = "\u001B[46m";
        String ANSI_WHITE_BACKGROUND = "\u001B[47m";

        System.out.println(ANSI_GREEN_BACKGROUND + "    " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_GREEN_BACKGROUND + ANSI_BLACK + " 01 " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_GREEN_BACKGROUND  + ANSI_RED + " 01 " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_GREEN_BACKGROUND + " " + ANSI_BLACK + "|>" + " " +ANSI_RESET);
        System.out.println();

        System.out.println(ANSI_CYAN_BACKGROUND + "    " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_CYAN_BACKGROUND + ANSI_BLACK + " 01 " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_CYAN_BACKGROUND + ANSI_RED + " 01 " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_CYAN_BACKGROUND + " " + ANSI_BLACK + "|>" + " " +ANSI_RESET);
        System.out.println();

        System.out.println(ANSI_BLUE_BACKGROUND + "    " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_BLUE_BACKGROUND + ANSI_BLACK + " 01 " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_BLUE_BACKGROUND + ANSI_RED + " 01 " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_BLUE_BACKGROUND + " " + ANSI_BLACK + "|>" + " " +ANSI_RESET);
        System.out.println();

        System.out.println(ANSI_PURPLE_BACKGROUND + "    " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_PURPLE_BACKGROUND + ANSI_BLACK + " 01 " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_PURPLE_BACKGROUND + ANSI_RED + " 01 " + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_PURPLE_BACKGROUND + " " + ANSI_BLACK + "|>" + " " +ANSI_RESET);
        System.out.println();

    }



    @Test
    public void IslandBoardTest_Block(){
        IslandBoardCLI islandBoardCLI = new IslandBoardCLI();
        IslandBoardCLI.Block block = islandBoardCLI.new Block(Level.GROUND,-1);
        System.out.print(block.getContent()+"\n");
        assertEquals(block.getContent(), ColorSetter.BG_GREEN.setColor("    "));
    }

    @Test
    public void IslandBoardTest_showEmptyBoard(){
        IslandBoardCLI islandBoard = new IslandBoardCLI();
        islandBoard.showMaps();
        System.out.print("\n");
    }






    @Test
    public void IslandBoardTest_setMaps(){
        IslandBoardCLI islandBoard = new IslandBoardCLI();
        System.out.print("before setMaps\n");
        islandBoard.showMaps();
        System.out.print("\n");

        System.out.print("after setMaps\n");
        Space[][] spaces = new Space[5][5];
        for(int i = 0;i<5;i++)
            for(int j = 0;j<5;j++){
                spaces[i][j] = new Space();
                spaces[i][j].setLevel(Level.LEVEL1);
            }
        islandBoard.setMaps(spaces);
        islandBoard.showMaps();
        for(int i = 0;i<5;i++)
            for(int j = 0;j<5;j++){
                spaces[i][j].setLevel(Level.LEVEL2);
            }
        islandBoard.setMaps(spaces);
        islandBoard.showMaps();
        for(int i = 0;i<5;i++)
            for(int j = 0;j<5;j++){
                spaces[i][j].setLevel(Level.LEVEL3);
            }
        islandBoard.setMaps(spaces);
        islandBoard.showMaps();
        for(int i = 0;i<5;i++)
            for(int j = 0;j<5;j++){
                spaces[i][j].setLevel(Level.DOME);
            }
        islandBoard.setMaps(spaces);
        islandBoard.showMaps();
        for(int i = 0;i<5;i++)
            for(int j = 0;j<5;j++){
                spaces[i][j].setLevel(Level.GROUND);
            }
        islandBoard.setMaps(spaces);
        islandBoard.showMaps();
        for(int i = 0;i<5;i++)
            for(int j = 0;j<5;j++){
                spaces[i][j].setOccupiedBy((i+1)*10+j);
            }
        islandBoard.setMaps(spaces);
        islandBoard.showMaps();
        System.out.print("\n");
    }
}