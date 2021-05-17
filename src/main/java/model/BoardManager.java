package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Class that is responsible for the gameplay's logic.
 */
public class BoardManager {

    /**
     * Enum class for representing which player is next to move.
     */
    public enum NextPlayer {
        /**
         * Represents that player1 is next.
         */
        PLAYER1,
        /**
         * Represents that player2 is next.
         */
        PLAYER2;

        private NextPlayer next() {
            return NextPlayer.values()[(this.ordinal() + 1) % NextPlayer.values().length];
        }
    }

    private final Field[][] board;

    private final Position[] playerPositions;

    private GameState gameState;

    private NextPlayer nextPlayer;

    private final Logger logger = LoggerFactory.getLogger(BoardManager.class);

    /**
     * Creates a {@code BoardManager} object that is responsible for a single match.
     */
    public BoardManager() {
        logger.trace("Initializing game board");

        board = new Field[6][8];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = Field.EMPTY;
            }
        }
        board[0][0] = Field.PLAYER1;
        board[5][7] = Field.PLAYER2;

        playerPositions = new Position[]{new Position(0, 0), new Position(5, 7)};
        nextPlayer = NextPlayer.PLAYER1;
        gameState = GameState.RUNNING;
    }

    /**
     * {@return the current {@code Position} of each player}
     */
    public Position[] getPlayerPositions() {
        return playerPositions;
    }

    /**
     * {@return the next player to move}
     */
    public NextPlayer getNextPlayer() {
        return nextPlayer;
    }

    /**
     * {@return the current {@code GameState}}
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * Returns a the two-dimensional {@code array} that represents the board.
     *
     * @return a 2D {@code array} of {@code Field} objects
     */
    public Field[][] getBoard() {
        return this.board;
    }

    /**
     * Handles the movements of the chess pieces.
     * <p>
     * If the game is still running, it checks if any of the players are in losing position, then it handles the move.
     * If one of the players is next to move, it check whether the players current {@code Position} and the desired new {@code Position} are neighbours and
     * whether that desired {@code Position} is available to step on. The move happens only when these conditions are met.
     *
     * @param i the {@code int} that represents the row-index of the position to be moved to
     * @param j the {@code int} that represents the column-index of the position to be moved to
     */
    public void movePiece(int i, int j) {
        if (gameState.equals(GameState.RUNNING)) {
            try {
                Position pos = new Position(i, j);
                if (nextPlayer == NextPlayer.PLAYER1) {
                    performMove(pos, Field.PLAYER1);
                } else {
                    performMove(pos, Field.PLAYER2);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void performMove(Position destination, Field player) {
        int playerNumber = player.ordinal();
        Position playerPos = playerPositions[playerNumber];

        if (destination.isNeighbour(playerPos) && board[destination.row()][destination.col()].equals(Field.EMPTY)) {
            board[destination.row()][destination.col()] = player;
            board[playerPos.row()][playerPos.col()] = Field.UNAVAILABLE;
            playerPositions[playerNumber] = destination;
            nextPlayer = nextPlayer.next();
            logger.trace(String.format("Player %d moved to %s", playerNumber + 1, destination));

        }
    }

    /**
     * Checks whether any of the players are in losing position.
     * <p>
     * Each players current {@code Position}'s neighbours are checked. If none of them are empty, then that player is no longer able to move and loses the game.
     */
    public void checkIfGameIsOver() {
        GameState state = GameState.RUNNING;
        if (cannotMove(Field.PLAYER1)) {
            state = GameState.PLAYER2_WON;
            logger.trace("Game Over - Player 2 won");
        } else if (cannotMove(Field.PLAYER2)) {
            state = GameState.PLAYER1_WON;
            logger.trace("Game Over - Player 1 won");
        }
        this.gameState = state;
    }

    private boolean cannotMove(Field player) {
        List<Position> neighbours = playerPositions[player.ordinal()].getNeighbours();
        for (Position neighbour : neighbours) {
            if (board[neighbour.row()][neighbour.col()].equals(Field.EMPTY)) {
                return false;
            }
        }
        return true;
    }

    /**
     * If either of the players give up the game, this method sets the {@code gameState} attribute to the corresponding {@code GameState}.
     */
    public void giveUp() {
        if (nextPlayer == NextPlayer.PLAYER1) {
            gameState = GameState.PLAYER2_WON;
        } else {
            gameState = GameState.PLAYER1_WON;
        }
    }

    /**
     * Returns the {@code String} representation of the game's board.
     *
     * @return the {@code String} representation of the board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                sb.append(board[i][j].ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
