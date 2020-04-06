package it.polimi.ingsw.xyl.network.server;


import it.polimi.ingsw.xyl.model.message.Message;
import it.polimi.ingsw.xyl.view.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * Class for managing a player
 *
 * @author yaw
 */
public class PlayerServer implements Runnable
{
    private Socket client;
    private VirtualView vView;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    PlayerServer(Socket client, VirtualView vView)
    {
        this.client = client;
        this.vView = vView;


    }


    @Override
    public void run()
    {



        try {
            this.inputStream = new ObjectInputStream(this.client.getInputStream());
            this.outputStream = new ObjectOutputStream(this.client.getOutputStream());
            System.out.println("Connected to " + client.getInetAddress());
            try {
                while (true) {
                    Message clientMessage = (Message) inputStream.readObject();
                    vView.update(clientMessage);
                }
            }
            catch (ClassNotFoundException | ClassCastException e){
                System.out.println("invalid stream from client");
            }

            client.close();

        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }


    }

}