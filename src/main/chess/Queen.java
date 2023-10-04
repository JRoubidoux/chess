package chess;

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

        return null;
    }
}
