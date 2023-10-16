package chess;

import chess.*;

import java.util.Collection;

public class ChessPieceImp implements ChessPiece {
    /**
     * @return Which team this chess piece belongs to
     */
    private ChessGame.TeamColor currentTeam;
    private ChessPiece.PieceType typePiece;

    /**
     *
     * @param color
     * @param type
     */
    public ChessPieceImp(ChessGame.TeamColor color, ChessPiece.PieceType type) {
        currentTeam = color;
        typePiece = type;
    }

    /**
     *
     * @return
     */
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return currentTeam;
    }

    /**
     * @return which type of chess piece this piece is
     */
    @Override
    public PieceType getPieceType() {
        return typePiece;
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
        return null;
    }
}
