import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.*;

public class ChessBoardImp implements ChessBoard {
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */

    private ChessPiece[][] chessBoard = new ChessPiece[8][8];

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        var row = position.getRow();
        var column = position.getColumn();
        chessBoard[row][column] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    @Override
    public ChessPiece getPiece(ChessPosition position) {
        var row = position.getRow();
        var column = position.getColumn();
        return chessBoard[row][column];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    @Override
    public void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 0; j++) {
                if (i == 0) {

                }
            }
        }
    }
}
