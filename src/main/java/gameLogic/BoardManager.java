package gameLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BoardManager {

    public Field[][] board;

    Position player1Position;
    Position player2Position;

    public boolean player1IsNext;

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
    }

    public void movePiece(int i, int j) {
        try {
            Position pos = new Position(i, j);

            if (player1IsNext && pos.isNeighbour(player1Position) && board[pos.row()][pos.col()].equals(Field.EMPTY)) {
                board[i][j] = Field.PLAYER1;
                board[player1Position.row()][player1Position.col()] = Field.UNAVAILABLE;
                player1Position = pos;
                player1IsNext = false;
            } else if (pos.isNeighbour(player2Position) && board[pos.row()][pos.col()].equals(Field.EMPTY)) {
                board[i][j] = Field.PLAYER2;
                board[player2Position.row()][player2Position.col()] = Field.UNAVAILABLE;
                player2Position = pos;
                player1IsNext = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

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

    public static void main(String[] args) throws IOException {
        BoardManager game = new BoardManager();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println(game.toString());
            String input = in.readLine();
            game.movePiece(Integer.parseInt(String.valueOf(input.charAt(0))),Integer.parseInt(String.valueOf(input.charAt(2))));
        }
    }
}
