package Database;

import Models.AuthToken;
import Models.Game;
import Models.User;
import chess.ChessGameImp;
import dataAccess.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseRAM implements Database{

    private HashMap<Integer, HashMap<String, Object>> games = new HashMap<>();
    private HashMap<String, String> authTokens = new HashMap<>();
    private HashMap<String, HashMap<String, String>> users = new HashMap<>();

    @Override
    public void writeGame(Game game) {
        games.put(game.getGameID(), new HashMap<String, Object>());
        games.get(game.getGameID()).put("whiteUsername", game.getWhiteUsername());
        games.get(game.getGameID()).put("blackUsername", game.getBlackUsername());
        games.get(game.getGameID()).put("gameName", game.getGameName());
        games.get(game.getGameID()).put("game", game.getGame());

    }

    @Override
    public void writeAuth(AuthToken authToken) {
        authTokens.put(authToken.getAuthToken(), authToken.getUsername());

    }

    @Override
    public void writeUser(User user) {
        users.put(user.getUsername(), new HashMap<String, String>());
        users.get(user.getUsername()).put("password", user.getPassword());
        users.get(user.getUsername()).put("email", user.getEmail());
    }

    @Override
    public Game readGame(Integer gameID) {
        if (games == null) {return null;}
        var gameFromDict = games.get(gameID);
        var game = new Game();
        game.setGameID(gameID);
        game.setGame((ChessGameImp) gameFromDict.get("game"));
        game.setGameName((String) gameFromDict.get("gameName"));
        game.setBlackUsername((String) gameFromDict.get("blackUsername"));
        game.setWhiteUsername((String) gameFromDict.get("whiteUsername"));
        return game;
    }

    @Override
    public User readUser(String username) {
        if (users == null) {return null;}
        return new User(username, users.get(username).get("password"), users.get(username).get("email"));
    }

    @Override
    public AuthToken readAuth(String authToken) {
        if (authTokens == null) {return null;}
        return new AuthToken(authToken, authTokens.get(authToken));
    }

    @Override
    public void removeGame(Integer gameID) {
        games.remove(gameID);
    }

    @Override
    public void removeAuth(String authToken) {
        authTokens.remove(authToken);
    }

    @Override
    public void removeUser(String username) {
        users.remove(username);

    }

    @Override
    public void updateGame(Game game) {
        games.get(game.getGameID()).put("whiteUsername", game.getWhiteUsername());
        games.get(game.getGameID()).put("blackUsername", game.getBlackUsername());
        games.get(game.getGameID()).put("gameName", game.getGameName());
        games.get(game.getGameID()).put("game", game.getGame());
    }


    @Override
    public void updateUser(User user) {
        users.get(user.getUsername()).put("password", user.getPassword());
        users.get(user.getUsername()).put("email", user.getEmail());
    }

    @Override
    public ArrayList<Game> readAllGames() {
        var allGames = new ArrayList<Game>();
        for (Integer gameID : games.keySet()) {
            allGames.add(readGame(gameID));
        }
        return allGames;
    }

    @Override
    public void clearGames() {
        if (games != null) {games.clear();}
    }

    @Override
    public void clearUsers() {
        if (users != null) {users.clear();}
    }

    @Override
    public void clearAuth() {
        if (authTokens != null) {authTokens.clear();}
    }

    @Override
    public void clearDB() {
        clearGames();
        clearUsers();
        clearAuth();
    }

    @Override
    public boolean noGamesInDB() {
        return games.isEmpty();
    }

    @Override
    public boolean noUsersInDB() {
        return users.isEmpty();
    }

    @Override
    public boolean noAuthInDB() {
        return authTokens.isEmpty();
    }


}
