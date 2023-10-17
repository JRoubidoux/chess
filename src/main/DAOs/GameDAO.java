package DAOs;

import Models.Game;
import dataAccess.DataAccessException;

import java.util.ArrayList;

/**
 * Class that represents the Data Access Object that deals with games.
 */
public class GameDAO {

    /**
     * Takes a given game and puts the information into the Database.
     *
     * @param game An existing game that hasn't been put into the DB.
     */
    public void insertNewGame(Game game) throws DataAccessException {}

    /**
     * Given a gameID this function returns a given game name in our DB.
     *
     * @param gameID A unique integer that corresponds to a given game
     * @return The Game object with the corresponding game ID.
     */
    public Game findGame(int gameID) throws DataAccessException {return null;}

    /**
     * Returns all game names of currently existing games
     *
     * @return Returns an array list of Game objects, each of which is a game name.
     */
    public ArrayList<Game> findAllGames() throws DataAccessException {return null;}

    /**
     * Takes in a username and stores it as white or black
     *
     * @param username A String to store in the game.
     */
    public void ClaimSpotInGame(String username) throws DataAccessException {}

    /**
     * Given a game, it will update the chessGame string in the database
     * to reflect the current status of the game.
     *
     * @param chessGame A Game object.
     */
    public void UpdateGame(Game chessGame) throws DataAccessException {}

    /**
     * Removes a game from the DB.
     *
     * @param chessGame A game Object
     */
    public void RemoveGame(Game chessGame) throws DataAccessException {}

    /**
     * Removes all games from the DB.
     */
    public void ClearGames() throws DataAccessException {}
}
