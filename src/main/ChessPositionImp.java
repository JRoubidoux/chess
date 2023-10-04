import chess.ChessPosition;

public class ChessPositionImp implements ChessPosition {
    /**
     * Variables for this class and constructor for the class as well
     * **/

    private int row = 0;
    private int column = 0;
    public ChessPositionImp(int inputRow, int inputColumn) {
        row = inputRow;
        column = inputColumn;
    }


    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */

    @Override
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    @Override
    public int getColumn() {
        return column;
    }
}
