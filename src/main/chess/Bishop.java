package chess;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Bishop Class that represents the Bishop Piece in a chess game.
 */
public class Bishop extends ChessPieceImp{

    /**
     * Constructor that instantiates the piece type and the color of the piece.
     *
     * @param color This is the color of the piece.
     */
    public Bishop(ChessGame.TeamColor color) {
        super(color, PieceType.BISHOP);
    }

    /**
     * This function returns the moves the bishop can make on the chess board.
     *
     * @param board The current chessBoard
     * @param myPosition The current position of the Bishop piece
     * @return A list of moves the Bishop could make.
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        // Instantiate a list of valid moves.
        var validBishopMovesList = new ArrayList<ChessMove>();

        // Add valid moves to the list.
        validBishopMoves(validBishopMovesList, board, myPosition);


        return validBishopMovesList;
    }

    /**
     * This is a helper function to the Piece moves function
     *
     * @param validBishopMovesList List of moves that Bishop can make
     * @param board The current chessboard
     * @param myPosition The current position of the Bishop
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
     * @param validBishopMovesList List of valid Bishop moves.
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
