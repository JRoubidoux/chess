package Database;

import java.util.HashMap;

public class DataBaseRAM {

    private HashMap<Integer, HashMap<String, Object>> games;
    private HashMap<String, String> authTokens;
    private HashMap<String, HashMap<String, String>> users;

    public HashMap<Integer, HashMap<String, Object>> getGames() {
        return games;
    }

    public HashMap<String, String> getAuthTokens() {
        return authTokens;
    }

    public HashMap<String, HashMap<String, String>> getUsers() {
        return users;
    }
}
