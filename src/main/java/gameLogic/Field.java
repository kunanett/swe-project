package gameLogic;

/**
 * The possible states of an individual field on the game's board.
 */
public enum Field {
    /**
     * The field is occupied by player 1.
     */
    PLAYER1,

    /**
     * The field is occupied by player 2.
     */
    PLAYER2,

    /**
     * The field is empty.
     */
    EMPTY,

    /**
     * The field is empty, but it has been stepped upon once, it is now unavailable.
     */
    UNAVAILABLE
}
