package Models;

import chess.ChessGameImp;

/**
 * Class that represents a game object on the server.
 */
public class Game {

    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGameImp game;
    private String resigned = null;

    public String getResigned() {
        return resigned;
    }

    public void setResigned(String resigned) {
        this.resigned = resigned;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ChessGameImp getGame() {
        return game;
    }

    public void setGame(ChessGameImp game) {
        this.game = game;
    }
}
