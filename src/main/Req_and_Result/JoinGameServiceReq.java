package Req_and_Result;

import chess.ChessGame;

import java.util.HashMap;

public class JoinGameServiceReq {

    public JoinGameServiceReq() {
    }

    private String authToken;
    private String color;
    private int gameID;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
