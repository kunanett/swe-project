package gameLogic;

public class Position {
    int row;
    int col;

    public Position(int row, int col) {
        if (row < 0 || row > 5 || col < 0 || col > 8) {
            throw new IllegalArgumentException("No such position on the board");
        }else{
            this.row = row;
            this.col = col;
        }
    }

    public boolean isNeighbour(Position that){
        return Math.abs(this.row - that.row) <= 1 && Math.abs(this.col - that.col) <= 1;
    }

    public int row(){
        return this.row;
    }
    public int col(){
        return this.col;
    }
}
