package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;



/**
 * @author Lin Xin
 */

public class Artemis extends Cosplayer{

    private int[] initialPosition = new int[2];
    private int movedWorker = 0;
    public Artemis(Player player) {
        super(player);
        super.godPower = GodPower.ARTEMIS;
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
        if(getAvailableMoves(worker).contains(direction)){
            int targetPositionX = player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0];
            //positionX after move
            int targetPositionY = player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]; //positionY after move
            IslandBoard currentIslandBoard = player.getCurrentGameBoard().getIslandBoard(); //islandBoard before first move
            Space currentSpace = currentIslandBoard.getSpaces()  //the space before move
                    [player.getWorkers()[worker].getPositionX()][player.getWorkers()[worker].getPositionY()];
            if (nextAction == Action.MOVE) {
                    initialPosition[0] = player.getWorkers()[worker].getPositionX(); //store position before first move as initial position
                    initialPosition[1] = player.getWorkers()[worker].getPositionY();
                    player.getWorkers()[worker].setFromLevel(currentSpace.getLevel().toInt());  //store worker's level
                    currentSpace.setOccupiedBy(-1);   //free the space occupied before first move
                    player.getWorkers()[worker].setPosition(  //position after first move
                            player.getWorkers()[worker].getPositionX() + direction.toMarginalPosition()[0],
                            player.getWorkers()[worker].getPositionY() + direction.toMarginalPosition()[1]
                    );
                    player.getWorkers()[worker].resetForced();
                    //occupy space after first move
                    currentIslandBoard.getSpaces()[targetPositionX][targetPositionY].setOccupiedBy(player.getPlayerId() * 10 + worker);
                    movedWorker = worker;
                    nextAction = Action.MOVEORBUILD;
                    checkWin();
            } else if(!(targetPositionX == initialPosition[0] && targetPositionY == initialPosition[1])
                     && movedWorker == worker) {
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
            }else
                System.out.println("You cannot move back!");
        }else
                System.out.println("Your move is not available!");
    }
}
