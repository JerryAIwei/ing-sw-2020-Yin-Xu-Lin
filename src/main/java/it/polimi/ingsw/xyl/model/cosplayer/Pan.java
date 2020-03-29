package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;


public class Pan extends Cosplayer {

    public Pan(Player player) {
        super(player);
        super.godPower = GodPower.PAN;
    }

    /**
     * Pan will win if the worker moves down two or more levels
     *
     */
    public void checkWin() {
        IslandBoard currentIslandBoard = this.getPlayer().getCurrentGameBoard().getIslandBoard();

        boolean normalWin = (this.getPlayer().getWorkers()[0].fromLevel() != 3 &&
                currentIslandBoard.getSpaces()
                        [this.getPlayer().getWorkers()[0].getPositionX()]
                        [this.getPlayer().getWorkers()[0].getPositionY()].getLevel() == Level.LEVEL3)
                || (this.getPlayer().getWorkers()[1].fromLevel() != 3 &&
                currentIslandBoard.getSpaces()
                        [this.getPlayer().getWorkers()[1].getPositionX()]
                        [this.getPlayer().getWorkers()[1].getPositionY()].getLevel() == Level.LEVEL3);

        boolean powerWin = (this.getPlayer().getWorkers()[0].fromLevel() == 2 &&
                currentIslandBoard.getSpaces()
                        [this.getPlayer().getWorkers()[0].getPositionX()]
                        [this.getPlayer().getWorkers()[0].getPositionY()].getLevel() == Level.GROUND)
                ||(this.getPlayer().getWorkers()[0].fromLevel() == 3 &&
                currentIslandBoard.getSpaces()
                        [this.getPlayer().getWorkers()[0].getPositionX()]
                        [this.getPlayer().getWorkers()[0].getPositionY()].getLevel() == Level.GROUND)
                ||(this.getPlayer().getWorkers()[0].fromLevel() == 3 &&
                currentIslandBoard.getSpaces()
                        [this.getPlayer().getWorkers()[0].getPositionX()]
                        [this.getPlayer().getWorkers()[0].getPositionY()].getLevel() == Level.LEVEL1)
                ||(this.getPlayer().getWorkers()[1].fromLevel() == 2 &&
                currentIslandBoard.getSpaces()
                        [this.getPlayer().getWorkers()[1].getPositionX()]
                        [this.getPlayer().getWorkers()[1].getPositionY()].getLevel() == Level.GROUND)
                ||(this.getPlayer().getWorkers()[1].fromLevel() == 3 &&
                currentIslandBoard.getSpaces()
                        [this.getPlayer().getWorkers()[1].getPositionX()]
                        [this.getPlayer().getWorkers()[1].getPositionY()].getLevel() == Level.GROUND)
                ||(this.getPlayer().getWorkers()[1].fromLevel() == 3 &&
                currentIslandBoard.getSpaces()
                        [this.getPlayer().getWorkers()[1].getPositionX()]
                        [this.getPlayer().getWorkers()[1].getPositionY()].getLevel() == Level.LEVEL1);
        if (normalWin || powerWin) {
            this.getPlayer().setCurrentStatus(PlayerStatus.WIN);
            // notify();
        }

        /* lose will keep same as Civilian Mod */
        boolean lose =
                this.getPlayer().getCosplayer().getAvailableMoves(0).size() == 0
                        && this.getPlayer().getCosplayer().getAvailableMoves(1).size() == 0;
        if (lose) {
            this.getPlayer().setCurrentStatus(PlayerStatus.LOSE);
        }
    }
}
