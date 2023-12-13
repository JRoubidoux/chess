package webSocketMessages.serverMessages;

public class Error extends ServerMessage{

    private String errorMessage;
    public Error(ServerMessageType type, String message) {
        super(type);
        this.errorMessage = message;
    }

    public String getMessage() {
        return this.errorMessage;
    }
}