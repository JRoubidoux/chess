package Req_and_Result;

import java.util.Map;

/**
 * Request Class to support the Login Service class.
 */
public class LoginServiceReq {

    /**
     * Class constructor
     */
    public LoginServiceReq(Map<String, String> body) {
        setUsername(body.get("username"));
        setPassword(body.get("password"));
    }

    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
