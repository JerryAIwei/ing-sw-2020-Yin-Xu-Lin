package it.polimi.ingsw.xyl.view.cli;

import it.polimi.ingsw.xyl.model.Level;
import it.polimi.ingsw.xyl.model.Space;
import it.polimi.ingsw.xyl.util.ColorSetter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IslandBoardCLICLITest {
    @Test
    public void IslandBoardTest_Block(){
        IslandBoardCLI islandBoardCLI = new IslandBoardCLI();
        IslandBoardCLI.Block block = islandBoardCLI.new Block(Level.GROUND,-1);
        System.out.print(block.getContent()+"\n");
        assertEquals(block.getContent(), ColorSetter.BG_BLUE.setColor("    "));
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