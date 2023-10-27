package Services;

import Req_and_Result.CreateGameServiceReq;
import Req_and_Result.RegisterServiceRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameServiceTest extends testSetup{

    @BeforeEach
    void clearData() {
        this.cleardb();
    }

    @Test
    void createAGame() {
        var result = this.registerUser("jack", "pass", "email");
        var gameResult = this.createAGame(((RegisterServiceRes) result).getAuthToken(), "gameName");
        assertEquals(game);
    }

}