package Handlers;

import Req_and_Result.genRes;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class generalHand {
    /*
    General class to support functionality of other handler classes.
     */

    public static <T> T turnToJava(Request req, Class<T> clazz) {

        var body = new Gson().fromJson(req.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    public Object getResponse(Response res, Integer status, genRes serviceRes) {
        res.status(status);
        var body = new Gson().toJson(Map.of("message", String.format("Error: " + serviceRes.getMessage())));
        res.body(body);
        return body;
    }
}
