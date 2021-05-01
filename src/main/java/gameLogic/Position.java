package gameLogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Position {
    int row;
    int col;

    Logger logger = LoggerFactory.getLogger(Position.class);

    public Position(int row, int col) {
        if (row < 0 || row > 5 || col < 0 || col > 7) {
            throw new IllegalArgumentException("No such position on the board");
        }else{
            this.row = row;
            this.col = col;
        }
    }

    public boolean isNeighbour(Position that){
        return Math.abs(this.row - that.row) <= 1 && Math.abs(this.col - that.col) <= 1;
    }

    public List<Position> getNeighbours(){
        List<Position> result = new ArrayList<Position>();
            for (int i = 0; i<6; i++){
                for (int j = 0; j<8; j++){
                    Position pos = new Position(i, j);
                    if (this.isNeighbour(pos)){
                        result.add(pos);
                    }
                }
            }

        return result;
    }

    public int row(){
        return this.row;
    }
    public int col(){
        return this.col;
    }

    public String toString(){
        return ("Position(" + row + ", " + col + ")");
    }
}
