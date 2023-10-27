package Req_and_Result;

/**
 * Response Class to support the CreateGame Service class.
 */
public class CreateGameServiceRes extends genRes {

    /**
     * Class constructor
     */
    public CreateGameServiceRes() {
    }

    private int gameID;


    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
