package it.polimi.ingsw.xyl.network.client;

import it.polimi.ingsw.xyl.model.VirtualGame;
import it.polimi.ingsw.xyl.model.message.Message;
import it.polimi.ingsw.xyl.network.server.Server;
import it.polimi.ingsw.xyl.view.ViewInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * Class for reacting with Server
 *
 * @author yaw
 */

public class Client implements Runnable{
    private Socket server;
    private ViewInterface view;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public Client(ViewInterface view){
        this.view=view;
    }

    /**
     * connect to server and start a new thread
     * @param ip IP address of the server
     */
    public void init(String ip){
        try {
            server = new Socket(ip, Server.SOCKET_PORT);
            outputStream = new ObjectOutputStream(server.getOutputStream());
            inputStream = new ObjectInputStream(server.getInputStream());
            outputStream.flush();
            (new Thread(this)).start();
        }
        catch(IOException e){
            view.update(e);
        }
    }
    /**
     * shutdown the connection safely
     */
    public void shutDown() {
        try {
            inputStream.close();
            outputStream.close();
            server.close();
        } catch (IOException e) {
            view.update(e);
        }
    }
    /**
     * method called when starting to run a thread
     * listen to the server then update the view
     */
    @Override
    public void run()
    {
        Thread.currentThread().setName("Socket Client Thread");

        while (true) {
            try {

                Message received = (Message) inputStream.readObject();
                if(received instanceof VirtualGame) {
                    view.update((VirtualGame) received);
                }
                else{
                    view.update(received);
                }
            } catch (IOException | ClassNotFoundException e) {
                view.update(e);
                break;
            }
        }
        shutDown();
    }
    /**
     *sendMessage to server, called by View
     *
     */
    public void sendMessage(Message message){
        try{
            outputStream.writeObject(message);
        }
        catch (IOException e){
            view.update(e);
        }
    }
}

