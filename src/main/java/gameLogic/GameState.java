package gameLogic;

/**
 * The possible states of the game.
 */
public enum GameState {
    /**
     * The game is currently being played.
     */
    RUNNING,

    /**
     * The game is over and player 1 is the winner.
     */
    PLAYER1_WON,

    /**
     * The game is over and player 2 is the winner.
     */
    PLAYER2_WON
}
