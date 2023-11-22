package Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest extends testSetup {

    @BeforeEach
    void clearData() {
        this.cleardb();
    }


    @Test
    void testLogin() {
        var regRes = this.registerUser("login_jack", "secret", "gmail");
        assertEquals(regRes.getMessage(), "success");

        var loginRes = this.loginUser("login_jack", "secret");
        assertEquals(loginRes.getMessage(), "success");
    }

    @Test
    void testLoginNoPass() {
        var regRes = this.registerUser("login_jack", "secret", "gmail");
        assertEquals(regRes.getMessage(), "success");

        var loginRes = this.loginUser("jack", "mula");
        assertEquals(loginRes.getMessage(), "unauthorized");
    }

    @Test
    void clearDataAfter() {
        this.cleardb();
    }
}