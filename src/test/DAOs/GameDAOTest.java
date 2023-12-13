package DAOs;

import Models.Game;
import chess.ChessGame;
import chess.ChessGameImp;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {

    private Game getClassicGame(String wU, String bU, String gN, int id) {
        var game = new Game();
        game.setGameName(gN);
        game.setBlackUsername(bU);
        game.setWhiteUsername(wU);
        game.setGameID(id);
        game.setGame(new ChessGameImp());
        return game;
    }

    @BeforeEach
    public void clearGames() {
        var gameDAO = new GameDAO();
        try {
            gameDAO.ClearGames();
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void insertNewGamePos() {
        var gameDAO = new GameDAO();

        try {
            gameDAO.insertNewGame(getClassicGame("white", "black", "gameName", 1));
            var game = gameDAO.findGame(1);
            assertEquals(game.getBlackUsername(), "black");
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }

    }

    @Test
    void insertNewGameNeg() {
        var gameDAO = new GameDAO();

        try {
            gameDAO.insertNewGame(getClassicGame("white", "black", "gameName", 1));
            var game = gameDAO.findGame(1);
            assertNotEquals(game.getBlackUsername(), "white");
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void findGamePos() {
        var gameDAO = new GameDAO();

        try {
            gameDAO.insertNewGame(getClassicGame("white", "black", "gameName", 1));
            var game = gameDAO.findGame(1);
            assertNotNull(game);
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void findGameNeg() {
        var gameDAO = new GameDAO();

        try {
            gameDAO.insertNewGame(getClassicGame("white", "black", "gameName", 1));
            var game = gameDAO.findGame(2);
            assertNull(game);
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            assertEquals(e.getMessage(), "No game exists with this ID.");
        }
    }

    @Test
    void findAllGamesPos() {
        var gameDAO = new GameDAO();

        try {
            gameDAO.insertNewGame(getClassicGame("white", "black", "gameName", 1));
            gameDAO.insertNewGame(getClassicGame("white2", "black2", "gameName2", 2));
            var games = gameDAO.findAllGames();
            assertEquals(games.size(), 2);
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void findAllGamesNeg() {
        var gameDAO = new GameDAO();

        try {
            gameDAO.insertNewGame(getClassicGame("white", "black", "gameName", 1));
            gameDAO.insertNewGame(getClassicGame("white2", "black2", "gameName2", 2));
            var games = gameDAO.findAllGames();
            assertNotEquals(games.size(), 4);
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void claimSpotInGamePos() {
        var gameDAO = new GameDAO();

        try {
            var game1 = getClassicGame(null, "black", "gameName", 1);
            gameDAO.insertNewGame(game1);
            gameDAO.ClaimSpotInGame("jacob", 1, ChessGame.TeamColor.WHITE);
            var game = gameDAO.findGame(1);
            assertEquals(game.getWhiteUsername(), "jacob");
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void claimSpotInGameNeg() {
        var gameDAO = new GameDAO();

        try {
            var game1 = getClassicGame(null, "black", "gameName", 1);
            gameDAO.insertNewGame(game1);
            gameDAO.ClaimSpotInGame("jacob", 1, ChessGame.TeamColor.WHITE);
            var game = gameDAO.findGame(1);
            assertNotEquals(game.getWhiteUsername(), "white");
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void updateGamePos() {
        var gameDAO = new GameDAO();

        try {
            var game1 = getClassicGame("white", "black", "gameName", 1);
            gameDAO.insertNewGame(game1);
            game1.setWhiteUsername("new white");
            gameDAO.UpdateGame(game1);
            var game = gameDAO.findGame(1);
            assertEquals(game.getWhiteUsername(), "new white");
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void updateGameNeg() {
        var gameDAO = new GameDAO();

        try {
            var game1 = getClassicGame("white", "black", "gameName", 1);
            gameDAO.insertNewGame(game1);
            game1.setWhiteUsername("new white");
            gameDAO.UpdateGame(game1);
            var game = gameDAO.findGame(1);
            assertNotEquals(game.getWhiteUsername(), "white");
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void removeGamePos() {
        var gameDAO = new GameDAO();

        try {
            var game1 = getClassicGame("white", "black", "gameName", 1);
            var ID = gameDAO.insertNewGame(game1);
            gameDAO.RemoveGame(1);
            var game = gameDAO.findGame(1);
            assertNull(game);
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            assertEquals(e.getMessage(), "No game exists with this ID.");
        }
    }

    @Test
    void removeGameNeg() {
        var gameDAO = new GameDAO();

        try {
            var game1 = getClassicGame("white", "black", "gameName", 1);
            gameDAO.insertNewGame(game1);
            gameDAO.RemoveGame(2);
            var game = gameDAO.findGame(1);
        }
        catch (DataAccessException e) {
            assertEquals(e.getMessage(), "No game corresponds to this gameID");

        }
    }

    @Test
    void clearGamesPos() {
        var gameDAO = new GameDAO();

        try {
            var game1 = getClassicGame("white", "black", "gameName", 1);
            gameDAO.insertNewGame(game1);
            gameDAO.ClearGames();
            var game = gameDAO.findGame(1);
            assertNull(game);

        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void gameInDBPos() {
        var gameDAO = new GameDAO();

        try {
            var game1 = getClassicGame("white", "black", "gameName", 1);
            gameDAO.insertNewGame(game1);
            assertTrue(gameDAO.gameInDB(1));
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void gameInDBNeg() {
        var gameDAO = new GameDAO();

        try {
            var game1 = getClassicGame("white", "black", "gameName", 1);
            gameDAO.insertNewGame(game1);
            gameDAO.RemoveGame(1);
            assertFalse(gameDAO.gameInDB(1));
            gameDAO.ClearGames();
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }
}