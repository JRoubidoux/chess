package Handlers;

import Req_and_Result.ClearAppServiceReq;
import Req_and_Result.ClearAppServiceRes;
import Req_and_Result.RegisterServiceReq;
import Req_and_Result.RegisterServiceRes;
import Services.ClearAppService;
import Services.RegisterService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class registerUserHand {

    public Object handleReg(Request req, Response res) {
        var body = turnToJava(req, Map.class);
//        var clearReq = new ClearAppServiceReq();
//        var clearService = new ClearAppService();
//        var clearRes = clearService.clearApp(clearReq);
        // requestclass
        var regReq = new RegisterServiceReq(body);
        // serviceclass
        var regServ = new RegisterService();
        var regRes = regServ.register(regReq);
        // responseclass
        return turnToJson(res, regRes);


    }

    private static <T> T turnToJava(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    private static Object turnToJson(Response res, Object o) {
        res.type("application/json");

        if (((RegisterServiceRes) o).getMessage().equals("success")) {
            res.status(200);
            var map = new HashMap<String, String>();
            map.put("authToken", ((RegisterServiceRes) o).getAuthToken());
            map.put("username", ((RegisterServiceRes) o).getUsername());
            var body = new Gson().toJson(map);
            res.body(body);
            return body;
        }

        else if (((RegisterServiceRes) o).getMessage().equals("bad request")) {
            res.status(400);
            var body = new Gson().toJson(Map.of("message", "bad request"));
            res.body(body);
            return body;
        }
        else if (((RegisterServiceRes) o).getMessage().equals("already taken")) {
            res.status(403);
            var body = new Gson().toJson(Map.of("message", "already taken"));
            res.body(body);
            return body;
        }
        else {
            res.status(500);
            var body = new Gson().toJson(Map.of("message", ((RegisterServiceRes) o).getMessage()));
            res.body(body);
            return body;
        }
    }
}
