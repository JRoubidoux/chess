package chess;

import java.util.ArrayList;
import java.util.Collection;

public class King extends ChessPieceImp {

    public King(ChessGame.TeamColor color) {
        super(color, PieceType.KING);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // The king can move 1 in any direction. It can capture enemy pieces.

        var validKingMovesList = new ArrayList<ChessMove>();
        var rowNum = myPosition.getRow();
        var colNum = myPosition.getColumn();

        // King goes up
        validKingMoves(validKingMovesList, board, myPosition, rowNum+1, colNum);
        // King goes up and right
        validKingMoves(validKingMovesList, board, myPosition, rowNum+1, colNum+1);
        // King goes right
        validKingMoves(validKingMovesList, board, myPosition, rowNum, colNum+1);
        // King goes down and right
        validKingMoves(validKingMovesList, board, myPosition, rowNum-1, colNum+1);
        // King goes down
        validKingMoves(validKingMovesList, board, myPosition, rowNum-1, colNum);
        // King goes down and left
        validKingMoves(validKingMovesList, board, myPosition, rowNum-1, colNum-1);
        // King goes left
        validKingMoves(validKingMovesList, board, myPosition, rowNum, colNum-1);
        // King goes up and left
        validKingMoves(validKingMovesList, board, myPosition, rowNum+1, colNum-1);

        return validKingMovesList;
    }

    public void validKingMoves(Collection<ChessMove> validKingMovesList, ChessBoard board, ChessPosition myPosition, int newRow, int newCol) {


        if (((newRow < 8) && (newRow >= 0)) && ((newCol < 8) && (newCol >= 0))) {
            var newPos = new ChessPositionImp(newRow, newCol);
            if (board.getPiece(newPos) == null) {
                validKingMovesList.add(new ChessMoveImp(myPosition, newPos));
            }
            else if (board.getPiece(newPos).getTeamColor() != this.getTeamColor()) {
                validKingMovesList.add(new ChessMoveImp(myPosition, newPos));
            }
        }
    }

}
