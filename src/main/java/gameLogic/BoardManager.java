package gameLogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Class that is responsible for the gameplay's logic.
 */
public class BoardManager {

    private final Field[][] board;

    private Position player1Position;
    private Position player2Position;

    private GameState gameState;

    private boolean player1IsNext;

    private final Logger logger = LoggerFactory.getLogger(BoardManager.class);

    /**
     * Creates a {@code BoardManager} object that is responsible for a single match.
     */
    public BoardManager() {
        board = new Field[6][8];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = Field.EMPTY;
            }
        }
        board[0][0] = Field.PLAYER1;
        board[5][7] = Field.PLAYER2;

        player1Position = new Position(0, 0);
        player2Position = new Position(5, 7);
        player1IsNext = true;
        gameState = GameState.RUNNING;

        logger.trace("Initializing game board");
    }

    /**
     * Returns the current state of the game.
     *
     * @return a {@code GameState}
     */
    public GameState getGameState(){
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
     *
     * If the game is still running, it checks if any of the players are in losing position, then it handles the move.
     * If one of the players is next to move, it check whether the players current {@code Position} and the desired new {@code Position} are neighbours and
     * whether that desired {@code Position} is available to step on. The move happens only when these conditions are met.
     *
     * @param i the {@code int} that represents the row-index of the position to be moved to
     * @param j the {@code int} that represents the column-index of the position to be moved to
     */
    public void movePiece(int i, int j) {
        if (gameState.equals(GameState.RUNNING)) {
            checkIfGameIsOver();
            try {
                Position pos = new Position(i, j);

                if (player1IsNext && pos.isNeighbour(player1Position) && board[pos.row()][pos.col()].equals(Field.EMPTY)) {
                    board[i][j] = Field.PLAYER1;
                    board[player1Position.row()][player1Position.col()] = Field.UNAVAILABLE;
                    player1Position = pos;
                    player1IsNext = !player1IsNext;
                    logger.trace("Player 1 moved");
                } else if (!player1IsNext && pos.isNeighbour(player2Position) && board[pos.row()][pos.col()].equals(Field.EMPTY)) {
                    board[i][j] = Field.PLAYER2;
                    board[player2Position.row()][player2Position.col()] = Field.UNAVAILABLE;
                    player2Position = pos;
                    player1IsNext = !player1IsNext;
                    logger.trace("Player 2 moved");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Checks whether one of the players is in losing position.
     *
     * Each players current {@code Position}'s neighbours are checked. If none of them are empty, then that player is no longer able to move and loses the game.
     */
    public void checkIfGameIsOver() {
        List<Position> neighbours = player1Position.getNeighbours();
        System.out.println(neighbours.toString());
        boolean gameIsOver = true;
        for (Position neighbour : neighbours) {
            if (board[neighbour.row()][neighbour.col()].equals(Field.EMPTY)) {
                gameIsOver = false;
            }
        }
        if (gameIsOver) {
            gameState = GameState.PLAYER2_WON;
            logger.trace("Game Over - Player 2 won");
        } else {
            neighbours = player2Position.getNeighbours();
            gameIsOver = true;
            for (Position neighbour : neighbours) {
                if (board[neighbour.row()][neighbour.col()].equals(Field.EMPTY)) {
                    gameIsOver = false;
                }
            }
            if (gameIsOver) {
                gameState = GameState.PLAYER1_WON;
                logger.trace("Game Over - Player 1 won");
            }
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
