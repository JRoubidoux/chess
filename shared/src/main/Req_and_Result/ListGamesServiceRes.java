package Req_and_Result;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Response Class to support the ListGames Service class
 */
public class ListGamesServiceRes extends genRes {

    /**
     * Class Constructor
     */
    public ListGamesServiceRes() {
    }

    private ArrayList<HashMap<String, Object>> games;

    public ArrayList<HashMap<String, Object>> getGames() {
        return games;
    }

    public void setGames(ArrayList<HashMap<String, Object>> games) {
        this.games = games;
    }

}
