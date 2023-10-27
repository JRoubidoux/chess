package Handlers;

import Req_and_Result.LoginServiceReq;
import Req_and_Result.LoginServiceRes;
import Req_and_Result.LogoutServiceReq;
import Req_and_Result.genRes;
import Services.LoginService;
import Services.LogoutService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class logoutHand extends generalHand{

    public Object handleLogout(Request req, Response res) {
        String auth = req.headers("authorization");
        var logoutReq = new LogoutServiceReq(auth);
        var logoutServ = new LogoutService();
        var logoutRes = logoutServ.logout(logoutReq);
        return turnToJson(res, logoutRes);
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
        else {
            return this.getResponse(res, 500, serviceRes);
        }
    }
}
