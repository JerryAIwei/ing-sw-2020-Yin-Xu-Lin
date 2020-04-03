package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import java.util.Vector;

import static it.polimi.ingsw.xyl.model.GodPower.ARTEMIS;

/**
 * @author Lin Xin
 */

public class Artemis extends Cosplayer{

    private boolean firstMove = true;
    private int[] initialPosition = new int[2];

    public Artemis(Player player) {
        super(player);
        super.godPower = ARTEMIS;
    }

    /**
     * move method with godpower Artemis.
     * Artemis' move: worker may move one additional time,
     * but not back to its initial space.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction) {
        if(firstMove){
            this.initialPosition[0] = this.getPlayer().getWorkers()[worker].getPositionX(); //position before first move
            this.initialPosition[1] = this.getPlayer().getWorkers()[worker].getPositionY();
            int targetPositionX = this.getPlayer().getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];//position after first move
            int targetPositionY = this.getPlayer().getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            IslandBoard currentIslandBoard = this.getPlayer().getCurrentGameBoard().getIslandBoard();//islandBoard before first move
            Vector<Direction> availableMoves = getAvailableMoves(worker);
            if(availableMoves.contains(direction)){
                Space currentSpace = currentIslandBoard.getSpaces()[this.getPlayer().getWorkers()[worker].getPositionX()][this.getPlayer().getWorkers()[worker].getPositionY()];//the space before first move
                this.getPlayer().getWorkers()[worker].setFromLevel(currentSpace.getLevel().toInt());  //store worker's level
                currentSpace.setOccupiedBy(-1);   //free the space occupied before first move
                this.getPlayer().getWorkers()[worker].setPosition(  //position after first move
                        this.getPlayer().getWorkers()[worker].getPositionX()+direction.toMarginalPosition()[0],
                        this.getPlayer().getWorkers()[worker].getPositionY()+direction.toMarginalPosition()[1]
                );
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setOccupiedBy(this.getPlayer().getPlayerId()*10+worker); //occupy space after first move
                checkWin();
            }
            else{
                System.out.println("Your move is not available!");
            }
            //then not first move
            this.firstMove = false;
        }
        else {
            //second move cannot move back to this.initialPosition
            int targetPositionX = this.getPlayer().getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];//position after second move
            int targetPositionY = this.getPlayer().getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1];
            IslandBoard currentIslandBoard = this.getPlayer().getCurrentGameBoard().getIslandBoard();//islandBoard before second move
            Vector<Direction> availableMoves = getAvailableMoves(worker);
            if (availableMoves.contains(direction)) {
                if (targetPositionX == initialPosition[0] && targetPositionY == initialPosition[1]) {  //remove if target is the initial position
                    availableMoves.remove(direction);
                    System.out.println("Your move is not available!");
                }
                else {
                    Space currentSpace = currentIslandBoard.getSpaces()[this.getPlayer().getWorkers()[worker].getPositionX()][this.getPlayer().getWorkers()[worker].getPositionY()];//the space before first move
                    this.getPlayer().getWorkers()[worker].setFromLevel(currentSpace.getLevel().toInt());  //store worker's level
                    currentSpace.setOccupiedBy(-1);   //free the space occupied before first move
                    this.getPlayer().getWorkers()[worker].setPosition(  //position after first move
                            this.getPlayer().getWorkers()[worker].getPositionX()+direction.toMarginalPosition()[0],
                            this.getPlayer().getWorkers()[worker].getPositionY()+direction.toMarginalPosition()[1]
                    );
                    currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setOccupiedBy(this.getPlayer().getPlayerId()*10+worker); //occupy space after first move
                    checkWin();
                }
            }
            else{
                System.out.println("Your move is not available!");
            }
        }
    }
}
