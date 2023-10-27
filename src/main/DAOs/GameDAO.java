package DAOs;

import Database.DataBaseRAM;
import Database.Database;
import Models.Game;
import chess.ChessGame;
import chess.ChessGameImp;
import dataAccess.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that represents the Data Access Object that deals with games.
 */
public class GameDAO {


    private Database db = new DataBaseRAM();


    /**
     * Takes a given game and puts the information into the Database.
     *
     * @param game An existing game that hasn't been put into the DB.
     * @throws DataAccessException If an error occurs trying to insert a game.
     */
    public void insertNewGame(Game game) throws DataAccessException {
        if (!gameInDB(game.getGameID())) {
            db.writeGame(game);
        }
        else {
            throw new DataAccessException("A game with this ID already exists.");
        }
    }

    /**
     * Given a gameID this function returns a given game name in our DB.
     *
     * @param gameID A unique integer that corresponds to a given game
     * @return The Game object with the corresponding game ID.
     * @throws DataAccessException If an error occurs trying to find a game.
     */
    public Game findGame(int gameID) throws DataAccessException {
        if (db.readGame(gameID) == null) {
            throw new DataAccessException("No game exists with this ID.");
        }
        else {
            return db.readGame(gameID);
        }
    }

    /**
     * Returns all game names of currently existing games
     *
     * @return Returns an array list of Game objects, each of which is a game name.
     * @throws DataAccessException If an error occurs trying to find all games.
     */
    public ArrayList<Game> findAllGames() throws DataAccessException {
        var games = db.readAllGames();

        if (games.isEmpty()) {
            throw new DataAccessException("There are no games in the DB.");
        }
        else {
            return games;
        }
    }

    /**
     * Takes in a username and stores it as white or black
     *
     * @param username A String to store in the game.
     * @throws DataAccessException If an error occurs trying to claim spot in a game.
     */
    public void ClaimSpotInGame(String username, Integer gameID, ChessGame.TeamColor color) throws DataAccessException {
        var gameInfo = db.readGame(gameID);
        if (gameInfo == null) {
            throw new DataAccessException("Trying to join a game that doesn't exist");
        }
        else if (color == ChessGame.TeamColor.WHITE) {
            if (gameInfo.getWhiteUsername() == null) {
                // make user white
                gameInfo.setWhiteUsername(username);
                db.updateGame(gameInfo);
            }
            else {
                // throw DAE
                throw new DataAccessException("White's taken.");
            }
        }
        else {
            if (gameInfo.getBlackUsername() == null) {
                // make user black
                gameInfo.setBlackUsername(username);
                db.updateGame(gameInfo);
            }
            else {
                // throw DAE
                throw new DataAccessException("Black's taken.");
            }
        }
    }

    /**
     * Given a game, it will update the chessGame string in the database
     * to reflect the current status of the game.
     *
     * @param chessGame A Game object.
     * @throws DataAccessException If an error occurs trying to update a game.
     */
    public void UpdateGame(Game chessGame) throws DataAccessException {
        db.updateGame(chessGame);
    }

    /**
     * Removes a game from the DB.
     *
     * @param gameID A game ID
     * @throws DataAccessException If an error occurs trying to delete a game.
     */
    public void RemoveGame(Integer gameID) throws DataAccessException {
        if (!gameInDB(gameID)) {
            throw new DataAccessException("No game corresponds to this gameID");
        }
        else {
            db.removeGame(gameID);
        }
    }


    /**
     * Removes all games from the DB.
     * @throws DataAccessException If an error occurs trying to delete all games.
     */
    public void ClearGames() throws DataAccessException {
        db.clearGames();
    }

    public boolean gameInDB(Integer gameID) {
        if (noGamesinDB()) {return false;}
        else { return db.readGame(gameID) != null; }
    }

    public boolean noGamesinDB() {
        return db.noGamesInDB();
    }


}
