package chess;

import chess.ChessPosition;

import java.util.Objects;

public class ChessPositionImp implements ChessPosition {
    /**
     * Variables for this class and constructor for the class as well
     * **/

    private int row = 0;
    private int column = 0;

    /**
     *
     * @param inputRow
     * @param inputColumn
     */
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

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImp that = (ChessPositionImp) o;
        return row == that.row && column == that.column;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
