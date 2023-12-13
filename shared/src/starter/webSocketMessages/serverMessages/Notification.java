package webSocketMessages.serverMessages;

public class Notification extends ServerMessage{

    private String message;
    private boolean gameInPlay;
    public Notification(ServerMessageType type, String message, boolean gameInPlay) {
        super(type);
        this.message = message;
        this.gameInPlay = gameInPlay;
    }

    public String getMessage() {
        return this.message;
    }
    public boolean getGameInPlay() { return this.gameInPlay; }
}
