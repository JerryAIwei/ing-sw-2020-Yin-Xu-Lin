package it.polimi.ingsw.xyl.network.server;


import it.polimi.ingsw.xyl.model.VirtualGame;
import it.polimi.ingsw.xyl.model.message.AskPlayerNameMessage;
import it.polimi.ingsw.xyl.model.message.Message;
import it.polimi.ingsw.xyl.model.message.PlayerNameMessage;
import it.polimi.ingsw.xyl.view.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class for managing a player
 *
 * @author yaw
 */
public class PlayerServer implements Runnable {
    private Socket client;
    private VirtualView vView;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String playerName;

    public InetAddress getIp() {
        return ip;
    }

    private InetAddress ip;

    PlayerServer(Socket client, VirtualView vView) {
        this.client = client;
        this.vView = vView;
        this.ip = client.getInetAddress();
        try {
            this.inputStream = new ObjectInputStream(this.client.getInputStream());
            this.outputStream = new ObjectOutputStream(this.client.getOutputStream());
            System.out.println("Connected to " + client.getInetAddress());
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        sendMessage(new AskPlayerNameMessage());
        //vView.update(this);
    }


    @Override
    public void run() {

        while (true) {
            try {
                Message clientMessage = (Message) inputStream.readObject();

                if (clientMessage instanceof PlayerNameMessage) {
                    playerName = ((PlayerNameMessage) clientMessage).getPlayerName();
                    ((PlayerNameMessage) clientMessage).setPs(this);
                    System.out.println(((PlayerNameMessage) clientMessage).getPs().getIp());
                     vView.update(clientMessage);
                } else
                    vView.update(clientMessage);
            } catch (ClassNotFoundException | ClassCastException | IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sendMessage to server, called by View
     */
    public void sendMessage(Message message){
        try {
            if(message instanceof VirtualGame)
                System.out.println("sendMessage virtualGame"+((VirtualGame)message).getGameStatus());
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
