package it.polimi.ingsw.xyl.view.cli;
import it.polimi.ingsw.xyl.model.VirtualGame;
import it.polimi.ingsw.xyl.model.message.Message;
import it.polimi.ingsw.xyl.network.client.Client;
import it.polimi.ingsw.xyl.util.ColorSetter;
import it.polimi.ingsw.xyl.view.ViewInterface;
import it.polimi.ingsw.xyl.model.message.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for Display CLI messages.
 *
 * @author yaw
 */

public class CLI implements ViewInterface {
    private IslandBoardCLI islandBoardCLI = new IslandBoardCLI();
    private Client client;
    private String userName;

    public static void main(String[] args) {
        System.out.println("new a cli");
        CLI cli= new CLI();
        cli.launch();
        System.out.println("finished");
    }

    public void CLI(){
        client = new Client(this);
    }

    @Override
    public void update(VirtualGame virtualGame) {

    }

    @Override
    public void update(Exception e) {

    }

    @Override
    public void sendMessage(Message message) {
        client.sendMessage(message);
    }


    public void launch(){
        splashScreen();
        askLogin();
    }

    /**
     * show welcome when start a new CLI
     * */
    private void splashScreen() {

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

    /**
     * Set ip and username
     * connect to server
     * called when start a new CLI
     * */

    private void askLogin(){
        System.out.println("Please Enter Server IP");
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.next();
        //client.init(ip);
        System.out.println("Please Enter Login Name");
        userName = scanner.next();
        //sendMessage(new PlayerNameMessage(userName));
    }



}
