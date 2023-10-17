package chess;

import chess.ChessPosition;

import java.util.Objects;

/**
 * This class represents a given position on the chessboard during a chess game.
 */
public class ChessPositionImp implements ChessPosition {

    private int row = 0;
    private int column = 0;

    /**
     * Class constructor
     *
     * @param inputRow An integer representing the row on the board
     * @param inputColumn An integer representing the column on the board
     */
    public ChessPositionImp(int inputRow, int inputColumn) {
        row = inputRow;
        column = inputColumn;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    /**
     * Equals method for ChessPositionImp
     *
     * @param o Should be a ChessPositionImp
     * @return Boolean value. True is the objects are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImp that = (ChessPositionImp) o;
        return row == that.row && column == that.column;
    }

    /**
     * Function that returns a hashcode for a ChessPositionImp object
     *
     * @return An integer
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
