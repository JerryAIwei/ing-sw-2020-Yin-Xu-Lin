package it.polimi.ingsw.xyl.model;

/**
 * Necessary statuses of a player.
 *
 * @author Shaoxun
 */
public enum PlayerStatus {
    INLOBBY,            // the player is in the game lobby (just connected)

    INGAMEBOARD,        // the player joined into a game board (or created a new one)
                        // (now the number of players in the game board is less than set value)

    GODPOWERED,         // the player chose his GodPower

    WAITING4START,       // the player is about to chose a Start Player

    WAITING4INIT,        // the player is about to place his two workers

    WORKERPREPARED,     // the player placed his two workers

    MYTURN,             // it is this player's turn

    NOTMYTURN,          // it is another player's turn

    WIN,                // the player won the game

    LOSE               // the player lost the game
}
