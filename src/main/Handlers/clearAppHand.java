package Handlers;
import Req_and_Result.genRes;
import Services.ClearAppService;
import spark.Request;
import spark.Response;
import Req_and_Result.ClearAppServiceReq;

public class clearAppHand extends generalHand {

    /*
    creates request object for the service, returns a response object generated from service
     */
    public Object handleClear(Request req, Response res) {
        var clearReq = new ClearAppServiceReq();
        var clearService = new ClearAppService();
        var clearRes = clearService.clearApp(clearReq);
        return turnToJson(res, clearRes);


    }

    private Object turnToJson(Response res, genRes serviceRes) {
        res.type("application/json");
        if (serviceRes.getMessage() != null) {
            return this.getResponse(res, 500, serviceRes);
        }
        else {
            res.status(200);
            return "{}";
        }
    }
}
