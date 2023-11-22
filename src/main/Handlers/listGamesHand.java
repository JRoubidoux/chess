package Handlers;

import Req_and_Result.ListGamesServiceReq;
import Req_and_Result.ListGamesServiceRes;
import Req_and_Result.genRes;
import Services.ListGamesService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Map;

public class listGamesHand extends generalHand {

    /*
    creates request object for the service, returns a response object generated from service
     */
    public Object handleListGames(Request req, Response res) {
        String auth = req.headers("authorization");
        var listGameReq = new ListGamesServiceReq(auth);
        var listGameServ = new ListGamesService();
        var listGameRes = listGameServ.listGames(listGameReq);
        return turnToJson(res, listGameRes);
    }

    private Object turnToJson(Response res, genRes serviceRes) {
        res.type("application/json");

        if (serviceRes.getMessage().equals("success")) {
            res.status(200);
            var body = new Gson().toJson(Map.of("games", ((ListGamesServiceRes) serviceRes).getGames()));
            res.body(body);
            return body;
        }

        else if (serviceRes.getMessage().equals("unauthorized")) {
            return this.getResponse(res, 401, serviceRes);
        }
        else {
            return this.getResponse(res, 500, serviceRes);
        }
    }
}
