package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;

import java.util.ArrayList;


/**
 * @author Lin Xin
 */

public class Artemis extends Cosplayer{

    private Direction firstMoveDirection;
    public Artemis(Player player) {
        super(player);
        super.godPower = GodPower.ARTEMIS;
    }

    /**
     * move method with godpower Artemis.
     * Artemis' move: worker may move one additional time,
     * but not back to its initial space.
     *
     * @param worker '0' or '1' represent two workers (we call them worker A and B) of a player.
     * @param direction see Direction class.
     */
    @Override
    public void move(int worker, Direction direction) {
        if(getAvailableMoves(worker).contains(direction)){
            int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            //positionX after move
            int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]; //positionY after move
            IslandBoard currentIslandBoard = player.getCurrentGameBoard().getIslandBoard(); //islandBoard before first move
            Space currentSpace = currentIslandBoard.getSpaces()  //the space before move
                    [player.getWorkers()[worker].getPositionX()][player.getWorkers()[worker].getPositionY()];
            if (nextAction == Action.MOVE) {
                    player.getWorkers()[worker].setFromLevel(currentSpace.getLevel().toInt());  //store worker's level
                    currentSpace.setOccupiedBy(-1);   //free the space occupied before first move
                    player.getWorkers()[worker].setPosition(  //position after first move
                            player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0],
                            player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]
                    );
                    player.getWorkers()[worker].resetForced();
                    //occupy space after first move
                    currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setOccupiedBy(player.getPlayerId() * 10 + worker);
                    firstMoveDirection = direction;
                    workerInAction = worker;
                    nextAction = Action.MOVEORBUILD;
                    checkWin();
            } else if(nextAction == Action.MOVEORBUILD && workerInAction == worker) {
                player.getWorkers()[worker].setFromLevel(currentSpace.getLevel().toInt());  //store worker's level
                currentSpace.setOccupiedBy(-1);   //free the space occupied before second move
                player.getWorkers()[worker].setPosition(  //position after second move
                        player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0],
                        player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]
                );
                player.getWorkers()[worker].resetForced();
                currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setOccupiedBy(player.getPlayerId() * 10 + worker); //occupy space after second move
                nextAction = Action.BUILD;
                checkWin();
            }
        }else {
            System.out.println("Your move is not available!");
            throw new RuntimeException("Move not available.");
        }
    }

    @Override
    public ArrayList<Direction> getAvailableMoves(int worker) {
        ArrayList<Direction> availableMoves = super.getAvailableMoves(worker);
        if (nextAction == Action.MOVEORBUILD){
            availableMoves.remove(firstMoveDirection.toOpposite());
        }
        return availableMoves;
    }
}
