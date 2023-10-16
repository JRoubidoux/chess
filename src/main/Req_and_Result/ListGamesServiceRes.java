package Req_and_Result;

import java.util.ArrayList;
import java.util.HashMap;

public class ListGamesServiceRes {

    public ListGamesServiceRes() {
    }

    private ArrayList<HashMap<String, Object>> games;
    private String message;

    public ArrayList<HashMap<String, Object>> getGames() {
        return games;
    }

    public void setGames(ArrayList<HashMap<String, Object>> games) {
        this.games = games;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
