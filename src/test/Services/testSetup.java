package Services;

import DAOs.AuthDAO;
import DAOs.GameDAO;
import DAOs.UserDAO;
import Req_and_Result.*;
import dataAccess.DataAccessException;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testSetup {


    public genRes registerUser(String username, String password, String email) {
        var body = new HashMap<String, String>();
        body.put("username", username);
        body.put("password", password);
        body.put("email", email);

        var regReq = new RegisterServiceReq(body);
        var regServ = new RegisterService();
        return regServ.register(regReq);
    }

    public genRes loginUser(String username, String password) {
        var body2 = new HashMap<String, String>();
        body2.put("username", username);
        body2.put("password", password);
        var loginReq = new LoginServiceReq(body2);
        var loginServ = new LoginService();
        return loginServ.login(loginReq);
    }

    public genRes logoutUser(String authToken) {
        var logoutReq = new LogoutServiceReq(authToken);
        var logoutServ = new LogoutService();
        return logoutServ.logout(logoutReq);
    }

    public genRes createAGame(String authToken, String gameID) {
        var createGameReq = new CreateGameServiceReq(authToken, gameID);
        var createGameServ = new CreateGameService();
        return createGameServ.createGame(createGameReq);
    }

    public void cleardb() {
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
}
