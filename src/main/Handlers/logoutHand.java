package Handlers;

import Req_and_Result.LogoutServiceReq;
import Req_and_Result.genRes;
import Services.LogoutService;
import spark.Request;
import spark.Response;

public class logoutHand extends generalHand{

    /*
    creates request object for the service, returns a response object generated from service
     */
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
