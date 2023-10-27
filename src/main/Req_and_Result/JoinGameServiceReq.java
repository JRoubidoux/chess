package Req_and_Result;

import chess.ChessGame;

/**
 * Request Class to support the JoinGame Service class.
 */
public class JoinGameServiceReq {

    /**
     * Class constructor
     */
    public JoinGameServiceReq(String authToken, String color, int gameID) {
        setAuthToken(authToken);
        setColor(color);
        setGameID(gameID);


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
