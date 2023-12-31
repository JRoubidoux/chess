package Server;

import DAOs.AuthDAO;
import DAOs.GameDAO;
import Models.Game;
import Server.websocketSupport.ConnectionManager;
import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import dataAccess.DataAccessException;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.HashMap;
import org.eclipse.jetty.websocket.api.Session;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameDAO = new GameDAO();
    private final AuthDAO authDAO = new AuthDAO();


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayerCommand.class), session);
            case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserverCommand.class), session);
            case MAKE_MOVE -> makeMove(jsonToMakeMove(message), session);
            case RESIGN -> resign(new Gson().fromJson(message, ResignCommand.class));
            case LEAVE -> leave(new Gson().fromJson(message, LeaveCommand.class));
        }
    }

    private void leave(LeaveCommand command) throws IOException {
        var gameID = command.getGameID();
        var authToken = command.getAuthString();

        try {
            var username = authDAO.retrieveAuthToken(authToken).getUsername();
            var currGame = gameDAO.findGame(gameID);
            if (username.equals(currGame.getWhiteUsername())) {
                currGame.setWhiteUsername(null);
            }
            if (username.equals(currGame.getBlackUsername())) {
                currGame.setBlackUsername(null);
            }
            gameDAO.UpdateGame(currGame);
            String msg = String.format("%s has left the game.", username);
            var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, msg, false);
            leaveResponse(gameID, authToken, notification);
            connections.remove(gameID, authToken);

        }
        catch (DataAccessException e) {
            var temp = e;
        }

        connections.remove(gameID, authToken);
    }

    public void leaveResponse(int gameID, String authToken, Notification notification) throws IOException {
        for (var c : connections.connections.get((Integer) gameID).values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(authToken)) {
                    c.send(new Gson().toJson(notification));
                }
            }
        }
    }

    private void resign(ResignCommand command) throws IOException {
        var authToken = command.getAuthString();
        var gameID = command.getGameID();

        try {
            var currGame = gameDAO.findGame(gameID);
            var username = authDAO.retrieveAuthToken(authToken).getUsername();
            var resigned = currGame.getResigned();

            String errorMessage = null;
            var commandComesFromObserver = false;
            var resignedNull = true;

            if (resigned == null) {
                currGame.setResigned(username);
                gameDAO.UpdateGame(currGame);
            }
            if (resigned != null) {
                resignedNull = false;
            }

            if ((resignedNull) && (!currGame.getWhiteUsername().equals(username)) && (!currGame.getBlackUsername().equals(username))) {
                errorMessage = "Observes aren't playing, thus can't resign.";
                commandComesFromObserver = true;
            }

            if (!resignedNull) {
                errorMessage = "Game already over. Other player already resigned.";
                commandComesFromObserver = true;
            }

            if (commandComesFromObserver) {
                var errorResponse = new Error(ServerMessage.ServerMessageType.ERROR, errorMessage);
                connections.connections.get(gameID).get(authToken).send(new Gson().toJson(errorResponse));
            }
            else {
                String msg = String.format("%s resigns the game.", username);
                var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, msg, false);
                playerResignsRespone(notification, gameID);

            }
        }
        catch (DataAccessException e) {
            var temp = e;
        }
    }

    private void playerResignsRespone(Notification notification, int gameID) throws IOException {
        for (var c : connections.connections.get((Integer) gameID).values()) {
            if (c.session.isOpen()) {
                c.send(new Gson().toJson(notification));
            }
        }
    }

    public MakeMoveCommand jsonToMakeMove(String json) {
        var map = new Gson().fromJson(json, HashMap.class);
        var gameID = ((Double) map.get("gameID")).intValue();
        var authToken = (String) map.get("authToken");
        var move = getMove((LinkedTreeMap<String, Object>) map.get("move"));
        return new MakeMoveCommand(gameID, authToken, move);
    }

    public ChessMoveImp getMove(LinkedTreeMap<String, Object> map) {
        var startPos = getPos((LinkedTreeMap<String, Double>) map.get("startPos"));
        var endPos = getPos((LinkedTreeMap<String, Double>) map.get("endPos"));
        return new ChessMoveImp(startPos, endPos);
    }

    public ChessPositionImp getPos(LinkedTreeMap<String, Double> map) {
        int row = ((Double) map.get("row")).intValue();
        int column = ((Double) map.get("column")).intValue();
        return new ChessPositionImp(row, column);
    }

    private void makeMove(MakeMoveCommand makeMoveC, Session session) throws IOException {
        var gameID = makeMoveC.getGameID();
        var authToken = makeMoveC.getAuthString();
        var chessMove = makeMoveC.getMove();

        try {
            var currGame = gameDAO.findGame(gameID);
            var username = authDAO.retrieveAuthToken(authToken).getUsername();

            ChessGame.TeamColor teamColor = null;
            ChessGame.TeamColor otherTeam = null;
            String otherUser = null;

            if (currGame.getWhiteUsername().equals(username)) {
                teamColor = ChessGame.TeamColor.WHITE;
                otherTeam = ChessGame.TeamColor.BLACK;
                otherUser = currGame.getBlackUsername();
            }
            if (currGame.getBlackUsername().equals(username)) {
                teamColor = ChessGame.TeamColor.BLACK;
                otherTeam = ChessGame.TeamColor.WHITE;
                otherUser = currGame.getWhiteUsername();
            }

            String errorMessage = null;
            var makeTheMove = true;
            var rootInCheckmate = false;
            var oppInCheckmate = false;
            var oppInCheck = false;
            var isObserver = false;
            var isResigned = false;

            if (teamColor == null) {
                makeTheMove = false;
                isObserver = true;
                errorMessage = "Observer can't make move.";
            }

            if (currGame.getResigned() != null) {
                makeTheMove = false;
                isResigned = true;
                errorMessage = "Game is over.";
            }

            if ((currGame.getGame().getTeamTurn() != teamColor) && (makeTheMove)) {
                makeTheMove = false;
                errorMessage = "Not your turn";
            }

            if ((makeTheMove) && (currGame.getGame().getTeamTurn() == teamColor) && (currGame.getGame().isInCheckmate(teamColor))) {
                makeTheMove = false;
                rootInCheckmate = true;
                errorMessage = "Can't make move, you're in checkmate";
            }

            if ((!currGame.getGame().validMoves(chessMove.getStartPosition()).contains(chessMove)) && (makeTheMove)) {
                makeTheMove = false;
                errorMessage = "Not a valid move.";
            }

            if (makeTheMove) {

                currGame.getGame().makeMove(chessMove);
                gameDAO.UpdateGame(currGame);


                if (currGame.getGame().isInCheckmate(otherTeam)) {
                    oppInCheckmate = true;
                }
                if ((!oppInCheckmate) && (currGame.getGame().isInCheck(otherTeam))) {
                    oppInCheck = true;
                }

                String messageForOthers = String.format("%s moved.", username);
                if (oppInCheckmate) {
                    messageForOthers = messageForOthers + String.format(" %s in is checkmate.", otherUser);
                }
                if (oppInCheck) {
                    messageForOthers = messageForOthers + String.format(" %s in is check.", otherUser);
                }
                var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, messageForOthers, !oppInCheckmate);

                var loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, writeGame(gameDAO.findGame(gameID)));
                makeMoveResponse(gameID, authToken, notification, loadGame);

            }
            else {
                if (rootInCheckmate || isResigned || isObserver) {
                    // send error to root
                    var errorResponse = new Error(ServerMessage.ServerMessageType.ERROR, errorMessage);
                    connections.connections.get(gameID).get(authToken).send(new Gson().toJson(errorResponse));
                }
                else {
                    var errorResponse = new Error(ServerMessage.ServerMessageType.ERROR, errorMessage);
                    sendError(new Gson().toJson(errorResponse), session);
                }
            }

        }
        catch (DataAccessException | InvalidMoveException e) {
            var temp = e;
        }
    }

    public void makeMoveResponse(int gameID, String authToken, Notification notification, LoadGame loadGame) throws IOException {

        // send notification
        for (var c : connections.connections.get((Integer) gameID).values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(authToken)) {
                    c.send(new Gson().toJson(notification));
                }
            }
        }

        for (var c : connections.connections.get((Integer) gameID).values()) {
            if (c.session.isOpen()) {
                    c.send(new Gson().toJson(loadGame));
            }
        }
    }

    private void joinPlayer(JoinPlayerCommand joinPlayerC, Session session) throws IOException {

        var gameID = joinPlayerC.getGameID();
        var authToken = joinPlayerC.getAuthString();
        var playerColor = "white";
        if (joinPlayerC.getPlayerColor().equalsIgnoreCase("black")) {
            playerColor = "black";
        }


        try {
            String errorMessage = null;
            String username = null;
            var addPlayer = true;

            if (!authDAO.authInDB(authToken)) {
                addPlayer = false;
                errorMessage = "Bad authToken.";
            }

            if ((!gameDAO.gameInDB(gameID)) && addPlayer) {
                addPlayer = false;
                errorMessage = "Game doesn't exist.";
            }

            if (addPlayer) {
                username = authDAO.retrieveAuthToken(authToken).getUsername();
            }

            if ((playerColor.equals("white")) && addPlayer) {
                var dbResult = gameDAO.findGame(gameID).getWhiteUsername();
                if (dbResult != null) {
                    if (!dbResult.equals(username)) {
                        addPlayer = false;
                        errorMessage = "Color in game is already taken.";
                    }
                }
                else {
                    addPlayer = false;
                    errorMessage = "HTTP method didn't join player.";
                }
            }
            if ((playerColor.equals("black")) && addPlayer) {
                var dbResult = gameDAO.findGame(gameID).getBlackUsername();
                if (dbResult != null) {
                    if (!dbResult.equals(username)) {
                        addPlayer = false;
                        errorMessage = "Color in game is already taken.";
                    }
                }
                else {
                    addPlayer = false;
                    errorMessage = "HTTP method didn't join player.";
                }
            }
            if (addPlayer) {

                connections.add(gameID, authToken, session);

                var messageForOthers = String.format("%s joined the game as %s.", username, playerColor);
                var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, messageForOthers, true);

                var loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, writeGame(gameDAO.findGame(gameID)));

                joinPlayerResponse(gameID, authToken, notification, loadGame);
            }
            else {
                var errorResponse = new Error(ServerMessage.ServerMessageType.ERROR, errorMessage);
                sendError(new Gson().toJson(errorResponse), session);
            }
        }
        catch (DataAccessException e) {
            var temp = e;
        }
    }

    private void joinObserver(JoinObserverCommand joinPlayerC, Session session) throws IOException {

        var gameID = joinPlayerC.getGameID();
        var authToken = joinPlayerC.getAuthString();

        connections.add(gameID, authToken, session);
        try {

            String errorMessage = null;
            var addPlayer = true;

            if (!authDAO.authInDB(authToken)) {
                addPlayer = false;
                errorMessage = "Bad authToken.";
            }

            if ((!gameDAO.gameInDB(gameID)) && addPlayer) {
                addPlayer = false;
                errorMessage = "Game doesn't exist.";
            }

            if (addPlayer) {
                var username = authDAO.retrieveAuthToken(authToken).getUsername();
                var messageForOthers = String.format("%s observes the game.", username);
                var notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, messageForOthers, true);

                var loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, writeGame(gameDAO.findGame(gameID)));

                joinPlayerResponse(gameID, authToken, notification, loadGame);
            }
            else {
                var errorResponse = new Error(ServerMessage.ServerMessageType.ERROR, errorMessage);
                sendError(new Gson().toJson(errorResponse), session);
            }
        }
        catch (DataAccessException e) {

        }
    }

    public void joinPlayerResponse(int gameID, String authToken, Notification notification, LoadGame loadGame) throws IOException {

        // send notification
        for (var c : connections.connections.get((Integer) gameID).values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(authToken)) {
                    c.send(new Gson().toJson(notification));
                }
            }
        }

        connections.connections.get(gameID).get(authToken).send(new Gson().toJson(loadGame));
    }

    public void sendError(String message, Session session) throws IOException{
        session.getRemote().sendString(message);
    }

    public String writeGame(Game game) {
        var gameName = game.getGameName();
        Integer gameID = game.getGameID();
        String currTurn = null;
        if (game.getGame().getTeamTurn() == ChessGame.TeamColor.WHITE) {
            currTurn = "WHITE";
        }
        if (game.getGame().getTeamTurn() == ChessGame.TeamColor.BLACK) {
            currTurn = "BLACK";
        }
        var currBoard = game.getGame().getBoard();
        var json = turnToJson((ChessBoardImp) currBoard);
        var whiteU = game.getWhiteUsername();
        var blackU = game.getBlackUsername();

        var map = new HashMap<String, Object>();
        //gameName, currentTurn, blackUser, whiteUser, gameState
        map.put("gameID", gameID);
        map.put("gameName", gameName);
        map.put("currentTurn", currTurn);
        map.put("blackUser", blackU);
        map.put("whiteUser", whiteU);
        map.put("gameState", json);

        var temp = new Gson().toJson(map);
        return temp;
    }

    public ChessBoardImp turnToJava(String jsonString) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessBoardImp.class, new ChessBoardAdapter());
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        return gson.fromJson(jsonString, ChessBoardImp.class);
    }

    public Object turnToJson(ChessBoardImp chessBoard) {
        var body = new Gson().toJson(chessBoard);
        return body;
    }

    public class ChessBoardAdapter extends TypeAdapter<ChessBoard> {
        @Override
        public void write(JsonWriter out, ChessBoard value) {
            // Implement the serialization logic if needed.
        }

        @Override
        public ChessBoard read(JsonReader in) {
            // Implement the deserialization logic for the ChessBoard class here.
            try {
                in.beginObject(); // Start reading the JSON object for ChessBoard.
                in.nextName(); // Assuming there's a key name for the 8x8 array, read it (e.g., "chessBoard").

                var chessBoard = new ChessBoardImp(); // Initialize the 8x8 array.

                in.beginArray(); // Start reading the JSON array for the chessboard.

                for (int i = 0; i < 8; i++) {
                    in.beginArray(); // Start reading a row within the array.

                    for (int j = 0; j < 8; j++) {
                        // Use the ChessPieceAdapter to read and deserialize each chess piece.
                        ChessPiece chessPiece = new ChessPieceAdapter().read(in);
                        var chessPosition = new ChessPositionImp(i, j);
                        chessBoard.addPiece(chessPosition, chessPiece);
                    }

                    in.endArray(); // End the row array.
                }

                in.endArray(); // End the main array for the chessboard.

                in.endObject(); // End the ChessBoard object.

                return chessBoard;
            } catch (IOException e) {
                // Handle any exceptions appropriately.
                return null; // or throw an exception.
            }
        }
    }


    public class ChessPieceAdapter extends TypeAdapter<ChessPiece> {
        @Override
        public ChessPiece read(JsonReader in) throws IOException {
            // Implement the deserialization logic here.
            // Read the JSON data and create an instance of the specific chess piece.
            String currentTeam = null;
            String typePiece = null;

            if (in.peek() == JsonToken.NULL) {
                // Handle the case where the next value is null
                in.nextNull(); // Consume the null value
                return null;
            }

            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                if ("currentTeam".equals(name)) {
                    currentTeam = in.nextString();
                } else if ("typePiece".equals(name)) {
                    typePiece = in.nextString();
                } else {
                    in.skipValue(); // Ignore unknown properties.
                }
            }
            in.endObject();

            ChessGame.TeamColor color = null;
            if (currentTeam.equals("WHITE")) {
                color = ChessGame.TeamColor.WHITE;
            }
            else {
                color = ChessGame.TeamColor.BLACK;
            }
            // Create and return the appropriate chess piece based on 'currentTeam' and 'typePiece'.
            // Example:
            if ("ROOK".equals(typePiece)) {
                return new Rook(color); // Create your Rook class with the provided constructor.
            } else if ("KNIGHT".equals(typePiece)) {
                return new Knight(color); // Create your Knight class with the provided constructor.
            } else if ("BISHOP".equals(typePiece)) {
                return new Bishop(color); // Create your Bishop class with the provided constructor.
            } else if ("PAWN".equals(typePiece)) {
                return new Pawn(color); // Create your Pawn class with the provided constructor.
            } else if ("QUEEN".equals(typePiece)) {
                return new Queen(color); // Create your Queen class with the provided constructor.
            } else {
                return new King(color); // Create your King class with the provided constructor.
            }
        }

        @Override
        public void write(JsonWriter jsonWriter, ChessPiece chessPiece) throws IOException {

        }
    }
}