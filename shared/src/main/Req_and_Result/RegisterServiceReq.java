package Req_and_Result;

import java.util.Map;

/**
 * Request Class to support the Register Service class.
 */
public class RegisterServiceReq {

    /**
     * Class constructor
     */
    public RegisterServiceReq(Map<String, String> body) {
        setUsername(body.get("username"));
        setEmail(body.get("email"));
        setPassword(body.get("password"));
    }

    private String username;
    private String password;
    private String email;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
