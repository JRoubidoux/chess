package Services;

import DAOs.GameDAO;
import Models.Game;
import Req_and_Result.ClearAppServiceReq;
import chess.ChessGameImp;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearAppServiceTest {

    @Test
    void clearApp() {
        var gameDao = new GameDAO();

        var game = new Game();
        game.setGameName("gameName");
        game.setWhiteUsername("jack");
        game.setBlackUsername("jill");
        game.setGame(new ChessGameImp());

        try {
            gameDao.insertNewGame(game);
        }
        catch (DataAccessException e) {
            System.out.println("Server error.");
        }

        assertTrue(gameDao.gameInDB(1), "No game was written into the DB.");

        var clearReq = new ClearAppServiceReq();
        var clearService = new ClearAppService();
        var clearRes = clearService.clearApp(clearReq);

        assertFalse(gameDao.gameInDB(82), "The clear DB function didn't work properly.");
    }
}