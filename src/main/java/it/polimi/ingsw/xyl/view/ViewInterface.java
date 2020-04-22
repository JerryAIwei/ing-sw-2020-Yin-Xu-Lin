package it.polimi.ingsw.xyl.view;

import it.polimi.ingsw.xyl.model.VirtualGame;
import it.polimi.ingsw.xyl.model.message.Message;

public interface ViewInterface{

    void update(VirtualGame virtualGame);
    void update(Exception e);
    void update(Message message);
    void sendMessage(Message message);
}
