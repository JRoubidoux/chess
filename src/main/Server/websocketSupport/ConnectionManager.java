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
        var connection = new Connection(authToken, session);
        if (connections.containsKey((Integer) gameID)) {
            connections.get((Integer) gameID).put(authToken, connection);
        }
        else {
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
    public void joinPlayerResponse(int gameID, String authToken, Notification notification, LoadGame loadGame) throws IOException {
        var removeList = new ArrayList<Connection>();

        // send notification
        for (var c : connections.get((Integer) gameID).values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(authToken)) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }
        //TODO: This may cause a bug.
        for (var c : removeList) {
            connections.get((Integer) gameID).remove(c.getAuthToken());
            if (connections.get((Integer) gameID).isEmpty()) {
                connections.remove((Integer) gameID);
            }
        }

        connections.get(gameID).get(authToken).send(new Gson().toJson(loadGame));
    }

    public void broadcast(String excludeVisitorName, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
//            if (c.session.isOpen()) {
//                if (!c.visitorName.equals(excludeVisitorName)) {
//                    c.send(notification.toString());
//                }
//            } else {
//                removeList.add(c);
//            }
        }

        // Clean up any connections that were left open.
//        for (var c : removeList) {
//            connections.remove(c.visitorName);
//        }
    }
}