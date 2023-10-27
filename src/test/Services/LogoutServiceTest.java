package Services;

import DAOs.AuthDAO;
import DAOs.GameDAO;
import DAOs.UserDAO;
import Req_and_Result.LoginServiceRes;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest extends testSetup {

    @BeforeEach
    void clearData() {
        this.cleardb();
    }


    @Test
    void testLogout() {
        var regRes = this.registerUser("jack", "secret", "gmail");
        assertEquals(regRes.getMessage(), "success");

        var loginRes = this.loginUser("jack", "secret");
        assertEquals(loginRes.getMessage(), "success");

        var logoutRes = this.logoutUser(((LoginServiceRes) loginRes).getAuthToken());
        assertEquals(logoutRes.getMessage(), "success");
    }

    @Test
    void testLogoutNoAuth() {
        var regRes = this.registerUser("jack", "secret", "gmail");
        assertEquals(regRes.getMessage(), "success");

        var loginRes = this.loginUser("jack", "secret");
        assertEquals(loginRes.getMessage(), "success");

        var logoutRes = this.logoutUser("random string");
        assertEquals(logoutRes.getMessage(), "unauthorized");
    }


}