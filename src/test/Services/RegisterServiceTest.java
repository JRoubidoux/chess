package Services;

import Req_and_Result.RegisterServiceReq;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.util.*;
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