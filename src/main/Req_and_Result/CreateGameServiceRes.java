package Req_and_Result;

import java.util.HashMap;

public class CreateGameServiceRes {

    public CreateGameServiceRes() {
    }

    private String message;
    private int gameID;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
