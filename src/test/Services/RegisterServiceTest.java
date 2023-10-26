package Services;

import Req_and_Result.RegisterServiceReq;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    @Test
    void registerApp() {

        var body = new HashMap<String, String>();
        body.put("username", "jack");
        body.put("password","secret");
        body.put("email", "gmail");

        var regReq = new RegisterServiceReq(body);
        var regServ = new RegisterService();
        var regRes = regServ.register(regReq);

        assertEquals(regRes.getMessage(), "success");

        var body2 = new HashMap<String, String>();
        body2.put("username", "jack");
        body2.put("password","secret");
        body2.put("email", "gmail");

        regReq = new RegisterServiceReq(body2);
        regServ = new RegisterService();
        regRes = regServ.register(regReq);

        assertEquals(regRes.getMessage(), "already taken");

    }
}