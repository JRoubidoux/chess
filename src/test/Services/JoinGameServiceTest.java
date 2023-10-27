package Services;

import Req_and_Result.CreateGameServiceRes;
import Req_and_Result.JoinGameServiceRes;
import Req_and_Result.RegisterServiceRes;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JoinGameServiceTest extends testSetup {


    @Test
    void joinAGame() {
        var result = ((RegisterServiceRes)this.registerUser("join_jack", "pass", "email"));
        var gameResult = ((CreateGameServiceRes)this.createAGame(result.getAuthToken(), "gameName"));
        assertEquals(gameResult.getMessage(), "success");

        var joinRes = ((JoinGameServiceRes)this.joinAGame(result.getAuthToken(), "WHITE", gameResult.getGameID()));
        assertEquals(joinRes.getMessage(), "success");
    }

    @Test
    void joinAGameColorTaken() {
        var result = ((RegisterServiceRes)this.registerUser("join_jack2", "pass1", "email"));
        var joinRes = ((JoinGameServiceRes)this.joinAGame(result.getAuthToken(), "WHITE", 1));
        assertEquals(joinRes.getMessage(), "already taken");
    }

    @Test
    void clearData() {
        this.cleardb();
    }



}