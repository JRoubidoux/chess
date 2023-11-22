package Req_and_Result;

import java.util.Map;

/**
 * Request Class to support the Logout Service class
 */
public class LogoutServiceReq {

    /**
     * Class constructor
     */
    public LogoutServiceReq(String authTokenS) {
        setAuthToken(authTokenS);
    }

    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
