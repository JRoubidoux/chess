package Handlers;

import Req_and_Result.*;
import Services.JoinGameService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class JoinGameHand extends generalHand {

    /*
    creates request object for the service, returns a response object generated from service
     */
    public Object handleJoinGame(Request req, Response res) {
        String auth = req.headers("authorization");
        var body = turnToJava(req, Map.class);
        String color = null;
        if (body.size() > 1) {color = (String) body.get("playerColor");}
        var joinGameReq = new JoinGameServiceReq(auth, color, ((Double) body.get("gameID")).intValue());
        var joinGameServ = new JoinGameService();
        var joinGameRes = joinGameServ.joinGame(joinGameReq);
        return turnToJson(res, joinGameRes);
    }

    private Object turnToJson(Response res, genRes serviceRes) {
        res.type("application/json");

        if (serviceRes.getMessage().equals("success")) {
            res.status(200);
            return "{}";
        }
        else if (serviceRes.getMessage().equals("unauthorized")) {
            return this.getResponse(res, 401, serviceRes);
        }
        else if (serviceRes.getMessage().equals("bad request")) {
            return this.getResponse(res, 400, serviceRes);
        }
        else if (serviceRes.getMessage().equals("already taken")) {
            return this.getResponse(res, 403, serviceRes);
        }
        else {
            return this.getResponse(res, 500, serviceRes);
        }
    }
}
