package Database;

import Models.AuthToken;
import Models.Game;
import Models.User;
import java.util.ArrayList;

public class DatabaseSQL implements Database {

    private static dataAccess.Database db = new dataAccess.Database();
    @Override
    public void writeGame(Game game) {

    }

    @Override
    public void writeAuth(AuthToken authToken) {

    }

    @Override
    public void writeUser(User user) {

    }

    @Override
    public Game readGame(Integer gameID) {
        return null;
    }

    @Override
    public User readUser(String username) {
        return null;
    }

    @Override
    public AuthToken readAuth(String authToken) {
        return null;
    }

    @Override
    public void removeGame(Integer gameID) {

    }

    @Override
    public void removeAuth(String authToken) {

    }

    @Override
    public void removeUser(String username) {

    }

    @Override
    public void updateGame(Game game) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public ArrayList<Game> readAllGames() {
        return null;
    }

    @Override
    public void clearGames() {

    }

    @Override
    public void clearUsers() {

    }

    @Override
    public void clearAuth() {

    }

    @Override
    public void clearDB() {

    }

    @Override
    public boolean noGamesInDB() {
        return false;
    }

    @Override
    public boolean noUsersInDB() {
        return false;
    }

    @Override
    public boolean noAuthInDB() {
        return false;
    }
}
