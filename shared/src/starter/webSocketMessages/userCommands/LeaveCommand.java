package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {

    private int gameID;
    private String playerColor;
    public LeaveCommand(int gameID, String authToken, String color) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
        this.playerColor = color;
    }

    public int getGameID() {
        return this.gameID;
    }
    public String getPlayerColor() { return this.playerColor;}

}
