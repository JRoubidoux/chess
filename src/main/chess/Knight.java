package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents the Knight piece in the chess game
 */
public class Knight extends ChessPieceImp{

    /**
     * Class Constructor
     *
     * @param color TeamColor object. Knight's color.
     */
    public Knight(ChessGame.TeamColor color) {
        super(color, PieceType.KNIGHT);
    }

    /**
     * Function that returns the valid moves a piece can make.
     *
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Knight's position.
     * @return A list of ChessMove objects. Valid moves the Knight can make.
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // bishop moves diagonally on any diagonal, it cannot move past pieces of its own color nor
        // can it move past enemies that it will attack.


        var validKnightMovesList = new ArrayList<ChessMove>();

        // can knight go up one, right two
        validKnightMoves(validKnightMovesList, board, myPosition, 1, 2);
        // can knight go up one, left two
        validKnightMoves(validKnightMovesList, board, myPosition, 1, -2);
        // can knight go up two, right one
        validKnightMoves(validKnightMovesList, board, myPosition, 2, 1);
        // can knight go up two, left one
        validKnightMoves(validKnightMovesList, board, myPosition, 2, -1);
        // can knight go down one right two
        validKnightMoves(validKnightMovesList, board, myPosition, -1, 2);
        // can knight go down one left two
        validKnightMoves(validKnightMovesList, board, myPosition, -1, -2);
        // can knight go down two right one
        validKnightMoves(validKnightMovesList, board, myPosition, -2, 1);
        // can knight go down two left one
        validKnightMoves(validKnightMovesList, board, myPosition, -2, -1);

        return validKnightMovesList;
    }

    /**
     * Helper function to the pieceMoves function.
     *
     * @param validKnightMovesList List of ChessMove object. Valid moves the Knight can make.
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Knight's position.
     * @param rowChanger Integer. Represents rows where the knight could move to.
     * @param colChanger Integer. Represents columns where the knight could move to.
     */
    public void validKnightMoves(Collection<ChessMove> validKnightMovesList,ChessBoard board, ChessPosition myPosition, int rowChanger, int colChanger){
        var rowNum = myPosition.getRow();
        var colNum = myPosition.getColumn();

        var newRow = rowNum + rowChanger;
        // Knight must be found within the boundaries of the chess board.
        if ((newRow < 8) && (newRow) >= 0) {
            var newCol = colNum + colChanger;
            if ((newCol < 8) && (newCol >=0)) {
                var newPos = new ChessPositionImp(newRow, newCol);
                if (board.getPiece(newPos) == null) {
                    validKnightMovesList.add(new ChessMoveImp(myPosition, newPos));
                }
                else {
                    if (board.getPiece(newPos).getTeamColor() != this.getTeamColor()) {
                        validKnightMovesList.add(new ChessMoveImp(myPosition, newPos));
                    }
                }
            }
        }
    }
}
