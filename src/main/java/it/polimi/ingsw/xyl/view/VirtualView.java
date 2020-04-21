package it.polimi.ingsw.xyl.view;


import it.polimi.ingsw.xyl.controller.GameController;
import it.polimi.ingsw.xyl.model.GameBoard;
import it.polimi.ingsw.xyl.model.GameLobby;
import it.polimi.ingsw.xyl.model.VirtualGame;
import it.polimi.ingsw.xyl.model.message.Message;
import it.polimi.ingsw.xyl.model.message.NameOKMessage;
import it.polimi.ingsw.xyl.model.message.PlayerNameMessage;
import it.polimi.ingsw.xyl.model.message.AskPlayerNameMessage;
import it.polimi.ingsw.xyl.network.server.PlayerServer;
import it.polimi.ingsw.xyl.view.cli.CLI;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class VirtualView {
    private volatile static VirtualView singleton;
    private GameController gameController;
    private Map<Integer, VirtualGame>  vGames = new HashMap<>();
    //private Map<String,PlayerServer> allPayerServers = new HashMap<>();
    private Map<InetAddress,PlayerServer> initPlayerServers= new HashMap<>();
    private Map<Integer, Vector<PlayerServer>> allPayerServers = new HashMap<>();
    private CLI cli; //for debug
    private boolean debug = false;

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

    public void register(CLI cli){
        this.cli = cli;
        this.debug = true;
    }//for debug

    public void register(String ip){

    }

    public void update(InetAddress ip, boolean ok, GameLobby gl){
        if(ok){
            NameOKMessage nameOkMessage = new NameOKMessage(gl);
            initPlayerServers.get(ip).sendMessage(nameOkMessage);
        }else
            initPlayerServers.get(ip).sendMessage(new AskPlayerNameMessage());
    }

    public void update(int gameId, GameBoard gameBoard){
        if(vGames.get(gameBoard.getGameId()) == null){
            VirtualGame vGame = new VirtualGame();
            vGame.setGameId(gameBoard.getGameId());
            vGame.setGameStatus(gameBoard.getCurrentStatus());
            vGame.setCurrentPlayerId(gameBoard.getCurrentPlayer().getPlayerId());
            vGame.setAvailableGodPowers(gameBoard.getAvailableGodPowers());
            vGame.updateVPlayers(gameBoard.getPlayers().values());
            vGame.setSpaces(gameBoard.getIslandBoard().getSpaces());
            vGames.put(gameBoard.getGameId(), vGame);
            if (debug)
                cli.update(vGame);//for debug
            sendMessage(gameId, vGame);
        }else{
            VirtualGame vGame = vGames.get(gameBoard.getGameId());
            vGame.setGameStatus(gameBoard.getCurrentStatus());
            vGame.setCurrentPlayerId(gameBoard.getCurrentPlayer().getPlayerId());
            vGame.setAvailableGodPowers(gameBoard.getAvailableGodPowers());
            vGame.updateVPlayers(gameBoard.getPlayers().values());
            vGame.setSpaces(gameBoard.getIslandBoard().getSpaces());
            if (debug)
                cli.update(vGame);//for debug
        }

    }

    public void update(Message message) {
        gameController.update(message);
    }

    public void update(PlayerServer ps){
        initPlayerServers.put(ps.getIp(),ps);
        ps.sendMessage(new AskPlayerNameMessage());
    }
    public void update(PlayerNameMessage playerNameMessage,PlayerServer ps){}

    // only for test
    public Map<Integer, VirtualGame> getvGames() {
        return vGames;
    }

    public void sendMessage(int gameId, VirtualGame vGame){
        for(PlayerServer playerServer:allPayerServers.get(gameId)){
            playerServer.sendMessage(vGame);
        }
    }
}
