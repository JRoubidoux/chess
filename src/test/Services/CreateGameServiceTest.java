package Services;

import Req_and_Result.CreateGameServiceReq;
import Req_and_Result.RegisterServiceRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameServiceTest extends testSetup{


    @Test
    void createAGame() {
        var result = this.registerUser("create_jack", "pass", "email");
        var gameResult = this.createAGame(((RegisterServiceRes) result).getAuthToken(), "gameName");
        assertEquals(gameResult.getMessage(), "success");
    }

    @Test
    void createAGameSameName() {
        var result = this.registerUser("create_jack2", "pass1", "email");
        var gameResult = this.createAGame("bad authToken", "gameName");
        assertEquals(gameResult.getMessage(), "unauthorized");
    }

    @Test
    void clearData() {
        this.cleardb();
    }

}