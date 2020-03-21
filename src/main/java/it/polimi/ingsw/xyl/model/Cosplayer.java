package it.polimi.ingsw.xyl.model;

/**
 * This class represents the role of the Player: God or Civilian
 *
 * @author Shaoxun
 */
public class Cosplayer {
    private Player player;
    private GodPower godPower;

    public Cosplayer(Player player){
        this.player = player;
    }
    public GodPower getGodPower() {
        return godPower;
    }

    public void setGodPower(GodPower godPower) {
        this.godPower = godPower;
    }

    public void move(Character worker, Direction direction){
        if (checkMove(worker,direction)) {
            player.setWorkerPosition(worker,
                    new int[]{player.getWorkerPosition(worker)[0] + direction.toMarginalPosition()[0],
                            player.getWorkerPosition(worker)[1] + direction.toMarginalPosition()[1]}
            );
        }else
            throw new IllegalStateException("Chosen worker can't be moved!");
    }

    /**
     * Check whether the chosen worker can be moved (Civilian mod).
     * Override this method in any God-Cosplayer Class.
     *
     * @param worker 'A' or 'B' represent two works of a player.
     * @param direction see Direction class.
     * @return whether or not the chosen worker can be moved.
     */
    private boolean checkMove(Character worker, Direction direction){
        // GameBoard currentGameBoard = player.getCurrentGameBoard();
        // IslandBoard currentIslandBoard = currentGameBoard.getIslandBoard();
        // int[] toBeMovedWorkerPosition =(worker=='A')?player.getWorkerAPosition():player.getWorkerBPosition();
        // TODO: checkMove of Civilian Mod
        return true;
    }
}