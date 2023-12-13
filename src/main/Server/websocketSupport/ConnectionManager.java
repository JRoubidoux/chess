package Server.websocketSupport;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String authToken, Session session) {

        if (connections.containsKey((Integer) gameID)) {
            if (!connections.get((Integer) gameID).containsKey(authToken)) {
                var connection = new Connection(authToken, session);
                connections.get((Integer) gameID).put(authToken, connection);
            }
        }
        else {
            var connection = new Connection(authToken, session);
            var newMap = new ConcurrentHashMap<String, Connection>();
            newMap.put(authToken, connection);
            connections.put(gameID, newMap);
        }
    }

    public void remove(int gameID, String authToken) {
        connections.get((Integer) gameID).remove(authToken);
        if (connections.get((Integer) gameID).isEmpty()) {
            connections.remove((Integer) gameID);
        }
    }
    //gameID, authToken, notification, loadGame

}