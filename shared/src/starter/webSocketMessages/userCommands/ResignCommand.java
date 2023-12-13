package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {

    private int gameID;
    public ResignCommand(int gameID, String authToken) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }

    public int getGameID() {
        return this.gameID;
    }


}