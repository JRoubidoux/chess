import Models.Game;
import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import exception.ResponseException;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

import static ui.EscapeSequences.*;

//need to extend Endpoint for websocket to work properly
public class WSFacade extends Endpoint {

    Session session;
    private Game game = new Game();
    private ChessGame.TeamColor userColor;
    private boolean gameInPlay = true;


    public WSFacade(String url, String color) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.userColor = null;
            if (color != null) {
                if (color.equals("white")) {
                    this.userColor = ChessGame.TeamColor.WHITE;
                }
                if (color.equals("black")) {
                    this.userColor = ChessGame.TeamColor.BLACK;
                }
            }

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        loadGame(new Gson().fromJson(message, LoadGame.class));
                        drawChessboard(new ArrayList<ChessMove>());
                    }
                    else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        var fromJson = new Gson().fromJson(message, Notification.class);
                        gameInPlay = fromJson.getGameInPlay();
                        printNotification(fromJson);
                        }
                    else {
                        printError(new Gson().fromJson(message, Error.class));
                    }

                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public Game getGame() {
        return this.game;
    }

    public boolean getGameStatus() {return this.gameInPlay;}

    public void loadGame(LoadGame loadGame) {
        this.game = readGame(loadGame);
    }

    public void printNotification(Notification notification) {
        System.out.println(notification.getMessage());
    }

    public void printError(Error error) {
        System.out.println(error.getMessage());
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void leaveGame(int gameID, String authToken, String teamColor) throws ResponseException {
        var leaveComm = new LeaveCommand(gameID, authToken, teamColor);
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(leaveComm));
        }
        catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void resignGame(int gameID, String authToken) throws ResponseException{
        var resignComm = new ResignCommand(gameID, authToken);
        try {
            if (this.getGameStatus()) {
                this.session.getBasicRemote().sendText(new Gson().toJson(resignComm));
            }
            else {
                System.out.println("Game is over.");
            }
        }
        catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    public void makeMove(ChessMove move, int gameID, String authToken) throws ResponseException{
        if (gameInPlay) {
            try {
                var makeMoveC = new MakeMoveCommand(gameID, authToken, move);
                this.session.getBasicRemote().sendText(new Gson().toJson(makeMoveC));
            } catch (IOException ex) {
                throw new ResponseException(500, ex.getMessage());
            }
        }
        else {
            System.out.println("Game over, no more moves can be made.");
        }

    }

    public void joinPlayer(int gameID, String authToken, String color) throws ResponseException {
        try {
            //int gameID, String authToken, ChessGame.TeamColor color
            var joinPlayerC = new JoinPlayerCommand(gameID, authToken, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(joinPlayerC));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinObserve(int gameID, String authToken) throws ResponseException {
        try {
            var joinObserverC = new JoinObserverCommand(gameID, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(joinObserverC));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

//    public void leave(int gameID, String authToken, String player) throws ResponseException {
//        try {
//            var command = new LeaveCommand(gameID, authToken);
//            this.session.getBasicRemote().sendText(new Gson().toJson(command));
//            this.session.close();
//        } catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }

    public Game readGame(LoadGame loadGameImp) {
        var json = loadGameImp.getGame();
        var map = new Gson().fromJson(json, HashMap.class);
        var chessBoard = turnToJava((String) map.get("gameState"));
        var gameImp = new ChessGameImp();
        var color = ChessGame.TeamColor.BLACK;
        if (((String) map.get("currentTurn")).equals("WHITE")) {
            color = ChessGame.TeamColor.WHITE;
        }
        gameImp.setBoard(chessBoard);
        gameImp.setTeamTurn(color);
        var gameModel = new Game();
        gameModel.setGameName((String) map.get("gameName"));
        Double doubleV = (double) map.get("gameID");
        gameModel.setGameID(doubleV.intValue());
        gameModel.setWhiteUsername((String) map.get("whiteUser"));
        gameModel.setBlackUsername((String) map.get("blackUser"));
        gameModel.setGame(gameImp);
        return gameModel;
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

    public void drawChessboard(Collection<ChessMove> moves) {
        var game = this.getGame();
        var chessGame = game.getGame();
        var chessBoard = (ChessBoardImp) chessGame.getBoard();

        if (this.userColor == ChessGame.TeamColor.BLACK) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
            System.out.print(SET_TEXT_COLOR_BLACK);
            System.out.print("    h  g  f  e  d  c  b  a    ");
            System.out.print(SET_BG_COLOR_BLACK);
            System.out.print("\n");

            for (int i = 0; i < 8; i++) {
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                System.out.printf(" %d ", i + 1);
                for (int j = 7; j >= 0; j--) {
                    var pos = new ChessPositionImp(i, j);
                    setChessboardColor(i, j, moves);
                    printPiece(chessBoard, pos);
                }
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                System.out.print(SET_TEXT_COLOR_BLACK);
                System.out.printf(" %d ", i + 1);
                System.out.print(SET_BG_COLOR_BLACK);
                System.out.print("\n");
            }
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
            System.out.print(SET_TEXT_COLOR_BLACK);
            System.out.print("    h  g  f  e  d  c  b  a    ");
            System.out.print("\u001b" + "[48;49;" + "15m");
            System.out.print(SET_TEXT_COLOR_WHITE);
            System.out.print("\n");
        }
        else {
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
            System.out.print(SET_TEXT_COLOR_BLACK);
            System.out.print("    a  b  c  d  e  f  g  h    ");
            System.out.print(SET_BG_COLOR_BLACK);
            System.out.print("\n");

            for (int i = 7; i >= 0; i--) {
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                System.out.printf(" %d ", i + 1);
                for (int j = 0; j < 8; j++) {
                    var pos = new ChessPositionImp(i, j);
                    setChessboardColor(i, j, moves);
                    printPiece(chessBoard, pos);
                }
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                System.out.print(SET_TEXT_COLOR_BLACK);
                System.out.printf(" %d ", i + 1);
                System.out.print(SET_BG_COLOR_BLACK);
                System.out.print("\n");
            }
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
            System.out.print(SET_TEXT_COLOR_BLACK);
            System.out.print("    a  b  c  d  e  f  g  h    ");
            System.out.print("\u001b" + "[48;49;" + "15m");
            System.out.print(SET_TEXT_COLOR_WHITE);
            System.out.print("\n");
        }
    }

    public void setChessboardColor(int n1, int n2, Collection<ChessMove> moves) {
        if (moves.isEmpty()) {
            if (((n1 + n2) % 2) == 0) {
                System.out.print(SET_BG_COLOR_WHITE);
            } else {
                System.out.print(SET_BG_COLOR_BLACK);
            }
        }
        else {
            var movesArray = moves.toArray();
            var startPos = ((ChessMoveImp) movesArray[0]).getStartPosition();
            var chessPos = new ChessPositionImp(n1, n2);
            if (Objects.equals(startPos, chessPos)) {
                System.out.print(SET_BG_COLOR_YELLOW);
            }
            else {
                var highlightedMove = false;
                for (var move : movesArray) {
                    var endPos = ((ChessMoveImp) move).getEndPosition();
                    if (Objects.equals(chessPos, endPos)) {
                        highlightedMove = true;
                    }
                }
                if (highlightedMove) {
                    if (((n1 + n2) % 2) == 0) {
                        System.out.print(SET_BG_COLOR_GREEN);
                    } else {
                        System.out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                }
                else {
                    if (((n1 + n2) % 2) == 0) {
                        System.out.print(SET_BG_COLOR_WHITE);
                    } else {
                        System.out.print(SET_BG_COLOR_BLACK);
                    }
                }
            }
        }
    }

    public void setPieceColor(ChessPiece chessPiece) {
        if (chessPiece == null) {
            System.out.print(SET_TEXT_COLOR_BLACK);
        }
        else {
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                System.out.print(SET_TEXT_COLOR_RED);
            }
            else {
                System.out.print(SET_TEXT_COLOR_BLUE);
            }
        }
    }

    public void printPiece(ChessBoardImp chessBoard, ChessPositionImp pos) {
        var chessPiece = chessBoard.getPiece(pos);
        setPieceColor(chessPiece);
        if (chessPiece == null) {
            System.out.print("   ");
        }
        else {
            if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK) {
                System.out.print(" R ");
            }
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                System.out.print(" N ");
            }
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                System.out.print(" B ");
            }
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.KING) {
                System.out.print(" K ");
            }
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                System.out.print(" Q ");
            }
            else if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                System.out.print(" P ");
            }
        }
    }
}