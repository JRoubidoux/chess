package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    private int gameID;
    private ChessMove move;
    private String teamColor;
    public MakeMoveCommand(int gameID, String authToken, ChessMove move, String color) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
        this.teamColor = color;
    }

    public int getGameID() {
        return this.gameID;
    }

    public ChessMove getMove() {
        return this.move;
    }

    public String getTeamColor() { return this.teamColor; }

}