package webSocketMessages.userCommands;

public class JoinPlayerCommand extends UserGameCommand {

    private int gameID;
    private String playerColor;
    public JoinPlayerCommand(int gameID, String authToken, String color) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = color;
    }

    public int getGameID() {
        return this.gameID;
    }

    public String getPlayerColor() {
        return this.playerColor;
    }

}
