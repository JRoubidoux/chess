package Handlers;

import Req_and_Result.*;
import Services.CreateGameService;
import Services.JoinGameService;
import chess.ChessGame;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Map;

public class JoinGameHand extends generalHand {

    public Object handleJoinGame(Request req, Response res) {
        String auth = req.headers("authorization");
        var body = turnToJava(req, Map.class);
        String argument = null;
        if (body.size() > 1) {argument = (String) body.get("playerColor");}
        var joinGameReq = new JoinGameServiceReq(auth, argument, ((Double) body.get("gameID")).intValue());
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
