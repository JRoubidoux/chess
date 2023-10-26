package Req_and_Result;

import java.util.HashMap;
import java.util.Map;

/**
 * Response Class to support the Register Service class.
 */
public class RegisterServiceRes extends genRes{

    /**
     * Class constructor
     */
    public RegisterServiceRes() {}

    private String username;
    private String authToken;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }



}
