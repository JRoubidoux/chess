package chess;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class ChessGameImp implements ChessGame {

    private ChessBoardImp chessBoard = new ChessBoardImp();
    private ChessGame.TeamColor currentTurn = TeamColor.WHITE;




    /**
     * @return Which team's turn it is
     */

    @Override
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    @Override
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var piece = chessBoard.getPiece(startPosition);

        if (piece == null) {
            return null;
        }
        else {
            var colorOfMovePiece = this.chessBoard.getPiece(startPosition).getTeamColor();
            var kingPos = findKing(colorOfMovePiece);
            //var kingPos = findKing(this.getTeamTurn());
            var listOfMovesThatCanAttackKing = piecesHaveKingInCheck(kingPos, colorOfMovePiece);
            if (listOfMovesThatCanAttackKing.size() > 1) {
                if (kingPos != startPosition) {
                    return new ArrayList<ChessMove>();
                }
                else {
                    var listOfKingMoves = placesKingCanMove(kingPos, colorOfMovePiece);
                    if (!listOfKingMoves.isEmpty()) {
                        return listOfKingMoves;
                    }
                    else {
                        return new ArrayList<ChessMove>();
                    }
                }
            }
            else if (listOfMovesThatCanAttackKing.size() == 1) {
                // Piece is King
                if (startPosition.equals(kingPos)) {
                    var listOfKingMoves = placesKingCanMove(kingPos, colorOfMovePiece);
                    if (listOfKingMoves.isEmpty()) {
                        return new ArrayList<ChessMove>();
                    }
                    else {
                        return listOfKingMoves;
                    }

                }
                // Piece isn't King
                else {
                    var piecesCanBlockOrAttack = memberCanBlockOrCaptureAttacker(listOfMovesThatCanAttackKing, colorOfMovePiece);
                    var listOfValidMoves = new ArrayList<ChessMove>();
                    for (ChessMove move: piecesCanBlockOrAttack) {
                        var newPos = move.getStartPosition();
                        if (newPos.equals(startPosition)) {
                            listOfValidMoves.add(move);
                        }
                    }
                    if (listOfValidMoves.isEmpty()) {
                        return new ArrayList<ChessMove>();
                    }
                    else {
                        return listOfValidMoves;
                    }
                }

            }
            else {
                var listOfAllPieceMoves = this.chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
                var finalListValidMoves = new ArrayList<ChessMove>();
                for (ChessMove move: listOfAllPieceMoves) {
                    if (!moveCompromisesSafety(move, colorOfMovePiece)) {
                        finalListValidMoves.add(move);
                    }
                }
                if (finalListValidMoves.isEmpty()) {
                    return new ArrayList<ChessMove>();
                }
                else {
                    return finalListValidMoves;
                }
            }
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var startPos = move.getStartPosition();
        var endPos = move.getEndPosition();
        if (chessBoard.getPiece(startPos).getTeamColor() != this.getTeamTurn()) {
            throw new InvalidMoveException();
        }
        var validMoves = validMoves(startPos);
        if (validMoves.isEmpty()) {
            throw new InvalidMoveException();
        }
        else {
            if (validMoves.contains(move)) {
                if (chessBoard.getPiece(endPos) == null) {
                    chessBoard.addPiece(endPos, returnCorrectPiece(move));
                    chessBoard.removePiece(startPos);
                    var currentTeamColor = this.getTeamTurn();
                    if (currentTeamColor == TeamColor.WHITE) {
                        this.setTeamTurn(TeamColor.BLACK);
                    } else {
                        this.setTeamTurn(TeamColor.WHITE);
                    }
                }
                else {
                    chessBoard.removePiece(endPos);
                    chessBoard.addPiece(endPos, returnCorrectPiece(move));
                    chessBoard.removePiece(startPos);
                    var currentTeamColor = this.getTeamTurn();
                    if (currentTeamColor == TeamColor.WHITE) {
                        this.setTeamTurn(TeamColor.BLACK);
                    } else {
                        this.setTeamTurn(TeamColor.WHITE);
                    }
                }
            }
            else {
                throw new InvalidMoveException();
            }
        }

    }

    public ChessPiece returnCorrectPiece(ChessMove move) {
        if (chessBoard.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN) {
            if (move.getPromotionPiece() != null) {
                return new ChessPieceImp(this.getTeamTurn(), move.getPromotionPiece());
            }
            else {
                return (ChessPieceImp) chessBoard.getPiece(move.getStartPosition());
            }
        }
        else {
            return (ChessPieceImp) chessBoard.getPiece(move.getStartPosition());
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    @Override
    public boolean isInCheck(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            var kingPos = findKing(teamColor);
            var listOfMovesThatCanAttackKing = piecesHaveKingInCheck(kingPos, teamColor);
            return !listOfMovesThatCanAttackKing.isEmpty();
        }
        else {
            var kingPos = findKing(teamColor);
            var listOfMovesThatCanAttackKing = piecesHaveKingInCheck(kingPos, teamColor);
            return !listOfMovesThatCanAttackKing.isEmpty();
        }
    }

    public Collection<ChessMove> piecesHaveKingInCheck(ChessPosition kingPos, TeamColor kingColor) {
        var listOfMovesThatCanAttackKing = new ArrayList<ChessMove>();
        if (kingPos == null) {
            return listOfMovesThatCanAttackKing;
        }

        // check if a knight can attack me
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 2, 1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 2, -1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 1, 2);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 1, -2);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -2, 1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -2, -1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -1, 2);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -1, -2);

        // check if rook or queen can attack me
            // moves right
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 7-kingPos.getColumn(), 1, 0);
            //moves left
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kingPos.getColumn(), -1, 0);
            // moves up
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 7-kingPos.getRow(), 0, 1);
            // moves down
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kingPos.getRow(), 0, -1);


        // check if bishop or queen can attack me
            // down and right
        var range = Math.min(kingPos.getRow(), 7-kingPos.getColumn());
        bishopOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, range, 1, -1);
            // down and left
        range = Math.min(kingPos.getRow(), kingPos.getColumn());
        bishopOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, range, -1, -1);
            // up and left
        range = Math.min(7-kingPos.getRow(), kingPos.getColumn());
        bishopOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, range, -1, 1);
            // up and right
        range = Math.min(7-kingPos.getRow(), 7-kingPos.getColumn());
        bishopOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, range, 1, 1);

        // check if a pawn can attack me. depends on whether I'm white or black
        pawnAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor);

        // check if opposing king can attack me.
        var kRow = kingPos.getRow();
        var kCol = kingPos.getColumn();
            // King attacked from above
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow+1, kCol);
            // King attacked from above and right
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow+1, kCol+1);
            // King attacked from right
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow, kCol+1);
            // King attacked from below and right
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow-1, kCol+1);
            // King attacked from below
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow-1, kCol);
            // King attacked from below and left
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow-1, kCol-1);
            // King attacked from left
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow, kCol-1);
            // King attacked from above and left
        kingAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kRow+1, kCol-1);

        return listOfMovesThatCanAttackKing;
    }


    public void kingAttacksKing(Collection<ChessMove> listOfMovesThatCanAttackKing, ChessPosition kingPos, TeamColor kingColor, int newRow, int newCol) {
        if (((newRow < 8) && (newRow >= 0)) && ((newCol < 8) && (newCol >= 0))) {
            var newPos = new ChessPositionImp(newRow, newCol);
            var potKing = this.chessBoard.getPiece(newPos);
            if ((potKing != null) && (potKing.getTeamColor() != kingColor) && (potKing.getPieceType()== ChessPiece.PieceType.KING)) {
                listOfMovesThatCanAttackKing.add(new ChessMoveImp(newPos, kingPos));
            }
        }
    }

    /**
     *
     * Function below checks if king is in check by way of pawn.
     *
     * @param listOfMovesThatCanAttackKing
     * @param kingPos
     * @param kingColor
     */
    public void pawnAttacksKing(Collection<ChessMove> listOfMovesThatCanAttackKing, ChessPosition kingPos, TeamColor kingColor) {
        if (kingColor == TeamColor.WHITE) {
            if (kingPos.getRow() != 7) {
                if (kingPos.getColumn() != 7) {
                    var newPos = new ChessPositionImp(kingPos.getRow() + 1, kingPos.getColumn() + 1);
                    var pieceUpAndRight = this.chessBoard.getPiece(newPos);
                    if ((pieceUpAndRight != null) && (pieceUpAndRight.getPieceType() == ChessPiece.PieceType.PAWN) && (pieceUpAndRight.getTeamColor() != kingColor)) {
                        listOfMovesThatCanAttackKing.add(new ChessMoveImp(newPos, kingPos));
                    }
                }
                if (kingPos.getColumn() != 0) {
                    var newPos = new ChessPositionImp(kingPos.getRow() + 1, kingPos.getColumn() - 1);
                    var pieceUpAndLeft = this.chessBoard.getPiece(newPos);
                    if ((pieceUpAndLeft != null) && (pieceUpAndLeft.getPieceType() == ChessPiece.PieceType.PAWN) && (pieceUpAndLeft.getTeamColor() != kingColor)) {
                        listOfMovesThatCanAttackKing.add(new ChessMoveImp(newPos, kingPos));
                    }
                }
            }
        }
        else {
            if (kingPos.getRow() != 0) {
                if (kingPos.getColumn() != 7) {
                    var newPos = new ChessPositionImp(kingPos.getRow() - 1, kingPos.getColumn() + 1);
                    var pieceBelowAndRight = this.chessBoard.getPiece(newPos);
                    if ((pieceBelowAndRight != null) && (pieceBelowAndRight.getPieceType() == ChessPiece.PieceType.PAWN) && (pieceBelowAndRight.getTeamColor() != kingColor)) {
                        listOfMovesThatCanAttackKing.add(new ChessMoveImp(newPos, kingPos));
                    }
                }
                if (kingPos.getColumn() != 0) {
                    var newPos = new ChessPositionImp(kingPos.getRow() - 1, kingPos.getColumn() - 1);
                    var pieceBelowAndLeft = this.chessBoard.getPiece(newPos);
                    if ((pieceBelowAndLeft != null) && (pieceBelowAndLeft.getPieceType() == ChessPiece.PieceType.PAWN) && (pieceBelowAndLeft.getTeamColor() != kingColor)) {
                        listOfMovesThatCanAttackKing.add(new ChessMoveImp(newPos, kingPos));
                    }
                }
            }
        }
    }

    /**
     * Function below checks if king can be put in check by a knight
     *
     * @param listOfMovesThatCanAttackKing
     * @param kingPos
     * @param kingColor
     * @param rowChanger
     * @param colChanger
     */
    public void knightAttacksKing(Collection<ChessMove> listOfMovesThatCanAttackKing, ChessPosition kingPos, TeamColor kingColor, int rowChanger, int colChanger){
        var rowNum = kingPos.getRow();
        var colNum = kingPos.getColumn();

        var newRow = rowNum + rowChanger;
        if ((newRow < 8) && (newRow) >= 0) {
            var newCol = colNum + colChanger;
            if ((newCol < 8) && (newCol >=0)) {
                var newPos = new ChessPositionImp(newRow, newCol);
                var potentialKnight = this.chessBoard.getPiece(newPos);
                if ((potentialKnight != null) && (potentialKnight.getPieceType() == ChessPiece.PieceType.KNIGHT) && (potentialKnight.getTeamColor() != kingColor)) {
                    listOfMovesThatCanAttackKing.add(new ChessMoveImp(newPos, kingPos));
                }
            }
        }
    }

    /**
     * Function below checks if the king is in check by a rook or a queen.
     *
     * @param listOfMovesThatCanAttackKing
     * @param kingPos
     * @param kingColor
     * @param range
     * @param colChanger
     * @param rowChanger
     */
    public void rookOrQueenAttacksKing(Collection<ChessMove> listOfMovesThatCanAttackKing, ChessPosition kingPos, TeamColor kingColor, int range, int colChanger, int rowChanger) {
        var newRow = kingPos.getRow();
        var newCol = kingPos.getColumn();

        for (int i = 0; i < range; i++) {
            newRow += rowChanger;
            newCol += colChanger;
            var newPos = new ChessPositionImp(newRow, newCol);
            var potRookOrQueen = this.chessBoard.getPiece(newPos);
            if ((potRookOrQueen != null) && ((potRookOrQueen.getPieceType() == ChessPiece.PieceType.ROOK) || (potRookOrQueen.getPieceType() == ChessPiece.PieceType.QUEEN)) && (potRookOrQueen.getTeamColor() != kingColor)) {
                listOfMovesThatCanAttackKing.add(new ChessMoveImp(newPos, kingPos));
                break;
            }
            else if (potRookOrQueen != null) {
                break;

            }
        }
    }

    /**
     *
     * Function below checks if King is in check by a bishop or queen.
     *
     * @param listOfMovesThatCanAttackKing
     * @param kingPos
     * @param kingColor
     * @param rangeToCheck
     * @param colChanger
     * @param rowChanger
     */
    public void bishopOrQueenAttacksKing(Collection<ChessMove> listOfMovesThatCanAttackKing, ChessPosition kingPos, TeamColor kingColor, int rangeToCheck, int colChanger, int rowChanger) {
        // Check to see how far we can go diagonally
        var newRowNum = kingPos.getRow();
        var newColNum = kingPos.getColumn();

        for (int i = 0; i < rangeToCheck; i++) {
            newRowNum += rowChanger;
            newColNum += colChanger;
            var newPos = new ChessPositionImp(newRowNum, newColNum);
            var potBishopOrQueen = this.chessBoard.getPiece(newPos);
            if ((potBishopOrQueen != null) && (potBishopOrQueen.getTeamColor() != kingColor) && ((potBishopOrQueen.getPieceType() == ChessPiece.PieceType.BISHOP) || (potBishopOrQueen.getPieceType() == ChessPiece.PieceType.QUEEN))) {
                listOfMovesThatCanAttackKing.add(new ChessMoveImp(newPos, kingPos));
                break;
            }
            else if (potBishopOrQueen != null) {
                break;
            }

        }
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    @Override
    public boolean isInCheckmate(TeamColor teamColor) {

        var kingPos = findKing(teamColor);
        var listOfMovesThatCanAttackKing = piecesHaveKingInCheck(kingPos, teamColor);
        if (listOfMovesThatCanAttackKing.size() > 1) {
            // See if king can move somewhere safely.
            return placesKingCanMove(kingPos, teamColor).isEmpty();
        }
        else if (listOfMovesThatCanAttackKing.size() == 1) {
            var kingCanMove = !placesKingCanMove(kingPos, teamColor).isEmpty();
            var piecesCanBlockOrAttack = !memberCanBlockOrCaptureAttacker(listOfMovesThatCanAttackKing, teamColor).isEmpty();
            // piece can block or attack for king?
            if ((kingCanMove) && (piecesCanBlockOrAttack)) {
                return false;
            }
            else { return true; }
        }
        else { return false; }

    }

    public ChessPosition findKing(TeamColor color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                var newPos = new ChessPositionImp(i, j);
                if (this.chessBoard.getPiece(newPos) != null) {
                    if (this.chessBoard.getPiece(newPos).getPieceType() == ChessPiece.PieceType.KING) {
                        if (this.chessBoard.getPiece(newPos).getTeamColor() == color) {
                            return newPos;
                        }
                    }
                }
            }
        }

        return null;
    }

    public Collection<ChessMove> placesKingCanMove(ChessPosition kingPos, TeamColor kingColor) {
        var listOfPosKingCanMove = new ArrayList<ChessMove>();
        var listOfKingMoves = (ArrayList<ChessMove>) this.chessBoard.getPiece(kingPos).pieceMoves(this.chessBoard, kingPos);
        for (ChessMove currMove : listOfKingMoves) {
            var listOfAttackers = piecesHaveKingInCheck(currMove.getEndPosition(), kingColor);
            if (listOfAttackers.isEmpty()) {
                listOfPosKingCanMove.add(currMove);
            }
        }
        return listOfPosKingCanMove;
    }

    public Collection<ChessMove> memberCanBlockOrCaptureAttacker(Collection<ChessMove> listOfMovesThatCanAttackKing, TeamColor kingColor) {
        // There will only be one attack move when we enter this function.

        var movesThatCanAttackKingAsArrayList = (ArrayList<ChessMove>) listOfMovesThatCanAttackKing;
        var moveAttackKing = movesThatCanAttackKingAsArrayList.get(0);
        var pieceThatCanAttackKing = chessBoard.getPiece(moveAttackKing.getStartPosition()).getPieceType();

        // find all positions between piece and king and the pos of the piece itself.
        var setOfSpaces = new HashSet<ChessPosition>();

        getSetOfSpaceBetweenAttackerAndKing(setOfSpaces, moveAttackKing);

        // Get the list of all possible piece moves of our team's pieces, except for king
        var listOfPiecesOfSameColor = new ArrayList<ChessPositionImp>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                var newPos = new ChessPositionImp(i, j);
                if (chessBoard.getPiece(newPos) != null) {
                    if ((chessBoard.getPiece(newPos).getTeamColor() == kingColor) && (newPos != moveAttackKing.getEndPosition())) {
                        listOfPiecesOfSameColor.add(newPos);
                    }
                }
            }
        }

        // List that contains all move to protect king from capture via blocking or attacking. (doesn't include the king)
        var listOfAllTeamMoves = new ArrayList<ChessMove>();
        for (ChessPosition pos: listOfPiecesOfSameColor) {
            var tempListOfMoves = chessBoard.getPiece(pos).pieceMoves(this.chessBoard, pos);
            for (ChessMove tempMove: tempListOfMoves) {
                if (setOfSpaces.contains(tempMove.getEndPosition())) {
                    listOfAllTeamMoves.add(tempMove);
                }
            }
        }

        // See which of our moves don't compromise the king's safety
        var finalListValidMoves = new ArrayList<ChessMove>();
        for (ChessMove move: listOfAllTeamMoves) {
            if (!moveCompromisesSafety(move, kingColor)) {
                finalListValidMoves.add(move);
            }
        }

        return finalListValidMoves;


    }


    public boolean moveCompromisesSafety(ChessMove move, TeamColor kingColor) {
        // this function assumes that the move can be made.

        // if king isn't on board, return false
        if (findKing(kingColor) == null) {
            return false;
        }

        // make move
        var oldEndPos = this.chessBoard.getPiece(move.getEndPosition());
        var oldStartPiece = this.chessBoard.getPiece(move.getStartPosition());
        if (oldEndPos == null) {
            this.chessBoard.addPiece(move.getEndPosition(), oldStartPiece);
            this.chessBoard.removePiece(move.getStartPosition());
            if (isInCheck(kingColor)) {
                this.chessBoard.removePiece(move.getEndPosition());
                this.chessBoard.addPiece(move.getStartPosition(), oldStartPiece);
                return true;
            }
            else {
                this.chessBoard.removePiece(move.getEndPosition());
                this.chessBoard.addPiece(move.getStartPosition(), oldStartPiece);
                return false;
            }
        }
        else {
            var oldEndPiece = oldEndPos;
            this.chessBoard.removePiece(move.getEndPosition());
            this.chessBoard.addPiece(move.getEndPosition(), oldStartPiece);
            this.chessBoard.removePiece(move.getStartPosition());
            if (isInCheck(kingColor)) {
                this.chessBoard.removePiece(move.getEndPosition());
                this.chessBoard.addPiece(move.getStartPosition(), oldStartPiece);
                this.chessBoard.addPiece(move.getEndPosition(), oldEndPiece);
                return true;
            }
            else {
                this.chessBoard.removePiece(move.getEndPosition());
                this.chessBoard.addPiece(move.getStartPosition(), oldStartPiece);
                this.chessBoard.addPiece(move.getEndPosition(), oldEndPiece);
                return false;
            }

        }
        // see if king still in check
        // if not, add to list of valid moves
        // unmake move
    }


    public void getSetOfSpaceBetweenAttackerAndKing(Collection<ChessPosition> setOfSpaces, ChessMove moveAttackKing) {
        var attackerS = moveAttackKing.getStartPosition();
        var attackerE = moveAttackKing.getEndPosition();

        if (piecePosIsHorToKing(attackerS, attackerE)) {
            horHelper(setOfSpaces, attackerS, attackerE);
        }
        else if (piecePosIsVerToKing(attackerS, attackerE)) {
            verHelper(setOfSpaces, attackerS, attackerE);
        }
        else if (piecePosIsDiagToKing(attackerS, attackerE)) {
            diagHelper(setOfSpaces, attackerS, attackerE);
        }
        else {
            setOfSpaces.add(attackerS);
        }
    }


    public boolean piecePosIsHorToKing(ChessPosition piecePos, ChessPosition kingPos) {
        return (piecePos.getRow() == kingPos.getRow());
    }

    public void horHelper(Collection<ChessPosition> setOfSpaces, ChessPosition attackerS, ChessPosition kingPos) {
        if (attackerS.getColumn() > kingPos.getColumn()) {
            var range = attackerS.getColumn() - kingPos.getColumn();
            for (int i = 0; i < range; i++) {
                setOfSpaces.add(new ChessPositionImp(attackerS.getRow(), attackerS.getColumn()-i));
            }
        }
        else {
            var range = kingPos.getColumn() - attackerS.getColumn();
            for (int i = 0; i < range; i++) {
                setOfSpaces.add(new ChessPositionImp(attackerS.getRow(), attackerS.getColumn()+i));
            }
        }
    }

    public boolean piecePosIsVerToKing(ChessPosition piecePos, ChessPosition kingPos) {
        return (piecePos.getColumn() == kingPos.getColumn());
    }

    public void verHelper(Collection<ChessPosition> setOfSpaces, ChessPosition attackerS, ChessPosition kingPos) {
        if (attackerS.getRow() > kingPos.getRow()) {
            var range = attackerS.getRow() - kingPos.getRow();
            for (int i = 0; i < range; i++) {
                setOfSpaces.add(new ChessPositionImp(attackerS.getColumn(), attackerS.getRow()-i));
            }
        }
        else {
            var range = kingPos.getRow() - attackerS.getRow();
            for (int i = 0; i < range; i++) {
                setOfSpaces.add(new ChessPositionImp(attackerS.getColumn(), attackerS.getRow()+i));
            }
        }
    }

    public boolean piecePosIsDiagToKing(ChessPosition piecePos, ChessPosition kingPos) {
        var slope = (piecePos.getRow()-kingPos.getRow())/(piecePos.getColumn()-kingPos.getColumn());
        return ((slope == 1) || (slope == -1));
    }

    public void diagHelper(Collection<ChessPosition> setOfSpaces, ChessPosition attackerS, ChessPosition kingPos) {
        var slope = (attackerS.getRow()-kingPos.getRow())/(attackerS.getColumn()-kingPos.getColumn());
        if (slope == 1) {
            if (attackerS.getColumn() < kingPos.getColumn()) {
                var range = kingPos.getColumn() - attackerS.getColumn();
                diagHelperHelper(setOfSpaces, attackerS, range, 1, 1);
            }
            else {
                var range = attackerS.getColumn() - kingPos.getColumn();
                diagHelperHelper(setOfSpaces, attackerS, range, -1, -1);
            }
        }
        else {
            if (attackerS.getColumn() < kingPos.getColumn()) {
                var range = kingPos.getColumn() - attackerS.getColumn();
                diagHelperHelper(setOfSpaces, attackerS, range, -1, 1);
            }
            else {
                var range = attackerS.getColumn() - kingPos.getColumn();
                diagHelperHelper(setOfSpaces, attackerS, range, 1, -1);
            }
        }
    }

    public void diagHelperHelper(Collection<ChessPosition> setOfSpaces, ChessPosition attackerS, int range, int rowChanger, int colChanger) {
        for (int i = 0; i < range; i++) {
            setOfSpaces.add(new ChessPositionImp(attackerS.getRow()+(rowChanger*i), attackerS.getColumn()+(colChanger*i)));
        }
    }

/**
    public Collection<ChessMove> listOfBlocksOrAttacks(ChessMoveImp moveAttackKing, ChessPieceImp pieceAttackKing) {


        // if attacker is knight, get all pieces who can attack knight.
        if (pieceAttackKing.getPieceType() == ChessPiece.PieceType.KNIGHT) {

        }
        // if attacker is rook or queen, get all pieces who can attack or block.

        // if attacker is bishop or queen, get all pieces who can attack or block.

        // if attacker is pawn, get all pieces who can attack or block.
    }

    public void movesToProtectKing(Collection<ChessMove> listOfMovesToProtectKing, HashSet<ChessPosition> setOfPlacesToMoveProtectors, ChessPositionImp kingPos, TeamColor kingColor) {
        var listOfPiecesOfSameColor = new ArrayList<ChessPositionImp>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                var newPos = new ChessPositionImp(i, j);
                if ((chessBoard.getPiece(newPos).getTeamColor() == kingColor) && (newPos != kingPos)) {
                    listOfPiecesOfSameColor.add(newPos);
                }
            }
        }

        // List that contains all move to protect king from capture via blocking or attacking. (doesn't include the king)
        var listOfAllTeamMoves = new ArrayList<ChessMove>();
        for (ChessPosition pos: listOfPiecesOfSameColor) {
            var tempListOfMoves = chessBoard.getPiece(pos).pieceMoves(this.chessBoard, pos);
            for (ChessMove tempMove: tempListOfMoves) {
                if (setOfPlacesToMoveProtectors.contains(tempMove.getEndPosition())) {
                    listOfAllTeamMoves.add(tempMove);
                }
            }
        }

        // List that contains all valid moves to protect king from attack via blocking or attacking
        var listOfValidMoves = new ArrayList<ChessMove>();
        for (ChessMove move: listOfAllTeamMoves) {
            if ((piecePosIsHorToKing(move.getStartPosition(), kingPos)) && (!piecePosIsHorToKing(move.getEndPosition(), kingPos))) {
                // do thing
            }
            else if ((piecePosIsVerToKing(move.getStartPosition(), kingPos)) && (!piecePosIsVerToKing(move.getEndPosition(), kingPos))) {
                // do thing
            }
            else if ((piecePosIsDiagToKing(move.getStartPosition(), kingPos)) && (!piecePosIsDiagToKing(move.getEndPosition(), kingPos))) {
                // do thing
            }
            else {
                listOfValidMoves.add(move);
            }
        }

    }

    public boolean piecePosIsHorToKing(ChessPosition piecePos, ChessPosition kingPos) {
        return (piecePos.getRow() == kingPos.getRow());
    }

    public boolean HorChecker(ChessPosition piecePos, int colChanger) {
        var currCol = piecePos.getColumn();
        var range = 0;
        if (colChanger == -1) {
            range = piecePos.getColumn();
        }
        else { range = 7-piecePos.getColumn(); }
        for (int i = 0; i < range; i++) {
            currCol -= 1;
            var newPos = new ChessPositionImp(piecePos.getRow(), currCol);

        }
    }

    public boolean piecePosIsVerToKing(ChessPosition piecePos, ChessPosition kingPos) {
        return (piecePos.getColumn() == kingPos.getColumn());
    }

    public boolean piecePosIsDiagToKing(ChessPosition piecePos, ChessPosition kingPos) {
        var slope = (piecePos.getRow()-kingPos.getRow())/(piecePos.getColumn()-kingPos.getColumn());
        return ((slope == 1) || (slope == -1));
    } **/

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        // check if king is in check,
        // if not, and king has nowhere to move
        // then king is in stalemate
        if (isInCheck(teamColor)) {
            return false;
        }
        else {
            var placesKingCanMove = placesKingCanMove(findKing(teamColor), teamColor);
            if (placesKingCanMove.isEmpty()) {
                return true;
            }
            else {
                return false;
            }

        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    @Override
    public void setBoard(ChessBoard board) {
        this.chessBoard = (ChessBoardImp) board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    @Override
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
