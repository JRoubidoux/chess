package Handlers;

import Req_and_Result.*;
import Services.RegisterService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;

public class registerUserHand extends generalHand {

    /*
    creates request object for the service, returns a response object generated from service
     */
    public Object handleReg(Request req, Response res) {
        var body = turnToJava(req, Map.class);
        var regReq = new RegisterServiceReq(body);
        var regServ = new RegisterService();
        var regRes = regServ.register(regReq);
        return turnToJson(res, regRes);
    }
    private Object turnToJson(Response res, genRes serviceRes) {
        res.type("application/json");

        if (serviceRes.getMessage().equals("success")) {
            res.status(200);
            var map = new HashMap<String, String>();
            map.put("authToken", ((RegisterServiceRes) serviceRes).getAuthToken());
            map.put("username", ((RegisterServiceRes) serviceRes).getUsername());
            var body = new Gson().toJson(map);
            res.body(body);
            return body;
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
