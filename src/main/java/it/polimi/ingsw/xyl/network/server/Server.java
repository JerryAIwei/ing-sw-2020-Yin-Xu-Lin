package it.polimi.ingsw.xyl.network.server;

import it.polimi.ingsw.xyl.controller.GameController;
import it.polimi.ingsw.xyl.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
    private static GameController gc = new GameController();
    private static VirtualView v = new VirtualView(gc);


    public static void main(String[] args)
    {
        ServerSocket socket;
        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }

        while (true) {
            try {
                /* accepts connections; for every connection we accept,
                 * create a new Thread executing a ClientHandler */
                Socket client = socket.accept();
                PlayerServer playerServer = new PlayerServer(client,v);
                Thread thread = new Thread(playerServer, "server_" + client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                System.out.println("connection dropped");
            }
        }
    }
}
