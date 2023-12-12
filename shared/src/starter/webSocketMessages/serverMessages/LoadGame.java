package webSocketMessages.serverMessages;

import Models.Game;

public class LoadGame extends ServerMessage{

    private String game;
    public LoadGame(ServerMessageType type, String game) {
        super(type);
        this.game = game;
    }

    public String getGame() {
        return this.game;
    }


}
