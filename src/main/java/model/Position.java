package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Record class for wrapping the coordinates of a 2D position.
 *
 * @param row the row-index of the position, must be between 0 and 5
 * @param col the column-index of the position, must be between 0 and 7
 */
public record Position(int row, int col) {
    /**
     * Creates a {@code Position} object.
     * The {@code Position} will be determined by two {@code int} coordinates. These coordinates must be in the limit of a 6x8 sized board.
     *
     * @param row the row-index of the position, must be between 0 and 5
     * @param col the column-index of the position, must be between 0 and 7
     */
    public Position(int row, int col) {
        if (row < 0 || row > 5 || col < 0 || col > 7) {
            throw new IllegalArgumentException("No such position on the board");
        } else {
            this.row = row;
            this.col = col;
        }
    }

    /**
     * Checks if two {@code Position} objects are neighbours on the board.
     * In this case, being neighbours means that a king chess piece is able to move from one position to the other one.
     *
     * @param that {@code Position} object that might be a neighbour of
     * @return {@code true} if the two {@code Position} objects are neighbours and {@code false} otherwise
     */
    public boolean isNeighbour(Position that) {
        return Math.abs(this.row - that.row) <= 1 && Math.abs(this.col - that.col) <= 1
                && !(this.row == that.row() && this.col == that.col());
    }

    /**
     * Returns the neighbours of a {@code Position} object.
     *
     * @return the {@code List} of neighbouring {@code Position} objects
     */
    public List<Position> getNeighbours() {
        List<Position> result = new ArrayList<Position>();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                Position pos = new Position(i, j);
                if (this.isNeighbour(pos)) {
                    result.add(pos);
                }
            }
        }
        return result;
    }
}
