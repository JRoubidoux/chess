package Req_and_Result;

/**
 * Response Class to support the Login Service class.
 */
public class LoginServiceRes extends genRes {

    /**
     * Class constructor
     */
    public LoginServiceRes() {}

    private String authToken;
    private String username;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {return authToken;}

    public String getUsername() {return username;}
}
