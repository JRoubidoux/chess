package Req_and_Result;

/**
 * Response Class to support the Register Service class.
 */
public class RegisterServiceRes {

    /**
     * Class constructor
     */
    public RegisterServiceRes() {}

    private String username;
    private String authToken;
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}