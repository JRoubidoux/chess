package Models;

/**
 * This class represents an authToken that is created for a user during a given session.
 */
public class AuthToken {

    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }
    private String authToken;
    private String username;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
