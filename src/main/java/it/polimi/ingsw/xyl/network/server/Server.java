package it.polimi.ingsw.xyl.network.server;

import it.polimi.ingsw.xyl.controller.GameController;
import it.polimi.ingsw.xyl.model.message.LoadDataMessage;
import it.polimi.ingsw.xyl.view.VirtualView;
//import org.objectweb.asm.commons.InstructionAdapter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for handling new connection
 *
 * create new PlayerServer for each player
 *
 * @author yaw
 */

public class Server
{
    public final static int SOCKET_PORT = 7777;
    private static final GameController gc = GameController.getSingleton();
    private static final VirtualView v = VirtualView.getSingleton();
    private static final Logger logger = Logger.getLogger("network.server.Server");

    //private static InstructionAdapter clients;
    //private Vector<Socket> clients = new Vector<>();

    public static void main(String[] args)
    {

        logger.log(Level.INFO, "Santorini game server starting.");
        gc.register(v);
        v.register(gc);
        checkData();
        v.update(new LoadDataMessage());
        ServerSocket socket;
        try {
            socket = new ServerSocket(SOCKET_PORT);
            logger.log(Level.INFO, "Server started.");
        } catch (IOException e) {
            System.err.println(e.toString());
            return;
        }

        while (true) {
            try {
                /* accepts connections; for every connection we accept,
                 * create a new Thread executing a ClientHandler */
                Socket client = socket.accept();
                //clients.add(client);

                PlayerServer playerServer = new PlayerServer(client,v);
                Thread thread = new Thread(playerServer, "server_" + client.getInetAddress());
                thread.start();

            } catch (IOException e) {
                System.out.println("connection dropped");
                return;
            }
        }
    }

     public static void checkData(){
        File dir = new File("./data/");
        boolean dirExists = dir.exists();
        if (!dirExists) {
            dirExists = dir.mkdirs();
        }
         File gameDir = new File("./data/game0");
         dirExists = gameDir.exists();
         if (dirExists && Objects.requireNonNull(dir.list()).length > 0) {
             try {
                 new Thread(() -> {
                     String input;
                     do {
                         System.out.print("Clean previous data? y/n:");
                         try (Scanner scanner = new Scanner(System.in)) {
                             input = scanner.nextLine();
                             if (input.trim().isEmpty()) {
                                 input = "n";
                                 logger.log(Level.INFO, "Time out, loading data...");
                             }
                         }
                     } while (!input.equals("y") && !input.equals("n"));
                     if (input.equals("y")) {
                         boolean deleted = false;
                         for (File file : Objects.requireNonNull(dir.listFiles())) {
                             if (file.isDirectory()) {
                                 File[] contents = file.listFiles();
                                 if (contents != null) {
                                     for (File f : contents) {
                                         deleted = f.delete();
                                     }
                                 }
                             }
                             deleted = file.delete();
                         }
                             if (deleted)
                                 logger.log(Level.INFO,"All previous data deleted.");
                     }
                 }).start();
                 Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                 Robot robot = new Robot();
                 robot.keyPress(KeyEvent.VK_ENTER);
                 robot.keyRelease(KeyEvent.VK_ENTER);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
    }

}
