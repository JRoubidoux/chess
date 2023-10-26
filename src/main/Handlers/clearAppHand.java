package Handlers;
import Req_and_Result.genRes;
import Services.ClearAppService;
import spark.Request;
import spark.Response;
import Req_and_Result.ClearAppServiceReq;

public class clearAppHand extends generalHand {

    public Object handleClear(Request req, Response res) {
        // var body = turnToJava(req, Map.class);
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
