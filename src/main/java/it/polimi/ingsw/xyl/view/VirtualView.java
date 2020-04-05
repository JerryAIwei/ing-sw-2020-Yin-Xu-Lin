package it.polimi.ingsw.xyl.view;


import it.polimi.ingsw.xyl.controller.GameController;
import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.VirtualGame;
import it.polimi.ingsw.xyl.model.message.Message;

import java.util.HashMap;
import java.util.Map;

public class VirtualView {
    private volatile static VirtualView singleton;
    private GameController gameController;
    private Map<Integer, VirtualGame>  vGames = new HashMap<>();

    private VirtualView (){}

    public static VirtualView getSingleton() {
        if (singleton == null) {
            synchronized (VirtualView.class) {
                if (singleton == null) {
                    singleton = new VirtualView();
                }
            }
        }
        return singleton;
    }

    public void destroy(){
        singleton = null;
    }

    public void register(GameController gc){
        gameController = gc;
    }

    public void update(GameBoard gameBoard){
        if(vGames.get(gameBoard.getGameId()) == null){
            VirtualGame vGame = new VirtualGame();
            vGame.setGameStatus(gameBoard.getCurrentStatus());
            vGame.updateVPlayers(gameBoard.getPlayers().values());
            vGame.setSpaces(gameBoard.getIslandBoard().getSpaces());
            vGames.put(gameBoard.getGameId(), vGame);
        }else{
            VirtualGame vGame = vGames.get(gameBoard.getGameId());
            vGame.setGameStatus(gameBoard.getCurrentStatus());
            vGame.updateVPlayers(gameBoard.getPlayers().values());
            vGame.setSpaces(gameBoard.getIslandBoard().getSpaces());
        }

    }

    public void update(Message message) {
        gameController.update(message);
    }

    // only for test
    public Map<Integer, VirtualGame> getvGames() {
        return vGames;
    }
}
