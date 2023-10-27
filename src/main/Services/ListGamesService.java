package Services;

import DAOs.GameDAO;
import Models.Game;
import Req_and_Result.ListGamesServiceReq;
import Req_and_Result.ListGamesServiceRes;
import Req_and_Result.LogoutServiceRes;
import dataAccess.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that lists the current games on the server.
 */
public class ListGamesService {

    /**
     * Gives a list of all games.
     *
     * @param request A ListGamesServiceRequest object.
     * @return A ListGamesServiceResponse object.
     */
    public ListGamesServiceRes listGames(ListGamesServiceReq request) {
        var gameDao = new GameDAO();
        try {
            var games = gameDao.findAllGames();
            // { "games": [{"gameID": 1234, "whiteUsername":"", "blackUsername":"", "gameName:""} ]}
            var gamesInfo = new ArrayList<HashMap<String, Object>>();
            for (Game game: games) {
                var dict = new HashMap<String, Object>();
                dict.put("gameID", game.getGameID());
                setUserNames(dict, game);
                dict.put("gameName", game.getGameName());
                gamesInfo.add(dict);
            }
            var response = new ListGamesServiceRes();
            response.setGames(gamesInfo);
            response.setMessage("success");
            return response;
        }
        catch (DataAccessException e) {
            var response = new ListGamesServiceRes();
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public void setUserNames(HashMap<String, Object> dict, Game game) {
        setUserName(dict, game.getWhiteUsername(), "whiteUsername");
        setUserName(dict, game.getBlackUsername(), "blackUsername");
    }

    public void setUserName(HashMap<String, Object> dict, String username, String key) {
        if (username == null) { dict.put(key, ""); }
        else { dict.put(key, username);}
    }
}
