package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class that represents the Queen piece in chess.
 */
public class Queen extends ChessPieceImp{

    /**
     * Class constructor
     *
     * @param color TeamColor object. Queen's color.
     */
    public Queen(ChessGame.TeamColor color) {
        super(color, PieceType.QUEEN);
    }

    /**
     * Function that finds the valid moves a Queen piece can make.
     *
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Queen's position.
     * @return List of ChessMove objects. Valid moves Queen can make.
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // Queen can go right, left, up, down, not past one of its own, and not past
        // a member of its own team, or past an enemy, but it can capture enemy.
        // Queen can also do this on the diagonals.
        var validQueenMovesList = new ArrayList<ChessMove>();

        var rowNum = myPosition.getRow();
        var colNum = myPosition.getColumn();
        validRookMoves(validQueenMovesList, board, myPosition, rowNum, colNum);
        validBishopMoves(validQueenMovesList, board, myPosition);

        return validQueenMovesList;
    }

    /**
     * Helper function to get the valid moves a Queen can make.
     *
     * @param validRookMovesList List of ChessMove objects. Valid moves a Queen can make.
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Queen's position.
     * @param rowNum An integer representing the row the Queen is on.
     * @param colNum An integer representing the column the Queen is on.
     */
    public void validRookMoves(Collection<ChessMove> validRookMovesList, ChessBoard board, ChessPosition myPosition, int rowNum, int colNum) {
        // Check valid moves right of the rook
        rookMoves(validRookMovesList, board, myPosition, rowNum, colNum, 7-colNum, 1, 0);
        // Check valid moves left of the rook
        rookMoves(validRookMovesList, board, myPosition, rowNum, colNum, colNum, -1, 0);
        // Check valid moves above the rook
        rookMoves(validRookMovesList, board, myPosition, rowNum, colNum, 7-rowNum, 0, 1);
        // Check valid moves below the rook
        rookMoves(validRookMovesList, board, myPosition, rowNum, colNum, rowNum, 0, -1);

    }

    /**
     * Helper function to get the valid moves a Queen can make.
     *
     * @param validRookMovesList List of ChessMove objects. Valid moves a Queen can make.
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Queen's position.
     * @param rowNum An integer representing the row the Queen is on.
     * @param colNum An integer representing the column the Queen is on.
     * @param range An integer that lets us know how far the Queen is from the edge of the board.
     * @param colChanger An integer the represents whether we are going, up, down, or staying on the same row.
     * @param rowChanger An integer the represents whether we are going, up, down, or staying on the same column.
     */
    public void rookMoves(Collection<ChessMove> validRookMovesList, ChessBoard board, ChessPosition myPosition, int rowNum, int colNum, int range, int colChanger, int rowChanger) {
        var newRow = rowNum;
        var newCol = colNum;

        for (int i = 0; i < range; i++) {
            newRow += rowChanger;
            newCol += colChanger;
            var newPos = new ChessPositionImp(newRow, newCol);
            if (board.getPiece(newPos) == null) {
                validRookMovesList.add(new ChessMoveImp(myPosition, newPos));
            } else if (board.getPiece(newPos).getTeamColor() != this.getTeamColor()) {
                validRookMovesList.add(new ChessMoveImp(myPosition, newPos));
                break;
            } else {
                break;
            }
        }
    }

    /**
     * This is the same functions that are in the Bishop class.
     *
     * @param validBishopMovesList List of moves that Queen can make
     * @param board The current chessboard
     * @param myPosition The current position of the Queen
     */
    public void validBishopMoves(Collection<ChessMove> validBishopMovesList, ChessBoard board, ChessPosition myPosition) {
        var rowNum = myPosition.getRow();
        var colNum = myPosition.getColumn();
        // check upper right
        var range = Math.min(7-rowNum, 7-colNum);
        checkDiagonal(validBishopMovesList, board, myPosition, rowNum, colNum, range, 1, 1);
        // check upper left
        range = Math.min(7-rowNum, colNum);
        checkDiagonal(validBishopMovesList, board, myPosition, rowNum, colNum, range, 1, -1);
        // check bottom left
        range = Math.min(rowNum, colNum);
        checkDiagonal(validBishopMovesList, board, myPosition, rowNum, colNum, range, -1, -1);
        range = Math.min(rowNum, 7-colNum);
        checkDiagonal(validBishopMovesList, board, myPosition, rowNum, colNum, range, -1, 1);
    }

    /**
     * This is a helper function for the ValidBishopMoves function
     *
     * @param validBishopMovesList List of valid Queen moves.
     * @param board Current Chessboard.
     * @param myPosition The current position of the piece.
     * @param rowNum Integer that represents the row number of the piece.
     * @param colNum Integer that represents the column number of the piece.
     * @param rangeToCheck How many iterations our for loop goes through.
     * @param rowOffset Whether we are moving right or left.
     * @param colOffset Whether we are moving up or down.
     */
    public void checkDiagonal(Collection<ChessMove> validBishopMovesList, ChessBoard board, ChessPosition myPosition, int rowNum, int colNum, int rangeToCheck, int rowOffset, int colOffset) {
        // Check to see how far we can go diagonally
        var newRowNum = rowNum;
        var newColNum = colNum;

        for (int i = 0; i < rangeToCheck; i++) {
            newRowNum += rowOffset;
            newColNum += colOffset;
            var newPos = new ChessPositionImp(newRowNum, newColNum);
            if (board.getPiece(newPos) != null) {
                if (board.getPiece(newPos).getTeamColor() != this.getTeamColor()) {
                    validBishopMovesList.add(new ChessMoveImp(myPosition, newPos));
                    break;
                }
                else {
                    break;
                }
            }
            else {
                validBishopMovesList.add(new ChessMoveImp(myPosition, newPos));
            }
        }
    }
}
