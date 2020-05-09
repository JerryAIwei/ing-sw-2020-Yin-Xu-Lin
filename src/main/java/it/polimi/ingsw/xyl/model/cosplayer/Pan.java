package it.polimi.ingsw.xyl.model.cosplayer;

import it.polimi.ingsw.xyl.model.*;


public class Pan extends Cosplayer {

    public Pan(Player player) {
        super(player);
        super.godPower = GodPower.PAN;
    }

    /**
     * Pan will win if the worker moves down two or more levels
     */
    @Override
    public void checkWin() {
        IslandBoard currentIslandBoard = player.getCurrentGameBoard().getIslandBoard();

        boolean normalWin = (player.getWorkers()[0].fromLevel() != 3 &&
                !player.getWorkers()[0].isForced() &&
                currentIslandBoard.getSpaces()
                        [player.getWorkers()[0].getPositionX()]
                        [player.getWorkers()[0].getPositionY()].getLevel() == Level.LEVEL3)
                || (player.getWorkers()[1].fromLevel() != 3 &&
                !player.getWorkers()[1].isForced() &&
                currentIslandBoard.getSpaces()
                        [player.getWorkers()[1].getPositionX()]
                        [player.getWorkers()[1].getPositionY()].getLevel() == Level.LEVEL3);

        boolean powerWin = (player.getWorkers()[0].fromLevel() -
                currentIslandBoard.getSpaces()[player.getWorkers()[0].getPositionX()]
                        [player.getWorkers()[0].getPositionY()].getLevel().toInt() >= 2)
                || (player.getWorkers()[1].fromLevel() - currentIslandBoard.getSpaces()
                        [player.getWorkers()[1].getPositionX()]
                        [player.getWorkers()[1].getPositionY()].getLevel().toInt() >= 2);

        if (normalWin || powerWin) {
            player.setCurrentStatus(PlayerStatus.WIN);
            player.getCurrentGameBoard().setCurrentStatus(GameStatus.GAMEENDED);
        }

        /* lose will keep same as Civilian Mod */
        boolean cannotMoveLose = workerInAction == -1 && nextAction == Action.MOVE
                && player.getCosplayer().getAvailableMoves(0).isEmpty()
                && player.getCosplayer().getAvailableMoves(1).isEmpty();

        boolean cannotMoveInOnesTurnLose = workerInAction != -1 && nextAction == Action.MOVE
                && player.getCosplayer().getAvailableMoves(workerInAction).isEmpty();

        boolean cannotMoveAndBuildLose = workerInAction == -1 && nextAction == Action.MOVEORBUILD
                && player.getCosplayer().getAvailableMoves(0).isEmpty()
                && player.getCosplayer().getAvailableMoves(1).isEmpty()
                && player.getCosplayer().getAvailableBuilds(0).isEmpty()
                && player.getCosplayer().getAvailableBuilds(1).isEmpty();

        boolean cannotBuildLose = nextAction == Action.BUILD
                && player.getCosplayer().getAvailableBuilds(workerInAction).isEmpty();

        boolean lose = cannotMoveLose || cannotMoveInOnesTurnLose || cannotBuildLose || cannotMoveAndBuildLose;

        if (lose) {
            player.setCurrentStatus(PlayerStatus.LOSE);
            int ax = player.getWorkers()[0].getPositionX();
            int ay = player.getWorkers()[0].getPositionY();
            int bx = player.getWorkers()[1].getPositionX();
            int by = player.getWorkers()[1].getPositionY();
            player.getCurrentGameBoard().getIslandBoard().getSpaces()[ax][ay].setOccupiedBy(-1);
            player.getCurrentGameBoard().getIslandBoard().getSpaces()[bx][by].setOccupiedBy(-1);
        }
    }
}
