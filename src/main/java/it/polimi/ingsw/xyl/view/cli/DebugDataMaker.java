package it.polimi.ingsw.xyl.view.cli;

import it.polimi.ingsw.xyl.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * For debugging
 */
public class DebugDataMaker {
    private static final GameBoard gameBoard = new GameBoard(0);
    private static final VirtualGame vGame = new VirtualGame(0);
    private static final Player playerA = new Player(0,"11");
    private static final Player playerB = new Player(1, "22");
    private static final Player playerC = new Player(2, "33");
    private static final Player[] playerList = {playerA,playerB,playerC};
    private static final Collection<Player> players = new ArrayList<>();
    private static final Space[][] spaces = gameBoard.getIslandBoard().getSpaces();
    private static final ArrayList<GodPower> availableGodPowers = new ArrayList<>();
    private static final IslandBoardCLI islandBoardCLI = new IslandBoardCLI();
    private static boolean noLevelUp = false;
    private static int playerNumber = 3;
    private static int currentPlayerId = 0;


    public static void main(String[] args) {
        init();
        setPlayerNumber();
        setAvailableGodPowers();
        setPlayers();
        setSpace();
        saveData();
    }

    public static void init(){
        for (int i = 0; i !=5; i++){
            for (int j = 0; j !=5; j++){
                spaces[i][j]= new Space();
            }
        }
    }

    public static void setPlayerNumber(){
        System.out.print("Input the total player number: ");
        playerNumber = new Scanner(System.in).nextInt();
    }

    public static void setAvailableGodPowers(){
        for (int i = 0; i < GodPower.values().length; i++) {
            System.out.println(i + ":" + GodPower.values()[i].toString());
        }
        System.out.println("Please select " + playerNumber + " God Powers:");
        for (int i = 0; i < playerNumber; i++) {
            availableGodPowers.add(GodPower.values()[new Scanner(System.in).nextInt()]);
        }
        saveVGame();
    }

    public static void setPlayers(){
        players.add(playerA);
        players.add(playerB);
        if(playerNumber == 3)
            players.add(playerC);

        for(int i = 0; i <playerNumber;i++){
            playerList[i].setCurrentGameBoard(gameBoard);
        }

        for (int i = 0; i < availableGodPowers.size(); i++) {
            System.out.println(i + ":" + availableGodPowers.get(i).toString());
        }

        for(int i = 0 ; i <playerNumber; i++){
            System.out.print("Input the god power of player " + i + ": ");
            int power =  new Scanner(System.in).nextInt();
            playerList[i].setCosplayer(availableGodPowers.get(power).cosplay(playerList[i]));
            System.out.println(playerList[i].getCosplayer().getGodPower().toString());
        }

        for (int j =0 ;j< playerNumber;j++) {
            System.out.println("Input the worker position of player " + j + ": ");
            int[] xys = new int[4];
            for (int i = 0; i < 4; i++) {
                xys[i] = new Scanner(System.in).nextInt();
            }
            playerList[j].setWorkers(xys[0], xys[1], xys[2], xys[3]);
            spaces[xys[0]][xys[1]].setOccupiedBy(j*10);
            spaces[xys[2]][xys[3]].setOccupiedBy(j*10+1);

            vGame.updateVPlayers(players);
            saveVGame();
            islandBoardCLI.setPlayers(vGame);
            islandBoardCLI.setMaps(spaces,0);
            islandBoardCLI.showMaps();
        }

        System.out.println("Input the current player ID:");
        currentPlayerId = new Scanner(System.in).nextInt();
        saveVGame();
        islandBoardCLI.setPlayers(vGame);
        islandBoardCLI.setMaps(spaces,0);
        islandBoardCLI.showMaps();
    }

    public static void setSpace(){
        for (int i = 0; i < Level.values().length; i++) {
            System.out.println(i + ":" + Level.values()[i].toString());
        }
        for (int i = 0; i<5; i++){
            for (int j = 0; j<5;j++){
                islandBoardCLI.showMaps();
                System.out.println("Input the level of position (" +j+","+i+").");
                spaces[j][i].setLevel(Level.values()[new Scanner(System.in).nextInt()]);
                saveVGame();
                islandBoardCLI.setMaps(spaces,0);
            }
        }
        saveVGame();
        islandBoardCLI.setPlayers(vGame);
        islandBoardCLI.setMaps(spaces,0);
        islandBoardCLI.showMaps();
        System.out.println("Type 1 for no level up this turn, 0 to exit:");
        int input = new Scanner(System.in).nextInt();
        if (input == 1)
            noLevelUp = true;
    }

    public static void saveVGame(){
        vGame.setNoLevelUp(noLevelUp);
        vGame.setPlayerNumber(playerNumber);
        vGame.setGameStatus(GameStatus.INGAME);
        vGame.setCurrentPlayerId(currentPlayerId);
        vGame.setAvailableGodPowers(availableGodPowers);
        vGame.setSpaces(spaces);
    }

    public static void saveData(){
        vGame.setNoLevelUp(noLevelUp);
        vGame.setPlayerNumber(playerNumber);
        vGame.setGameStatus(GameStatus.INGAME);
        vGame.setCurrentPlayerId(currentPlayerId);
        vGame.setAvailableGodPowers(availableGodPowers);
        vGame.setSpaces(spaces);
        for (Player player :players){
            player.setCurrentStatus(PlayerStatus.WORKERPREPARED);
            player.getCosplayer().prepare();
        }
        vGame.updateVPlayers(players);
        vGame.save();
        System.out.println("Data saved, you can start server " +
                "and login with nickname 11, 22 (and 33 if 3-player game).");
    }

}
