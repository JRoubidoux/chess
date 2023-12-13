package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    private int gameID;
    private ChessMove move;
    public MakeMoveCommand(int gameID, String authToken, ChessMove move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }

    public int getGameID() {
        return this.gameID;
    }

    public ChessMove getMove() {
        return this.move;
    }

}