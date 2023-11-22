package Handlers;

import Req_and_Result.*;
import Services.CreateGameService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Map;

public class createGameHand extends generalHand {

    /*
    creates request object for the service, returns a response object generated from service
     */
    public Object handleCreateGame(Request req, Response res) {
        String auth = req.headers("authorization");
        var body = turnToJava(req, Map.class);
        var createGameReq = new CreateGameServiceReq(auth, (String) body.get("gameName"));
        var createGameServ = new CreateGameService();
        var createGameRes = createGameServ.createGame(createGameReq);
        return turnToJson(res, createGameRes);
    }

    private Object turnToJson(Response res, genRes serviceRes) {
        res.type("application/json");

        if (serviceRes.getMessage().equals("success")) {
            res.status(200);
            var body = new Gson().toJson(Map.of("gameID", ((CreateGameServiceRes) serviceRes).getGameID()));
            res.body(body);
            return body;
        }

        else if (serviceRes.getMessage().equals("unauthorized")) {
            return this.getResponse(res, 401, serviceRes);
        }
        else if (serviceRes.getMessage().equals("bad request")) {
            return this.getResponse(res, 400, serviceRes);
        }
        else {
            return this.getResponse(res, 500, serviceRes);
        }
    }
}
