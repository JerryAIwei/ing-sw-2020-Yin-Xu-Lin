package it.polimi.ingsw.xyl.util;

import it.polimi.ingsw.xyl.model.PlayerStatus;
import org.junit.Test;

import static org.junit.Assert.*;

public class ColorSetterTest {
@Test
    public void ColorSetterTest_SetStringFg(){
    String willBeGreen = "I am Green\n";
    String willBeRed = "I am Red\n";
    String willBeBlue = "I am Blue\n";
    String willBeYellow = "I am Yellow\n";
    String greenString = ColorSetter.FG_GREEN.setColor(willBeGreen);
    String redString = ColorSetter.FG_RED.setColor(willBeRed);
    String blueString = ColorSetter.FG_BLUE.setColor(willBeBlue);
    String yellowString = ColorSetter.FG_YELLOW.setColor(willBeYellow);
    System.out.print(willBeGreen);
    System.out.print(greenString);
    System.out.print(willBeRed);
    System.out.print(redString);
    System.out.print(willBeBlue);
    System.out.print(blueString);
    System.out.print(willBeYellow);
    System.out.print(yellowString);
}
    @Test
    public void ColorSetterTest_SetSpaceFg(){
        String willBeGreen = " \n";
        String willBeRed = " \n";
        String willBeBlue = " \n";
        String willBeYellow = " \n";
        String greenString = ColorSetter.FG_GREEN.setColor(willBeGreen);
        String redString = ColorSetter.FG_RED.setColor(willBeRed);
        String blueString = ColorSetter.FG_BLUE.setColor(willBeBlue);
        String yellowString = ColorSetter.FG_YELLOW.setColor(willBeYellow);
        System.out.print(willBeGreen);
        System.out.print(greenString);
        System.out.print(willBeRed);
        System.out.print(redString);
        System.out.print(willBeBlue);
        System.out.print(blueString);
        System.out.print(willBeYellow);
        System.out.print(yellowString);
    }
    @Test
    public void ColorSetterTest_SetStringBg(){
        String willBeGreen = "My backgrand is Green\n";
        String willBeRed = "My backgrand is Red\n";
        String willBeBlue = "My backgrand is Blue\n";
        String willBeYellow = "My backgrand is Yellow\n";
        String greenString = ColorSetter.BG_GREEN.setColor(willBeGreen);
        String redString = ColorSetter.BG_RED.setColor(willBeRed);
        String blueString = ColorSetter.BG_BLUE.setColor(willBeBlue);
        String yellowString = ColorSetter.BG_YELLOW.setColor(willBeYellow);
        System.out.print(willBeGreen);
        System.out.print(greenString);
        System.out.print(willBeRed);
        System.out.print(redString);
        System.out.print(willBeBlue);
        System.out.print(blueString);
        System.out.print(willBeYellow);
        System.out.print(yellowString);
    }
    @Test
    public void ColorSetterTest_SetSpaceBg(){
        String willBeGreen = " \n";
        String willBeRed = " \n";
        String willBeBlue = " \n";
        String willBeYellow = " \n";
        String greenString = ColorSetter.BG_GREEN.setColor(willBeGreen);
        String redString = ColorSetter.BG_RED.setColor(willBeRed);
        String blueString = ColorSetter.BG_BLUE.setColor(willBeBlue);
        String yellowString = ColorSetter.BG_YELLOW.setColor(willBeYellow);
        System.out.print(willBeGreen);
        System.out.print(greenString);
        System.out.print(willBeRed);
        System.out.print(redString);
        System.out.print(willBeBlue);
        System.out.print(blueString);
        System.out.print(willBeYellow);
        System.out.print(yellowString);
    }

}