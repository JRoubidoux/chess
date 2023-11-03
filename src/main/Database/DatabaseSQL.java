package Database;

import Models.AuthToken;
import Models.Game;
import Models.User;
import Req_and_Result.LoginServiceRes;
import Req_and_Result.genRes;
import com.google.gson.Gson;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseSQL implements Database {
    @Override
    public void writeGame(Game game) {

    }

    private Object turnToJson(Game game) {

        var body = new Gson().toJson(game);
        return body;
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
