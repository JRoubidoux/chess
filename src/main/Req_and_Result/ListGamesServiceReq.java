package Req_and_Result;


/**
 * Request Class to support the ListGames Service class
 */
public class ListGamesServiceReq {

    /**
     * Class constructor
     */
    public ListGamesServiceReq() {
    }

    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
