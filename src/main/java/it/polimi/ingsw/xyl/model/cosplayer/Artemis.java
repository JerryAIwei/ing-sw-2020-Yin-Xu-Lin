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
     * Worker can only build after second move because of toNextPlayer（）
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    public void move(int worker, Direction direction) {
        int targetPositionX = this.getPlayer().getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0]; //positionX after move
        int targetPositionY = this.getPlayer().getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]; //positionY after move
        IslandBoard currentIslandBoard = this.getPlayer().getCurrentGameBoard().getIslandBoard(); //islandBoard before first move
        Vector<Direction> availableMoves = getAvailableMoves(worker);
        Space currentSpace = currentIslandBoard.getSpaces()  //the space before move
                [this.getPlayer().getWorkers()[worker].getPositionX()][this.getPlayer().getWorkers()[worker].getPositionY()];

        if (firstMove) {
            if (availableMoves.contains(direction)) {
                this.initialPosition[0] = this.getPlayer().getWorkers()[worker].getPositionX(); //store position before first move as initial position
                this.initialPosition[1] = this.getPlayer().getWorkers()[worker].getPositionY();
                this.getPlayer().getWorkers()[worker].setFromLevel(currentSpace.getLevel().toInt());  //store worker's level
                currentSpace.setOccupiedBy(-1);   //free the space occupied before first move
                this.getPlayer().getWorkers()[worker].setPosition(  //position after first move
                        this.getPlayer().getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0],
                        this.getPlayer().getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]
                );
                //occupy space after first move
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setOccupiedBy(this.getPlayer().getPlayerId() * 10 + worker);
                checkWin();
                //then not first move
                this.firstMove = false;
            } else {
                System.out.println("Your move is not available!");
            }
        } else if(!(targetPositionX == initialPosition[0] && targetPositionY == initialPosition[1]) && availableMoves.contains(direction)) {
            this.getPlayer().getWorkers()[worker].setFromLevel(currentSpace.getLevel().toInt());  //store worker's level
            currentSpace.setOccupiedBy(-1);   //free the space occupied before second move
            this.getPlayer().getWorkers()[worker].setPosition(  //position after second move
                    this.getPlayer().getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0],
                    this.getPlayer().getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]
            );
            currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setOccupiedBy(this.getPlayer().getPlayerId() * 10 + worker); //occupy space after second move
            checkWin();
            this.firstMove = true;
        } else{
            System.out.println("Your move is not available!");
        }
    }
}
