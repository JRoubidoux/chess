import com.google.gson.Gson;
import com.google.gson.*;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.lang.*;

public class serverFacade {

    private final String serverUrl;

    public serverFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public JsonObject makeRequest(boolean body, HashMap<String, ?> map, String path, String method, boolean header, String authToken) {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (header) {
                writeHeader(authToken, http);
            }
            if (body) {
                writeBody(map, http);
            }

            http.connect();
            var status = http.getResponseCode();
            if (status == 200) {
                var temp = readBody(http);
                temp.add("status", new JsonPrimitive(status));
                http.disconnect();
                return temp;
            }
            else {
                var temp = new JsonObject();
                temp.add("status", new JsonPrimitive(status));
                http.disconnect();
                return temp;
            }

        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.setRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void writeHeader(String authToken, HttpURLConnection http) throws IOException {
        if (authToken != null) {
            http.setRequestProperty("Authorization", authToken);
        }
    }

    private static JsonObject readBody(HttpURLConnection http) throws IOException {
        JsonObject response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                try (BufferedReader bReader = new BufferedReader(reader)) {
                    StringBuilder respStringBuilder = new StringBuilder();
                    String line;
                    while ((line = bReader.readLine()) != null) {
                        respStringBuilder.append(line);
                    }

                    response = new Gson().fromJson(respStringBuilder.toString(), JsonObject.class);
                }
            }
        }
        return response;
    }

    public void clear() {
        var path = "/db";
        String method = "DELETE";

        var jsonObject = makeRequest(false, null, path, method, false, null);

        if (jsonObject.isJsonNull()) {
            System.out.println("Other error. Try again. \n");
        }
        else {
            var status = jsonObject.get("status").getAsInt();
            if (status == 200) {
                System.out.print("DB is cleared.\n");
            }
            else if (status == 401){
                System.out.println("Unauthorized. Try again. \n");
            }
            else {
                System.out.println("Other error. Try again. \n");
            }
        }
    }

    public String login(String[] params) {
        var path = "/session";
        String method = "POST";

        var mapOfParams = new HashMap<String, String>();
        for (int i = 0; i < params.length; i++) {
            if (i == 0) {
                mapOfParams.put("username", params[i]);
            }
            else if (i == 1) {
                mapOfParams.put("password", params[i]);
            }
        }
        var jsonObject = makeRequest(true, mapOfParams, path, method, false, null);

        if (jsonObject.isJsonNull()) {
            return null;
        }
        else {
            var status = jsonObject.get("status").getAsInt();
            if (status == 200) {
                var username = jsonObject.get("username").getAsString();
                var authToken = jsonObject.get("authToken").getAsString();
                System.out.printf("Logged in as %s.\n", username);
                return authToken;
            }
            else if (status == 401){
                System.out.println("Unauthorized. Try again. \n");
                return null;
            }
            else {
                System.out.println("Other error. Try again. \n");
                return null;
            }
        }
    }

    public boolean logout(String authToken) {
        var path = "/session";
        String method = "DELETE";

        var jsonObject = makeRequest(false, null, path, method, true, authToken);

        if (jsonObject.isJsonNull()) {
            return false;
        }
        else {
            var status = jsonObject.get("status").getAsInt();
            if (status == 200) {
                System.out.print("Logged out.\n");
                return true;
            }
            else if (status == 401){
                System.out.println("Unauthorized. Try again. \n");
                return false;
            }
            else {
                System.out.println("Other error. Try again. \n");
                return false;
            }
        }
    }

    public String register(String[] params) {
        var path = "/user";
        String method = "POST";
        var mapOfParams = new HashMap<String, String>();
        for (int i = 0; i < params.length; i++) {
            if (i == 0) {
                mapOfParams.put("username", params[i]);
            }
            else if (i == 1) {
                mapOfParams.put("password", params[i]);
            }
            else {
                mapOfParams.put("email", params[i]);
            }
        }
        var jsonObject = makeRequest(true, mapOfParams, path, method, false, null);

        if (jsonObject == null) {
            return null;
        }
        else {
            var status = jsonObject.get("status").getAsInt();
            if (status == 200) {
                var username = jsonObject.get("username").getAsString();
                var authToken = jsonObject.get("authToken").getAsString();
                System.out.printf("Logged in as %s. \n", username);
                return authToken;
            }
            else if (status == 400){
                System.out.println("Bad request. Try again. \n");
                return null;
            }
            else if (status == 403) {
                System.out.println("Already taken. Try again. \n");
                return null;
            }
            else {
                System.out.println("Other error. Try again. \n");
                return null;
            }
        }
    }

    public String create(String[] params, String authToken) {
        var path = "/game";
        String method = "POST";
        var mapOfParams = new HashMap<String, String>();
        for (int i = 0; i < params.length; i++) {
            if (i == 0) {
                mapOfParams.put("gameName", params[i]);
            }
        }
        var jsonObject = makeRequest(true, mapOfParams, path, method, true, authToken);

        if (jsonObject == null) {
            return null;
        }
        else {
            var status = jsonObject.get("status").getAsInt();
            if (status == 200) {
                var gameID = jsonObject.get("gameID").getAsString();
                System.out.printf("Game: %s was created. Game ID: %s.\n", mapOfParams.get("gameName"), gameID);
                return gameID;
            }
            else if (status == 400){
                System.out.println("Bad request. Try again. \n");
                return null;
            }
            else if (status == 401) {
                System.out.println("Unauthorized. Try again. \n");
                return null;
            }
            else {
                System.out.println("Other error. Try again. \n");
                return null;
            }
        }
    }

    public String list(String authToken) {
        var path = "/game";
        String method = "GET";

        var jsonObject = makeRequest(false, null, path, method, true, authToken);

        if (jsonObject == null) {
            return null;
        }
        else {
            var status = jsonObject.get("status").getAsInt();
            if (status == 200) {
                var games = (JsonArray) jsonObject.get("games");
                System.out.println("Current games: ");
                for (JsonElement jsonE: games) {
                    var jsonOb = jsonE.getAsJsonObject();
                    String gameName = jsonOb.get("gameName").getAsString();
                    String gameID = jsonOb.get("gameID").getAsString();
                    String whiteU = "null";
                    String blackU = "null";
                    if (jsonOb.has("whiteUsername")) {
                        whiteU = jsonOb.get("whiteUsername").getAsString();
                    }
                    if (jsonOb.has("blackUsername")) {
                        blackU = jsonOb.get("blackUsername").getAsString();
                    }
                    System.out.printf("GameName: %s.\n", gameName);
                    System.out.printf("GameID: %s.\n", gameID);
                    System.out.printf("White Username: %s.\n", whiteU);
                    System.out.printf("Black Username: %s.\n", blackU);
                    System.out.println();
                }
                return "";
            }
            else if (status == 400){
                System.out.println("Bad request. Try again. \n");
                return null;
            }
            else if (status == 401) {
                System.out.println("Unauthorized. Try again. \n");
                return null;
            }
            else {
                System.out.println("Other error. Try again. \n");
                return null;
            }
        }
    }

    public boolean joinOrObserve(String[] params, String authToken) {
        var path = "/game";
        String method = "PUT";

        var mapOfParams = new HashMap<String, Object>();
        for (int i = 0; i < params.length; i++) {
            if (i == 0) {
                mapOfParams.put("gameID", Integer.parseInt(params[i]));
            }
            else {
                mapOfParams.put("playerColor", params[i].toUpperCase());
            }
        }

        var jsonObject = makeRequest(true, mapOfParams, path, method, true, authToken);

        if (jsonObject == null) {
            System.out.println("Other error. Try again. \n");
            return false;
        }
        else {
            var status = jsonObject.get("status").getAsInt();
            if (status == 200) {
                System.out.println("Successfully joined game.");
                return true;
            }
            else if (status == 400){
                System.out.println("Bad request. Try again. \n");
                return false;
            }
            else if (status == 401) {
                System.out.println("Unauthorized. Try again. \n");
                return false;
            }
            else {
                System.out.println("Other error. Try again. \n");
                return false;
            }
        }
    }

//    public String updateGame() {
//        try {
//            var ws = new WSClient("ws://localhost:8080/connect");
////            ws.send(new JoinPlayerCommand("authToken"));
//            var tempV = new JoinPlayerCommand("12345");
//            var hMap = new HashMap<String, String>();
//            hMap.put("authToken", tempV.getAuthString());
//            hMap.put("commandType", "JOIN_PLAYER");
//            Gson gson = new Gson();
//            var jsonFormat = gson.toJson(tempV);
//            ws.send(jsonFormat);
//            return "";
//        }
//        catch (Exception e) {
//            return e.toString();
//        }
//
//
//    }
}



