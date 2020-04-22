package it.polimi.ingsw.xyl.model;

import java.io.Serializable;

/**
 * Necessary statuses of a game board.
 *
 * @author Shaoxun
 */
public enum GameStatus implements Serializable {
    WAITINGINIT,
    WAITINGPLAYER,              // the number of players in the game board is less than set value
    WAITINGSTART,               // all players are ready to start, the starter will start the game soon
    INGAME,                     // gaming
    GAMEENDED,                  // game ended
}
