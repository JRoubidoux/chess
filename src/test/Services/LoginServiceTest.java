package Services;

import DAOs.AuthDAO;
import DAOs.GameDAO;
import DAOs.UserDAO;
import Database.DataBaseRAM;
import Req_and_Result.LoginServiceReq;
import Req_and_Result.RegisterServiceReq;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    @BeforeEach
    void cleardb() {
        var auth = new AuthDAO();
        var user = new UserDAO();
        var game = new GameDAO();
        try {
            game.ClearGames();
            auth.clearAuth();
            user.clearUsers();
        }
        catch (DataAccessException e) {
            System.out.println("server failure");
        }

    }
    @Test
    void testLogin() {

        var body = new HashMap<String, String>();
        body.put("username", "jack");
        body.put("password", "secret");
        body.put("email", "gmail");

        var regReq = new RegisterServiceReq(body);
        var regServ = new RegisterService();
        var regRes = regServ.register(regReq);

        assertEquals(regRes.getMessage(), "success");

        var body2 = new HashMap<String, String>();
        body2.put("username", "jack");
        body2.put("password", "secret");
        var loginReq = new LoginServiceReq(body2);
        var loginServ = new LoginService();
        var loginRes = loginServ.login(loginReq);
        assertEquals(loginRes.getMessage(), "success");
    }

    @Test
    void testLoginNoPass() {
        var body = new HashMap<String, String>();
        body.put("username", "jack");
        body.put("password", "secret");
        body.put("email", "gmail");

        var regReq = new RegisterServiceReq(body);
        var regServ = new RegisterService();
        var regRes = regServ.register(regReq);

        assertEquals(regRes.getMessage(), "success");

        var body2 = new HashMap<String, String>();
        body2.put("username", "jack");
        body2.put("password", "mula");
        var loginReq = new LoginServiceReq(body2);
        var loginServ = new LoginService();
        var loginRes = loginServ.login(loginReq);
        assertEquals(loginRes.getMessage(), "unauthorized");
    }
}