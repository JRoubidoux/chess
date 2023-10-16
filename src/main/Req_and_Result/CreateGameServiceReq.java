package Req_and_Result;

public class CreateGameServiceReq {

    public CreateGameServiceReq() {
    }

    private String authToken;
    private String gameName;
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
