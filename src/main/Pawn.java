import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class Pawn extends ChessPieceImp{

    public Pawn(ChessGame.TeamColor color) {
        super(color, PieceType.PAWN);
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @param board
     * @param myPosition
     * @return Collection of valid moves
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // 1.) This is a pawn, else we wouldn't have accessed this method.
        // 2.) We have a layout of the chess board.
        // 3.) We have our current position.

        // a.) what is the color of this pawn?
        // b.) what is the position of this pawn.
        // c.) if color is x and position is y, then pawn can move forward two spaces, if there
        // isn't a piece there already.
        // c.1) If the pawn is of certain color and can end up in promotion location, say promotion is valid for this move
        // d.) or a pawn can move forward 1 if there isn't a piece in front of it.
        // e.) if there are pieces up 1 and right or left 1, pawn can attack and move there.

        // Create collection of Chess Moves
        var validPawnMoves = new ArrayList<ChessMove>();
        var pawnColor = this.getTeamColor();
        if (pawnColor == ChessGame.TeamColor.WHITE) {
            pawnIsWhite(validPawnMoves, pawnColor, board, myPosition);
        }
        else {

        }




        return validPawnMoves;
    }

    public void pawnIsWhite(Collection<ChessMove> validPawnMoves, ChessGame.TeamColor pawnColor, ChessBoard board, ChessPosition myPosition) {

        // Get the row number
        var pawnRowNumber = myPosition.getRow();
        var pawnColumnNumber = myPosition.getColumn();

        var upOneRow = new ChessPositionImp(pawnRowNumber+1, myPosition.getColumn());
        if (board.getPiece(upOneRow) == null) {
            validPawnMoves.add(new ChessMoveImp(myPosition, upOneRow));

            // See if pawn can move forward 2.
            if (pawnRowNumber == 1) {
                var upTwoRows = new ChessPositionImp(pawnRowNumber+2, myPosition.getColumn());
                if (board.getPiece(upTwoRows) == null) {
                    validPawnMoves.add(new ChessMoveImp(myPosition, upTwoRows));
                }
            }
        }
        if (pawnColumnNumber == 0) {

        }
    }
}
