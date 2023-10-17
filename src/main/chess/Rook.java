package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class that represents the Rook piece in chess.
 */
public class Rook extends ChessPieceImp {

    /**
     * Class constructor
     *
     * @param color TeamColor object. Rook's color.
     */
    public Rook(ChessGame.TeamColor color) {
        super(color, PieceType.ROOK);
    }

    /**
     * Function that get the valid moves a Rook can make.
     *
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Rook's position.
     * @return List of ChessMove objects. Valid moves that the Rook can make.
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // Rook can go right, left, up, down, not past one of its own, and not past
        // an enemy, though it can capture an enemy.
        var validRookMovesList = new ArrayList<ChessMove>();

        var rowNum = myPosition.getRow();
        var colNum = myPosition.getColumn();
        validRookMoves(validRookMovesList, board, myPosition, rowNum, colNum);

        return validRookMovesList;
    }

    /**
     * Helper function to get the valid moves a Rook can make.
     *
     * @param validRookMovesList List of ChessMove objects. Valid moves a Rook can make.
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Rook's position.
     * @param rowNum An integer representing the row the Rook is on.
     * @param colNum An integer representing the column the Rook is on.
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
     * Helper function to get the valid moves a Rook can make.
     *
     * @param validRookMovesList List of ChessMove objects. Valid moves a Rook can make.
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Rook's position.
     * @param rowNum An integer representing the row the Rook is on.
     * @param colNum An integer representing the column the Rook is on.
     * @param range An integer that lets us know how far the Rook is from the edge of the board.
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
}
