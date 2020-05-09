package it.polimi.ingsw.xyl.network.server;


import it.polimi.ingsw.xyl.model.message.AskPlayerNameMessage;
import it.polimi.ingsw.xyl.model.message.ConnectionDroppedMessage;
import it.polimi.ingsw.xyl.model.message.Message;
import it.polimi.ingsw.xyl.model.message.PlayerNameMessage;
import it.polimi.ingsw.xyl.view.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger logger = Logger.getLogger("network.server.PlayerServer");

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
            logger.log(Level.INFO, "New connection from " + client.getInetAddress());
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
                }
                vView.update(clientMessage);
            } catch( ClassNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                vView.update(new ConnectionDroppedMessage(playerName));
//                logger.log(Level.WARNING, client.getInetAddress() + " connection dropped.");
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
            outputStream.writeObject(message);
            outputStream.reset();
        }catch(SocketException ignored){}
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
