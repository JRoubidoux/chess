package Services;

import Req_and_Result.RegisterServiceReq;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest extends testSetup{

    @Test
    void registerUser() {
        var regRes = this.registerUser("jack", "secret", "gmail");
        assertEquals(regRes.getMessage(), "success");
    }

    @Test
    void tryToRegisterUser() {
        var regRes = this.registerUser("jack", "secret", "gmail");
        assertEquals(regRes.getMessage(), "already taken");

    }
}