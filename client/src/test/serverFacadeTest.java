import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class serverFacadeTest {

    private static serverFacade serverConn = new serverFacade("http://localhost:8080");
    private static String[] defaultRegParams = {"jackie", "password", "jack@gmail.com"};
    private static String jackieAuthToken;


    @BeforeAll
    static void preliminary() {
        serverConn.clear();
        jackieAuthToken = serverConn.register(defaultRegParams);
    }

    @Test
    void login() {
        String[] params = {"jackie", "password"};
        var loginAuthToken = serverConn.login(params);
        assertNotNull(loginAuthToken, "Login failed.");
        serverConn.logout(loginAuthToken);

        String[] params2 = {"zzxx83g**", "password"};
        var login2AuthToken = serverConn.login(params2);
        assertNull(login2AuthToken, "Login succeeded but shouldn't have.");
    }

    @Test
    void logout() {
        String[] params = {"jackie", "password"};
        var loginAuthToken = serverConn.login(params);
        assertNotNull(loginAuthToken, "Login failed.");
        var result = serverConn.logout(loginAuthToken);
        assertTrue(result, "Logout failed.");

        String[] params2 = {"jackie", "password"};
        var login2AuthToken = "notARealAuthToken";
        var result2 = serverConn.logout(login2AuthToken);
        assertFalse(result2, "Logout succeeded when it shouldn't have.");
    }

    @Test
    void register() {
        String[] params = {"notInDB", "password", "jack@gmail.com"};
        var authToken = serverConn.register(params);
        assertNotNull(authToken, "Register failed.");
        serverConn.logout(authToken);

        var authToken2 = serverConn.register(defaultRegParams);
        assertNull(authToken2, "Registration succeeded when it shouldn't have.");
    }

    @Test
    void create() {
        String[] params = {"jackie's_game"};
        var result = serverConn.create(params, jackieAuthToken);
        assertNotNull(result, "Game creation failed.");

        var result2 = serverConn.create(params, "notAAuthToken");
        assertNull(result2, "game created when it should've have.");
    }

    @Test
    void list() {
        var result = serverConn.list(jackieAuthToken);
        assertNotNull(result, "games were not listed.");

        var result2 = serverConn.list("notAnAuthToken");
        assertNull(result2, "list games shouldn't have resulted in anything.");
    }

    @Test
    void joinOrObserve() {
        var result = serverConn.joinOrObserve(new String[]{"1", "WHITE"}, jackieAuthToken);
        assertTrue(result, "unable to join the game");

        var result2 = serverConn.joinOrObserve(new String[]{"1"}, "notAnAuthToken");
        assertFalse(result2, "observe game worked but shouldn't have.");
    }
}