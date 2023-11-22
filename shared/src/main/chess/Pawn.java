package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents the Pawn piece in the chessgame.
 */
public class Pawn extends ChessPieceImp{

    /**
     * Class constructor
     *
     * @param color TeamColor object. Pawn's color.
     */
    public Pawn(ChessGame.TeamColor color) {
        super(color, PieceType.PAWN);
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Pawn's position.
     * @return Collection of ChessMove objects. List of moves pawn could make.
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        // Create collection of Chess Moves
        var validPawnMoves = new ArrayList<ChessMove>();
        var pawnColor = this.getTeamColor();
        if (pawnColor == ChessGame.TeamColor.WHITE) {
            pawnIsWhite(validPawnMoves, board, myPosition);
        }
        else {
            pawnIsBlack(validPawnMoves, board, myPosition);
        }
        return validPawnMoves;
    }

    /**
     * Helper function to return moves if pawn is white.
     *
     * @param validPawnMoves List of ChessMove objects. List of valid moves the pawn can make.
     * @param board ChessBoard object. Current version of chessboard.
     * @param myPosition ChessPosition object. Position of current pawn
     */
    public void pawnIsWhite(Collection<ChessMove> validPawnMoves, ChessBoard board, ChessPosition myPosition) {

        // Get the row number
        var pawnRowNumber = myPosition.getRow();
        var pawnColumnNumber = myPosition.getColumn();

        // Call function that checks if pawn can move one up.
        pawnCanMoveOneUpOrDown(validPawnMoves, board, myPosition, pawnRowNumber, 1, 7); // we want to move up one and don't want to go past row 7

        // Call function that checks if pawn can move forward 2.
        pawnCanMoveTwoUpOrDown(validPawnMoves, board, myPosition, pawnRowNumber, 1, 1); // we want to go up 1 or 2, and we want to start at row 1.

        // Call function that checks if pawn can attack.
        checkPawnAttackMoves(validPawnMoves, board, myPosition, pawnRowNumber, pawnColumnNumber, 1); // we want to go up.
    }

    /**
     *  Helper function to return moves if pawn is white.
     *
     * @param validPawnMoves List of ChessMove objects. List of valid moves the pawn can make.
     * @param board ChessBoard object. Current version of chessboard.
     * @param myPosition ChessPosition object. Position of current pawn
     */
    public void pawnIsBlack(Collection<ChessMove> validPawnMoves, ChessBoard board, ChessPosition myPosition) {

        // Get the row number
        var pawnRowNumber = myPosition.getRow();
        var pawnColumnNumber = myPosition.getColumn();

        // Call function that checks if pawn can move one up.
        pawnCanMoveOneUpOrDown(validPawnMoves, board, myPosition, pawnRowNumber, -1, 0); // we want to move up one and don't want to go past row 7

        // Call function that checks if pawn can move forward 2.
        pawnCanMoveTwoUpOrDown(validPawnMoves, board, myPosition, pawnRowNumber, -1, 6); // we want to go up 1 or 2, and we want to start at row 1.

        // Call function that checks if pawn can attack.
        checkPawnAttackMoves(validPawnMoves, board, myPosition, pawnRowNumber, pawnColumnNumber, -1); // we want to go up.
    }

    /**
     * Helper function to get list of moves pawn can make.
     *
     * @param validPawnMoves List of ChessMove objects. Valid moves pawn can make.
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Pawn's current position.
     * @param pawnRowNumber An integer than represents what row the pawn is on.
     * @param rowOffset An integer that represents whether a pawn will move up or down.
     * @param endRow An integer that shows the farthest a pawn can travel.
     */
    public void pawnCanMoveOneUpOrDown(Collection<ChessMove> validPawnMoves, ChessBoard board, ChessPosition myPosition, int pawnRowNumber, int rowOffset, int endRow) {
        // This function checks if a pawn can move one forward.
        var valueOfOneRowUp = pawnRowNumber+rowOffset;
        var upOneRow = new ChessPositionImp(valueOfOneRowUp, myPosition.getColumn());
        if (board.getPiece(upOneRow) == null) {
            // Check if we move into the top row for promotion purposes
            if (valueOfOneRowUp == endRow) {
                validPawnMoves.add(new ChessMoveImp(myPosition, upOneRow, PieceType.QUEEN));
                validPawnMoves.add(new ChessMoveImp(myPosition, upOneRow, PieceType.ROOK));
                validPawnMoves.add(new ChessMoveImp(myPosition, upOneRow, PieceType.KNIGHT));
                validPawnMoves.add(new ChessMoveImp(myPosition, upOneRow, PieceType.BISHOP));
            }
            else {
                validPawnMoves.add(new ChessMoveImp(myPosition, upOneRow));
            }
        }
    }

    /**
     * Helper function to get list of moves pawn can make.
     *
     * @param validPawnMoves List of ChessMove objects. Valid moves pawn can make.
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Pawn's current position.
     * @param pawnRowNumber An integer than represents what row the pawn is on.
     * @param rowOffset An integer that represents whether a pawn will move up or down.
     * @param pawnStartRow An integer that shows whether the pawn is at its starting row.
     */
    public void pawnCanMoveTwoUpOrDown(Collection<ChessMove> validPawnMoves, ChessBoard board, ChessPosition myPosition, int pawnRowNumber, int rowOffset, int pawnStartRow) {
        // This function checks if a pawn can move forward two.

        // If we aren't on the second row then we can't use this rule.
        if (pawnRowNumber == pawnStartRow) { // should be 1 or 6
            var valueOfOneRowUp = pawnRowNumber+rowOffset;
            var upOneRow = new ChessPositionImp(valueOfOneRowUp, myPosition.getColumn());
            if (board.getPiece(upOneRow) == null) {
                var upTwoRows = new ChessPositionImp(pawnRowNumber + 2*rowOffset, myPosition.getColumn());
                if (board.getPiece(upTwoRows) == null) {
                    validPawnMoves.add(new ChessMoveImp(myPosition, upTwoRows));
                }
            }
        }
    }

    /**
     * Helper function to get list of moves pawn can make.
     *
     * @param validPawnMoves List of ChessMove objects. Valid moves pawn can make.
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Pawn's current position.
     * @param pawnRowNumber An integer than represents what row the pawn is on.
     * @param pawnColumnNumber An integer that represents the column the pawn is currently in.
     * @param rowOffset An integer that represents whether a pawn moves up or down the board.
     */
    public void checkPawnAttackMoves(Collection<ChessMove> validPawnMoves, ChessBoard board, ChessPosition myPosition, int pawnRowNumber, int pawnColumnNumber, int rowOffset) {

        // If pawn on leftmost side of board, can it attack to the upper right?
        if (pawnColumnNumber == 0) {
            var newRowNum = pawnRowNumber + rowOffset;
            var newColNum = pawnColumnNumber + 1;
            var upperDiagPos = new ChessPositionImp(newRowNum, newColNum);
            checkPawnAttackMove(validPawnMoves, board, myPosition, newRowNum, upperDiagPos);
        }

        // If pawn on the rightmost side of board, can it attack to the upper left?
        else if (pawnColumnNumber == 7) {
            var newRowNum = pawnRowNumber + rowOffset;
            var newColNum = pawnColumnNumber - 1;
            var upperDiagPos = new ChessPositionImp(newRowNum, newColNum);
            checkPawnAttackMove(validPawnMoves, board, myPosition, newRowNum, upperDiagPos);
        }

        // If pawn not on right or left part of the board
        else {
            var newRowNum = pawnRowNumber + rowOffset;
            var leftColNum = pawnColumnNumber - 1;
            var rightColNum = pawnColumnNumber + 1;

            var upperLeftPos = new ChessPositionImp(newRowNum, leftColNum);
            var upperRightPos = new ChessPositionImp(newRowNum, rightColNum);
            checkPawnAttackMove(validPawnMoves, board, myPosition, newRowNum, upperLeftPos);
            checkPawnAttackMove(validPawnMoves, board, myPosition, newRowNum, upperRightPos);
        }
    }

    /**
     * Helper function to get list of moves pawn can make.
     *
     * @param validPawnMoves List of ChessMove objects. Valid moves pawn can make.
     * @param board ChessBoard object. Current game board.
     * @param myPosition ChessPosition object. Pawn's current position.
     * @param newRowNum An integer that represents the new row a pawn will move to.
     * @param upperDiagPos An integer that represents a position diagonal to a pawn.
     */
    private void checkPawnAttackMove(Collection<ChessMove> validPawnMoves, ChessBoard board, ChessPosition myPosition, int newRowNum, ChessPositionImp upperDiagPos) {
        if (board.getPiece(upperDiagPos) != null) {
            if (board.getPiece(upperDiagPos).getTeamColor() != this.getTeamColor()) {
                if ((newRowNum == 7) || (newRowNum == 0)) {
                    validPawnMoves.add(new ChessMoveImp(myPosition, upperDiagPos, PieceType.QUEEN));
                    validPawnMoves.add(new ChessMoveImp(myPosition, upperDiagPos, PieceType.ROOK));
                    validPawnMoves.add(new ChessMoveImp(myPosition, upperDiagPos, PieceType.KNIGHT));
                    validPawnMoves.add(new ChessMoveImp(myPosition, upperDiagPos, PieceType.BISHOP));
                }
                else {
                    validPawnMoves.add(new ChessMoveImp(myPosition, upperDiagPos));
                }
            }
        }
    }
}
