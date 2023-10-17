package chess;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

/**
 * This class represents a ChessMove that can be made during the game
 */
public class ChessMoveImp implements ChessMove {

    private ChessPosition startPos = null;
    private ChessPosition endPos = null;
    private ChessPiece.PieceType promoType;


    /**
     * Constructor for the class. Takes in two parameters:
     *
     * @param start A ChessPosition object. The start position of a move.
     * @param end A ChessPosition object. The end position of a move.
     */
    public ChessMoveImp(ChessPosition start, ChessPosition end) {
        startPos = start;
        endPos = end;
        promoType = null;
    }

    /**
     * Other constructor for the ChessMove. This accepts a promotion piece as a parameter for when a Pawn is
     * up for promotion
     *
     * @param start ChessPosition object. The start position of a move.
     * @param end ChessPosition object. The end position of a move.
     * @param type ChessPiece.Type object. A chess piece to promote pawn to.
     */
    public ChessMoveImp(ChessPosition start, ChessPosition end, ChessPiece.PieceType type) {
        startPos = start;
        endPos = end;
        promoType = type;
    }


    @Override
    public ChessPosition getStartPosition() {
        return startPos;
    }


    @Override
    public ChessPosition getEndPosition() {
        return endPos;
    }


    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promoType;
    }

    /**
     * Equals method for the ChessMove
     *
     * @param o Should be a ChessMove object
     * @return Boolean value, whether the objects are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImp that = (ChessMoveImp) o;
        return Objects.equals(startPos, that.startPos) && Objects.equals(endPos, that.endPos) && promoType == that.promoType;
    }

    /**
     * Function that returns a unique hashcode for the given object
     *
     * @return An integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(startPos, endPos, promoType);
    }
}
