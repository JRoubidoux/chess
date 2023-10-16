package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Knight extends ChessPieceImp{

    /**
     *
     * @param color
     */
    public Knight(ChessGame.TeamColor color) {
        super(color, PieceType.KNIGHT);
    }

    /**
     *
     * @param board
     * @param myPosition
     * @return
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
     *
     * @param validKnightMovesList
     * @param board
     * @param myPosition
     * @param rowChanger
     * @param colChanger
     */
    public void validKnightMoves(Collection<ChessMove> validKnightMovesList,ChessBoard board, ChessPosition myPosition, int rowChanger, int colChanger){
        var rowNum = myPosition.getRow();
        var colNum = myPosition.getColumn();

        var newRow = rowNum + rowChanger;
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
