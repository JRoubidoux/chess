package Handlers;
import Services.ClearAppService;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;
import Req_and_Result.ClearAppServiceReq;
import Req_and_Result.ClearAppServiceRes;

import java.util.Map;

public class clearAppHand {

    public Object handleClear(Request req, Response res) throws DataAccessException {
        // var body = turnToJava(req, Map.class);
        var clearReq = new ClearAppServiceReq();
        var clearService = new ClearAppService();
        var clearRes = clearService.clearApp(clearReq);
        return turnToJson(res, clearRes);


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
        if (((ClearAppServiceRes) o).getMessage() != null) {
            res.status(500);
            var body = new Gson().toJson(Map.of("message", ((ClearAppServiceRes) o).getMessage()));
            res.body(body);
            return body;
        }
        else {
            res.status(200);
            return "{}";
        }
    }
}
