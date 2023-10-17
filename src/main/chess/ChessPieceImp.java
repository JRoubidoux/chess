package chess;

import chess.*;

import java.util.Collection;

/**
 * This class represents a ChessPiece in the chess game.
 */
public class ChessPieceImp implements ChessPiece {

    private ChessGame.TeamColor currentTeam;
    private ChessPiece.PieceType typePiece;

    /**
     * class constructor
     *
     * @param color TeamColor object. Either black or white to represent teams.
     * @param type ChessPiece.Type object. There are 6 types of pieces, this is how we keep track of what it is.
     */
    public ChessPieceImp(ChessGame.TeamColor color, ChessPiece.PieceType type) {
        currentTeam = color;
        typePiece = type;
    }


    @Override
    public ChessGame.TeamColor getTeamColor() {
        return currentTeam;
    }


    @Override
    public PieceType getPieceType() {
        return typePiece;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @param board ChessBoardImp object. Current board for our chess game.
     * @param myPosition ChessPosition object. The position of the given piece.
     * @return Collection of ChessMove objects. This is the list of valid moves this piece can make.
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
