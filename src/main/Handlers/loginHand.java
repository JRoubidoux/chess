package Handlers;

import Req_and_Result.*;
import Services.LoginService;
import Services.RegisterService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class loginHand extends generalHand {

    /*
    creates request object for the service, returns a response object generated from service
     */
    public Object handleLogin(Request req, Response res) {
        var body = turnToJava(req, Map.class);
        var loginReq = new LoginServiceReq(body);
        var loginServ = new LoginService();
        var loginRes = loginServ.login(loginReq);
        return turnToJson(res, loginRes);
    }

    private Object turnToJson(Response res, genRes serviceRes) {
        res.type("application/json");

        if (serviceRes.getMessage().equals("success")) {
            res.status(200);
            var map = new HashMap<String, String>();
            map.put("authToken", ((LoginServiceRes) serviceRes).getAuthToken());
            map.put("username", ((LoginServiceRes) serviceRes).getUsername());
            var body = new Gson().toJson(map);
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
