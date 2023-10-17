package Req_and_Result;

/**
 * Response Class to support the CreateGame Service class.
 */
public class CreateGameServiceRes {

    /**
     * Class constructor
     */
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
