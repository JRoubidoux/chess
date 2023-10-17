package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * This class represents the chess game. The board, pieces and rules are all found here.
 */
public class ChessGameImp implements ChessGame {

    private ChessBoardImp chessBoard = new ChessBoardImp();
    private ChessGame.TeamColor currentTurn = TeamColor.WHITE;




    /**
     * Return which team's turn it is
     *
     * @return The color of team whose turn it is.
     */
    @Override
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Sets the current turn attribute to the right team.
     *
     * @param team The color we are setting current team to.
     */
    @Override
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Gets valid moves for a piece at the given location, or null if no piece at startPosition
     *
     * @param startPosition The start position we want to get the valid moves for.
     * @return All the valid moves we can make.
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
            var listOfMovesThatCanAttackKing = piecesHaveKingInCheck(kingPos, colorOfMovePiece);

            // If King is in check
            if (listOfMovesThatCanAttackKing.size() == 1) {
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

            // King is not in check
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
     * @param move A chessmove we would like to attempt to make
     * @throws InvalidMoveException if move is invalid
     */
    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        var startPos = move.getStartPosition();
        var endPos = move.getEndPosition();

        // If piece trying to move is not the color of the current team throw exception
        if (chessBoard.getPiece(startPos).getTeamColor() != this.getTeamTurn()) {
            throw new InvalidMoveException();
        }

        var validMoves = validMoves(startPos);

        // If there is no piece at the position or the piece has no moves, throw exception
        if (validMoves.isEmpty()) {
            throw new InvalidMoveException();
        }
        else {
            if (validMoves.contains(move)) {
                // If there is no piece at the end position.
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
                // If there is an enemy piece in the end position, remove enemy piece.
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

    /**
     * This is a helper function to return the right piece that should end up at the end of a move.
     * If the piece is a pawn and has made it to the end of the board, then the piece is up for promotion.
     *
     * @param move The move of a piece on the chessboard.
     * @return A ChessPiece object
     */
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
     * Determines if the given team is in check.
     *
     * @param teamColor the color of the current team.
     * @return True is team is in check, false otherwise.
     */
    @Override
    public boolean isInCheck(TeamColor teamColor) {
        var kingPos = findKing(teamColor);
        var listOfMovesThatCanAttackKing = piecesHaveKingInCheck(kingPos, teamColor);
        return !listOfMovesThatCanAttackKing.isEmpty();
    }

    /**
     * See if there are any pieces that have the king in check.
     *
     * @param kingPos A ChessPosition Object, represents the king's location
     * @param kingColor A TeamColor Object, represents the king's color
     * @return A list of moves that currently have the King in check.
     */
    public Collection<ChessMove> piecesHaveKingInCheck(ChessPosition kingPos, TeamColor kingColor) {
        var listOfMovesThatCanAttackKing = new ArrayList<ChessMove>();
        if (kingPos == null) {
            return listOfMovesThatCanAttackKing;
        }

        // check if a knight can attack king
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 2, 1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 2, -1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 1, 2);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 1, -2);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -2, 1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -2, -1);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -1, 2);
        knightAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, -1, -2);

        // check if rook or queen can attack king
            // moves right
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 7-kingPos.getColumn(), 1, 0);
            //moves left
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kingPos.getColumn(), -1, 0);
            // moves up
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, 7-kingPos.getRow(), 0, 1);
            // moves down
        rookOrQueenAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor, kingPos.getRow(), 0, -1);


        // check if bishop or queen can attack king
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

        // check if a pawn can attack king. depends on whether I'm white or black
        pawnAttacksKing(listOfMovesThatCanAttackKing, kingPos, kingColor);

        // check if opposing king can attack current king.
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


    /**
     * Helper function to see if a king can attack another king.
     *
     * @param listOfMovesThatCanAttackKing A list of ChessMoves. A list of moves that have king in check.
     * @param kingPos A ChessPosition object. The current location of the king.
     * @param kingColor A TeamColor object. The color of the king.
     * @param newRow An integer representing a row in the chess board.
     * @param newCol An integer representing a column in the chess board.
     */
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
     * Function below checks if king is in check by way of pawn.
     *
     * @param listOfMovesThatCanAttackKing A list of ChessMove objects that can attack the king.
     * @param kingPos ChessPositionObject, King's current location.
     * @param kingColor TeamColorObject, King's current color.
     */
    public void pawnAttacksKing(Collection<ChessMove> listOfMovesThatCanAttackKing, ChessPosition kingPos, TeamColor kingColor) {
        // We need to know the color to see which pawns can attack.
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
     * @param listOfMovesThatCanAttackKing A list of ChessMove objects that represent pieces who have king in check
     * @param kingPos ChessPosition Object. The King's current position
     * @param kingColor TeamColor object. The King's color
     * @param rowChanger An integer that represents a row the knight could be located in.
     * @param colChanger An integer that represents a column the knight could be located in.
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
     * @param listOfMovesThatCanAttackKing A list of ChessMove objects that represent pieces who have king in check
     * @param kingPos ChessPosition object. King's position.
     * @param kingColor TeamColor object. King's color
     * @param range An integer representing how far we check on the board for a piece.
     * @param colChanger An integer that represents whether we are going up, down or staying the same.
     * @param rowChanger An integer that represents whether we are going left, right or staying the same.
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
     * Function below checks if King is in check by a bishop or queen.
     *
     * @param listOfMovesThatCanAttackKing A list of ChessMove objects that represent pieces who have king in check.
     * @param kingPos ChessPosition object. King's position.
     * @param kingColor TeamColor object. King's color.
     * @param rangeToCheck An integer representing how far we check on the board for a piece.
     * @param colChanger An integer that represents whether we are going up, down or staying the same.
     * @param rowChanger An integer that represents whether we are going left, right or staying the same.
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
     * @param teamColor TeamColor object. Color of team whose turn it is.
     * @return Boolean value. Whether certain team is in checkmate.
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

    /**
     * This function located where a king of a given color is located on the chessBoard
     *
     * @param color TeamColor object. Color of King piece we want to find.
     * @return ChessPosition object. Location of found King.
     */
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

    /**
     * This function returns a list of places the king can safely move if there are any.
     *
     * @param kingPos ChessPosition object. King's position
     * @param kingColor TeamColor object. King's color.
     * @return A List of ChessMove objects that represent where the King can move.
     */
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

    /**
     * Function that determines if there are any pieces that can attack a piece that has king in check or
     * if there are any pieces that can block the path of the piece that has the king in check.
     * All of this without compromising the safety of the king.
     *
     * @param listOfMovesThatCanAttackKing A list of ChessMove objects that represent pieces who have king in check.
     * @param kingColor TeamColor object. King's color.
     * @return A list of ChessMove objects that represent team pieces who can move to make King safe.
     */
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


    /**
     * The function checks if a given chess move would compromise the safety of the king
     *
     * @param move ChessMove object. Some move for a piece.
     * @param kingColor TeamColor object. King Color
     * @return Boolean value. Whether moving a piece would put the king in danger.
     */
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
    }


    /**
     * This function finds all the positions between an attacker and the king, and the position of the attacker.
     *
     *
     * @param setOfSpaces A set of ChessPosition objects. Represents space between King and attacker.
     * @param moveAttackKing A ChessMove object. The move of the attacker that could capture the King.
     */
    public void getSetOfSpaceBetweenAttackerAndKing(Collection<ChessPosition> setOfSpaces, ChessMove moveAttackKing) {
        var attackerS = moveAttackKing.getStartPosition();
        var attackerE = moveAttackKing.getEndPosition();

        // Check if an attacker is horizontal to the king
        if (piecePosIsHorToKing(attackerS, attackerE)) {
            horHelper(setOfSpaces, attackerS, attackerE);
        }

        // Or Vertical
        else if (piecePosIsVerToKing(attackerS, attackerE)) {
            verHelper(setOfSpaces, attackerS, attackerE);
        }

        // Or diagonal
        else if (piecePosIsDiagToKing(attackerS, attackerE)) {
            diagHelper(setOfSpaces, attackerS, attackerE);
        }

        // Or if attacker is a knight
        else {
            setOfSpaces.add(attackerS);
        }
    }


    /**
     * Checks if a piece is horizontal to the king
     *
     * @param piecePos ChessPosition object. Some piece's position.
     * @param kingPos ChessPosition object. King's position.
     * @return Boolean Value if a piece is horizontal to King.
     */
    public boolean piecePosIsHorToKing(ChessPosition piecePos, ChessPosition kingPos) {
        return (piecePos.getRow() == kingPos.getRow());
    }

    /**
     * if a piece is horizontal to king, get all spaces between king and attacker, including the position of the attacker.
     *
     * @param setOfSpaces A set of ChessPosition objects, represents space from attacker to king.
     * @param attackerS ChessPosition object. The start position of the attacker.
     * @param kingPos ChessPosition object. The King's position.
     */
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

    /**
     * Checks if an attacker is vertical to the king
     *
     * @param piecePos ChessPosition object. Some piece's position.
     * @param kingPos ChessPosition object. The King's position.
     * @return Boolean value if piece is vertical to King.
     */
    public boolean piecePosIsVerToKing(ChessPosition piecePos, ChessPosition kingPos) {
        return (piecePos.getColumn() == kingPos.getColumn());
    }

    /**
     * if a piece is vertical to king, get all spaces between king and attacker, including the position of the attacker.
     *
     * @param setOfSpaces set of ChessPosition objects representing positions from attacker to King.
     * @param attackerS ChessPosition object. Start position of attacker.
     * @param kingPos ChessPosition object. King's position.
     */
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

    /**
     * Checks if attacker is diagonal to King.
     *
     * @param piecePos ChessPosition object. Some piece's position.
     * @param kingPos ChessPosition object. King's position.
     * @return Boolean value if piece is diagonal to king.
     */
    public boolean piecePosIsDiagToKing(ChessPosition piecePos, ChessPosition kingPos) {
        var slope = (piecePos.getRow()-kingPos.getRow())/(piecePos.getColumn()-kingPos.getColumn());
        return ((slope == 1) || (slope == -1));
    }

    /**
     * if a piece is diagonal to king, get all spaces between king and attacker, including the position of the attacker.
     *
     * @param setOfSpaces set of ChessPosition objects. Represents positions from attacker to King.
     * @param attackerS ChessPosition object. Attacker's start position.
     * @param kingPos ChessPosition object. King's position.
     */
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

    /**
     * This is a helper function to the diagHelper function.
     *
     * @param setOfSpaces set of ChessPosition objects. Represents the positions from attacker to King.
     * @param attackerS ChessPosition object. Attacker's start position.
     * @param range An integer representing how far we check on the board for a piece.
     * @param colChanger An integer that represents whether we are going up, down or staying the same.
     * @param rowChanger An integer that represents whether we are going left, right or staying the same.
     */
    public void diagHelperHelper(Collection<ChessPosition> setOfSpaces, ChessPosition attackerS, int range, int rowChanger, int colChanger) {
        for (int i = 0; i < range; i++) {
            setOfSpaces.add(new ChessPositionImp(attackerS.getRow()+(rowChanger*i), attackerS.getColumn()+(colChanger*i)));
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor TeamColor Object. Color of a given team.
     * @return Boolean value whether team is in stalemate.
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
     * @param board ChessBoard object.
     */
    @Override
    public void setBoard(ChessBoard board) {
        this.chessBoard = (ChessBoardImp) board;
    }

    /**
     * Gets the current chessboard
     *
     * @return ChessBoard object: The current game's chessboard.
     */
    @Override
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
