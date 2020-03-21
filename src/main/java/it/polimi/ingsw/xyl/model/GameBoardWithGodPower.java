package it.polimi.ingsw.xyl.model;

import java.util.ArrayList;

/**
 * This class is the abstraction of A Santorini GAME (GodPower Mod),
 * A game lobby handles with one or more game(board).
 *
 * @author Shaoxun
 */
public class GameBoardWithGodPower extends GameBoard {
    private ArrayList<GodPower> availableGodPowers;

    public GameBoardWithGodPower(int gameId, int playerNumber, IslandBoard islandBoard) {
        super(gameId, playerNumber, islandBoard);
    }

}
