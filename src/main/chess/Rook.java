package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Rook extends ChessPieceImp {
    public Rook(ChessGame.TeamColor color) {
        super(color, PieceType.ROOK);
    }

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
