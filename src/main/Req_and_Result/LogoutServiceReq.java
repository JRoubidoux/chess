package Req_and_Result;

/**
 * Request Class to support the Logout Service class
 */
public class LogoutServiceReq {

    /**
     * Class constructor
     */
    public LogoutServiceReq() {}

    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
