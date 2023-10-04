import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class ChessMoveImp implements ChessMove {


    private ChessPosition startPos = null;
    private ChessPosition endPos = null;
    private ChessPiece.PieceType promoType;

    private boolean validPromotion;

    public ChessMoveImp(ChessPosition start, ChessPosition end) {
        startPos = start;
        endPos = end;
        validPromotion = false;
    }

    public ChessMoveImp(ChessPosition start, ChessPosition end, boolean promotion) {
        startPos = start;
        endPos = end;
        validPromotion = promotion;
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

    public boolean getValidPromotion() { return validPromotion;}

    public void setPromotionPiece(ChessPiece.PieceType promotionPiece) {
        promoType = promotionPiece;
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
}
