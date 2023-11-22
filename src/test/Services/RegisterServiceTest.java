package Services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest extends testSetup{

    @Test
    void clearData1() {
        this.cleardb();
    }
    @Test
    void registerUser() {
        var regRes = this.registerUser("register_jack", "secret", "gmail");
        assertEquals(regRes.getMessage(), "success");
    }

    @Test
    void tryToRegisterUser() {
        var regRes = this.registerUser("register_jack", "secret", "gmail");
        assertEquals(regRes.getMessage(), "already taken");

    }

    @Test
    void clearData() {
        this.cleardb();
    }
}