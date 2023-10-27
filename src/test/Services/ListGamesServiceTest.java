package Services;

import Req_and_Result.RegisterServiceRes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListGamesServiceTest extends testSetup {


    @Test
    void listGames() {
        var result = ((RegisterServiceRes)this.registerUser("list_jack", "pass", "email"));
        var gameResult = this.createAGame(((RegisterServiceRes) result).getAuthToken(), "gameName");
        assertEquals(gameResult.getMessage(), "success");

        var listGameRes = this.listGames(result.getAuthToken());
        assertEquals(listGameRes.getMessage(), "success");

    }

    @Test
    void listGamesBadAuth() {
        var result = ((RegisterServiceRes)this.registerUser("list_jack2", "pass1", "email"));
        var listGameRes = this.listGames("bad auth");
        assertEquals(listGameRes.getMessage(), "unauthorized");
    }

    @Test
    void clearData() {
        this.cleardb();
    }

}