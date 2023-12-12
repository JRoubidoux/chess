package Server;
import Handlers.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Request;
import spark.Response;
import spark.Spark;

@WebSocket
public class Server {
    private final WebSocketHandler webSocketHandler = new WebSocketHandler();

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        // Specify the port you want the server to listen on
        Spark.port(8080);
        Spark.webSocket("/connect", webSocketHandler);
        // Register a directory for hosting static files
        Spark.externalStaticFileLocation("src/main/web");

        // Register handlers for each endpoint using the method reference syntax
        Spark.delete("/db", this::clearDB);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);



    }

    private Object listGames(Request req, Response res) {
        var handle = new listGamesHand();
        return handle.handleListGames(req, res);
    }

    private Object joinGame(Request req, Response res) {
        var handle = new JoinGameHand();
        return handle.handleJoinGame(req, res);
    }

    private Object createGame(Request req, Response res) {
        var handle = new createGameHand();
        return handle.handleCreateGame(req, res);
    }

    private Object clearDB(Request req, Response res) {
        var handler = new clearAppHand();
        return handler.handleClear(req, res);
    }

    private Object registerUser(Request req, Response res) {
        var handle = new registerUserHand();
        return handle.handleReg(req, res);
    }

    private Object loginUser(Request req, Response res) {
        var handle = new loginHand();
        return handle.handleLogin(req, res);
    }

    private Object logoutUser(Request req, Response res) {
        var handle = new logoutHand();
        return handle.handleLogout(req, res);
    }

}
