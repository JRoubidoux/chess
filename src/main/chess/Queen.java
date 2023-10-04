package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Queen extends ChessPieceImp{
    public Queen(ChessGame.TeamColor color) {
        super(color, PieceType.QUEEN);
    }

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

    public void validBishopMoves(Collection<ChessMove> validPawnMoves, ChessBoard board, ChessPosition myPosition) {
        var rowNum = myPosition.getRow();
        var colNum = myPosition.getColumn();
        // check upper right
        var range = Math.min(7-rowNum, 7-colNum);
        checkDiagonal(validPawnMoves, board, myPosition, rowNum, colNum, range, 1, 1);
        // check upper left
        range = Math.min(7-rowNum, colNum);
        checkDiagonal(validPawnMoves, board, myPosition, rowNum, colNum, range, 1, -1);
        // check bottom left
        range = Math.min(rowNum, colNum);
        checkDiagonal(validPawnMoves, board, myPosition, rowNum, colNum, range, -1, -1);
        range = Math.min(rowNum, 7-colNum);
        checkDiagonal(validPawnMoves, board, myPosition, rowNum, colNum, range, -1, 1);
    }

    public void checkDiagonal(Collection<ChessMove> validPawnMoves, ChessBoard board, ChessPosition myPosition, int rowNum, int colNum, int rangeToCheck, int rowOffset, int colOffset) {
        // Check to see how far we can go diagonally
        var newRowNum = rowNum;
        var newColNum = colNum;

        for (int i = 0; i < rangeToCheck; i++) {
            newRowNum += rowOffset;
            newColNum += colOffset;
            var newPos = new ChessPositionImp(newRowNum, newColNum);
            if (board.getPiece(newPos) != null) {
                if (board.getPiece(newPos).getTeamColor() != this.getTeamColor()) {
                    validPawnMoves.add(new ChessMoveImp(myPosition, newPos));
                    break;
                }
                else {
                    break;
                }
            }
            else {
                validPawnMoves.add(new ChessMoveImp(myPosition, newPos));
            }

        }

    }
}
