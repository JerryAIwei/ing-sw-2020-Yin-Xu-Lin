package it.polimi.ingsw.xyl.view.cli;
import it.polimi.ingsw.xyl.util.ColorSetter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for Display CLI messages using JANSI LIBRARY.
 *
 * @author yaw
 */

public class CLI {

    synchronized void splashScreen() {

        System.out.println();

        System.out.println(ColorSetter.FG_BLUE.setColor(
                "   _____         _   _ _______ ____  _____  _____ _   _ _____ \n" +
                        "  / ____|  /\\   | \\ | |__   __/ __ \\|  __ \\|_   _| \\ | |_   _|\n" +
                        " | (___   /  \\  |  \\| |  | | | |  | | |__) | | | |  \\| | | |  \n" +
                        "  \\___ \\ / /\\ \\ | . ` |  | | | |  | |  _  /  | | | . ` | | |  \n" +
                        "  ____) / ____ \\| |\\  |  | | | |__| | | \\ \\ _| |_| |\\  |_| |_ \n" +
                        " |_____/_/    \\_\\_| \\_|  |_|  \\____/|_|  \\_\\_____|_| \\_|_____|\n" +
                        "                                                              \n" +
                        "                                                              "
        ));
        System.out.println();
        System.out.println("Made by"+ColorSetter.FG_GREEN.setColor(" XU Shaoxun,")+ColorSetter.FG_RED.setColor(" YIN Aiwei")+" and"+ColorSetter.FG_YELLOW.setColor(" LIN Xin"));
    }

    private static final int MAX_VERT_TILES = 5; //rows.
    private static final int MAX_HORIZ_TILES = 5; //cols.

    String tiles[][] = new String[MAX_VERT_TILES][MAX_HORIZ_TILES];

    List<String > weapons = new ArrayList<>();

    private void fillEmpy() {

        tiles[0][0] = "╔";
        for (int c = 1; c < MAX_HORIZ_TILES - 1; c++) {
            tiles[0][c] = "═";
        }

        tiles[0][MAX_HORIZ_TILES - 1] = "╗";

        for (int r = 1; r < MAX_VERT_TILES - 1; r++) {
            tiles[r][0] = "║";
            for (int c = 1; c < MAX_HORIZ_TILES - 1; c++) {
                tiles[r][c] = " ";
            }
            tiles[r][MAX_HORIZ_TILES-1] = "║";
        }

        tiles[MAX_VERT_TILES - 1][0] = "╚";
        for (int c = 1; c < MAX_HORIZ_TILES - 1; c++) {
            tiles[MAX_VERT_TILES - 1][c] = "═";
        }

        tiles[MAX_VERT_TILES - 1][MAX_HORIZ_TILES - 1] = "╝";

    }

    final void plot() {
        for (int r = 0; r < MAX_VERT_TILES; r++) {
            System.out.println();
            for (int c = 0; c < MAX_HORIZ_TILES; c++) {
                System.out.print(tiles[r][c]);
            }
        }
    }


}
