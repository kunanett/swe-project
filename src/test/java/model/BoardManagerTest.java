package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardManagerTest {

    BoardManager testGame;

    @BeforeEach
    void init() {
        testGame = new BoardManager();
    }

    void assertBoard(BoardManager.NextPlayer expectedNextPlayer, Position expectedPlayer1Pos, Position expectedPlayer2Pos) {
        assertAll(() -> assertEquals(expectedNextPlayer, testGame.getNextPlayer()),
                () -> assertEquals(expectedPlayer1Pos, testGame.getPlayerPositions()[0]),
                () -> assertEquals(expectedPlayer2Pos, testGame.getPlayerPositions()[1])
        );
    }

    void assertField(Field expected, Position actual) {
        assertEquals(expected, testGame.getBoard()[actual.row()][actual.col()]);
    }

    @Test
    void movePiece() {
        //player1 moves to Position(0, 1)
        testGame.movePiece(0, 1);
        assertBoard(BoardManager.NextPlayer.PLAYER2, new Position(0, 1), new Position(5, 7));
        assertField(Field.UNAVAILABLE, new Position(0, 0));

        //player2 moves to Position(4, 7)
        testGame.movePiece(4, 7);
        assertBoard(BoardManager.NextPlayer.PLAYER1, new Position(0, 1), new Position(4, 7));
        assertField(Field.UNAVAILABLE, new Position(5, 7));

        //player1 tries to move back to an unavailable field, should remain in place
        testGame.movePiece(0, 0);
        assertBoard(BoardManager.NextPlayer.PLAYER1, new Position(0, 1), new Position(4, 7));
        assertField(Field.PLAYER1, new Position(0, 1));

        //player1 moves to Position(1, 1)
        testGame.movePiece(1, 1);
        assertBoard(BoardManager.NextPlayer.PLAYER2, new Position(1, 1), new Position(4, 7));
        assertField(Field.PLAYER1, new Position(1, 1));
        assertField(Field.UNAVAILABLE, new Position(0, 1));

        //player2 tries to move on a non-existent field
        testGame.movePiece(-5, 10);
        assertBoard(BoardManager.NextPlayer.PLAYER2, new Position(1, 1), new Position(4, 7));
        assertField(Field.PLAYER1, new Position(1, 1));
        assertField(Field.UNAVAILABLE, new Position(0, 1));


    }

    @Test
    void checkIfGameIsOver() {
        testGame.movePiece(0, 1);
        testGame.movePiece(4, 7);
        testGame.movePiece(1, 1);
        testGame.movePiece(3, 7);
        testGame.movePiece(2, 1);
        testGame.movePiece(2, 7);
        testGame.movePiece(3, 1);
        testGame.movePiece(1, 7);
        testGame.movePiece(3, 0);
        testGame.movePiece(0, 7);
        testGame.movePiece(2, 0);
        testGame.checkIfGameIsOver();
        assertEquals(GameState.RUNNING, testGame.getGameState());
        testGame.movePiece(0, 6);
        testGame.movePiece(1, 0);
        testGame.checkIfGameIsOver();
        assertEquals(GameState.PLAYER2_WON, testGame.getGameState());
    }

    @Test
    void giveUp() {
        testGame.giveUp();
        assertEquals(GameState.PLAYER2_WON, testGame.getGameState());

        testGame = new BoardManager();
        testGame.movePiece(1, 1);
        testGame.giveUp();
        assertEquals(GameState.PLAYER1_WON, testGame.getGameState());
    }
}