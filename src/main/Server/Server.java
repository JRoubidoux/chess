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
        // Needed Methods
        Spark.delete("/db", this::clearDB);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.get("/game", this::loginUser);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

    }

    private Object joinGame(Request req, Response res) {
        return null;
    }

    private Object createGame(Request req, Response res) {
        return null;
    }

    private Object clearDB(Request req, Response res) {
        // Use a handler to manage the request.
        var handler = new clearAppHand();
        return handler.handleClear(req, res);
    }

    private Object registerUser(Request req, Response res) {
        return null;
    }

    private Object loginUser(Request req, Response res) {
        return null;
    }

    private Object logoutUser(Request req, Response res) {
        return null;
    }
}
