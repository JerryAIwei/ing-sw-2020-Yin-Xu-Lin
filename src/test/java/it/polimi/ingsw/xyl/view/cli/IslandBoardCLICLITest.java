package it.polimi.ingsw.xyl.view.cli;

import it.polimi.ingsw.xyl.model.Level;
import it.polimi.ingsw.xyl.util.ColorSetter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IslandBoardCLICLITest {
    @Test
    public void IslandBoardTest_Block(){
        IslandBoardCLI islandBoardCLI = new IslandBoardCLI();
        IslandBoardCLI.Block block = islandBoardCLI.new Block(Level.GROUND,-1);
        System.out.print(block.getContent()+"\n");
        assertEquals(block.getContent(), ColorSetter.BG_BLUE.setColor("   "));
    }

    @Test
    public void IslandBoardTest_showEmptyBoard(){
        IslandBoardCLI islandBoard = new IslandBoardCLI();
        islandBoard.show();
        System.out.print("\n");
    }
    @Test
    public void IslandBoardTest_addOnePlayer(){
        IslandBoardCLI islandBoard = new IslandBoardCLI();
        System.out.print("before add\n");
        islandBoard.show();
        System.out.print("\n");
        System.out.print("after add\n");
        islandBoard.addPlayer(0,0,1);
        islandBoard.show();
        System.out.print("\n");
    }
    @Test
    public void IslandBoardTest_buildNorm(){
        IslandBoardCLI islandBoard = new IslandBoardCLI();
        System.out.print("before build\n");
        islandBoard.show();
        System.out.print("\n");
        System.out.print("after build\n");
        islandBoard.build(0,0,Level.LEVEL1);
        islandBoard.show();
        System.out.print("\n");
    }

    @Test
    public void IslandBoardTest_buildDome(){
        IslandBoardCLI islandBoard = new IslandBoardCLI();
        System.out.print("before build Dome\n");
        islandBoard.show();
        System.out.print("\n");
        System.out.print("after build Dome\n");
        islandBoard.build(0,0,Level.DOME);
        islandBoard.show();
        System.out.print("\n");
    }

    @Test
    public void IslandBoardTest_MovePlayer(){
        IslandBoardCLI islandBoard = new IslandBoardCLI();
        System.out.print("before move\n");
        islandBoard.addPlayer(0,0,1);
        islandBoard.show();
        System.out.print("\n");

        System.out.print("after move\n");
        islandBoard.movePlayer(3,4,1);
        islandBoard.show();
        System.out.print("\n");
    }
}