package DAOs;

import Database.DataBaseRAM;
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


    private static HashMap<Integer, HashMap<String, Object>> games;


    /**
     * Takes a given game and puts the information into the Database.
     *
     * @param game An existing game that hasn't been put into the DB.
     * @throws DataAccessException If an error occurs trying to insert a game.
     */
    public void insertNewGame(Game game) throws DataAccessException {
        if (games.get(game.getGameID()) != null) {
            var dictionary = new HashMap<String, Object>();
            games.put(game.getGameID(), dictionary);
            games.get(game.getGameID()).put("whiteUsername", game.getWhiteUsername());
            games.get(game.getGameID()).put("blackUsername", game.getBlackUsername());
            games.get(game.getGameID()).put("gameName", game.getGameName());
            games.get(game.getGameID()).put("game", game.getGame());
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
        if (games.get(gameID) == null) {
            throw new DataAccessException("No game exists with this ID.");
        }
        else {
            var gameFromDict = games.get(gameID);
            var game = new Game();
            game.setGameID(gameID);
            game.setGame((ChessGameImp) gameFromDict.get("game"));
            game.setGameName((String) gameFromDict.get("gameName"));
            game.setBlackUsername((String) gameFromDict.get("blackUsername"));
            game.setWhiteUsername((String) gameFromDict.get("whiteUsername"));
            return game;
        }
    }

    /**
     * Returns all game names of currently existing games
     *
     * @return Returns an array list of Game objects, each of which is a game name.
     * @throws DataAccessException If an error occurs trying to find all games.
     */
    public ArrayList<Game> findAllGames() throws DataAccessException {
        if (games.isEmpty()) {
            throw new DataAccessException("There are no games in the DB.");
        }
        else {
            var allGames = new ArrayList<Game>();
            for (Integer gameID : games.keySet()) {
                allGames.add(findGame(gameID));
            }
            return allGames;
        }
    }

    /**
     * Takes in a username and stores it as white or black
     *
     * @param username A String to store in the game.
     * @throws DataAccessException If an error occurs trying to claim spot in a game.
     */
    public void ClaimSpotInGame(String username, Integer gameID, ChessGame.TeamColor color) throws DataAccessException {
        var gameInfo = games.get(gameID);
        if (gameInfo == null) {
            throw new DataAccessException("Trying to join a game that doesn't exist");
        }
        else if (gameInfo.get("whiteUsername") != null) {
            if (color == ChessGame.TeamColor.WHITE) {
                throw new DataAccessException("White is already taken.");
            }
            else {
                games.get(gameID).put("whiteUsername", username);
            }
        }
        if (gameInfo.get("blackUsername") != null) {
            if (color == ChessGame.TeamColor.BLACK) {
                throw new DataAccessException("Black is already taken.");
            }
            else {
                games.get(gameID).put("blackUsername", username);
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
    public void UpdateGame(Game chessGame) throws DataAccessException {}

    /**
     * Removes a game from the DB.
     *
     * @param gameID A game ID
     * @throws DataAccessException If an error occurs trying to delete a game.
     */
    public void RemoveGame(Integer gameID) throws DataAccessException {
        if (games.get(gameID) == null) {
            throw new DataAccessException("No game corresponds to this gameID");
        }
        else {
            games.remove(gameID);
        }
    }


    /**
     * Removes all games from the DB.
     * @throws DataAccessException If an error occurs trying to delete all games.
     */
    public void ClearGames() throws DataAccessException {
        if (games != null) { games.clear(); }
    }
}
