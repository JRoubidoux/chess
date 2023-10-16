package chess;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

/**
 *
 */
public class ChessMoveImp implements ChessMove {


    private ChessPosition startPos = null;
    private ChessPosition endPos = null;
    private ChessPiece.PieceType promoType;


    /**
     *
     * @param start
     * @param end
     */
    public ChessMoveImp(ChessPosition start, ChessPosition end) {
        startPos = start;
        endPos = end;
        promoType = null;
    }

    /**
     *
     * @param start
     * @param end
     * @param type
     */
    public ChessMoveImp(ChessPosition start, ChessPosition end, ChessPiece.PieceType type) {
        startPos = start;
        endPos = end;
        promoType = type;
    }


    /**
     * @return ChessPosition of starting location
     */
    @Override
    public ChessPosition getStartPosition() {
        return startPos;
    }

    /**
     * @return ChessPosition of ending location
     */
    @Override
    public ChessPosition getEndPosition() {
        return endPos;
    }


    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promoType;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImp that = (ChessMoveImp) o;
        return Objects.equals(startPos, that.startPos) && Objects.equals(endPos, that.endPos) && promoType == that.promoType;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(startPos, endPos, promoType);
    }
}
