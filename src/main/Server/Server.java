package Server;
import Handlers.clearAppHand;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.ArrayList;
import java.util.Map;

public class Server {

    private ArrayList<String> names = new ArrayList<>();

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);

        // Register a directory for hosting static files
        Spark.externalStaticFileLocation("src/main/web");

        // Register handlers for each endpoint using the method reference syntax
        Spark.post("/name/:name", this::addName);
        Spark.get("/name", this::listNames);
        Spark.delete("/name/:name", this::deleteName);

        // Needed Methods
        Spark.delete("/db", this::clearDB);

    }

    private Object clearDB(Request req, Response res) throws DataAccessException {
        // Use a handler to manage the request.
        var handler = new clearAppHand();
        return handler.handleClear(req, res);
    }

    private Object addName(Request req, Response res) {
        names.add(req.params(":name"));
        return listNames(req, res);
    }

    private Object listNames(Request req, Response res) {
        res.type("application/json");
        return new Gson().toJson(Map.of("name", names));
    }

    private Object deleteName(Request req, Response res) {
        names.remove(req.params(":name"));
        return listNames(req, res);
    }
}
