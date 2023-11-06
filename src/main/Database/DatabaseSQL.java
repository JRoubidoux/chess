package Database;

import Models.AuthToken;
import Models.Game;
import Models.User;
import chess.ChessBoardImp;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import chess.*;
import dataAccess.DataAccessException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseSQL implements Database {

    private static dataAccess.Database db = new dataAccess.Database();
    @Override
    public void writeGame(Game game) {
        var gameName = game.getGameName();
        var gameID = game.getGameID();
        var currTurn = game.getGame().getTeamTurn();
        var currBoard = game.getGame().getBoard();
        var json = turnToJson((ChessBoardImp) currBoard);


    }


    @Override
    public void writeAuth(AuthToken authToken) {

    }

    @Override
    public void writeUser(User user) {

    }

    @Override
    public Game readGame(Integer gameID) {
        return null;
    }

    @Override
    public User readUser(String username) {
        return null;
    }

    @Override
    public AuthToken readAuth(String authToken) {
        return null;
    }

    @Override
    public void removeGame(Integer gameID) {

    }

    @Override
    public void removeAuth(String authToken) {

    }

    @Override
    public void removeUser(String username) {

    }

    @Override
    public void updateGame(Game game) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public ArrayList<Game> readAllGames() {
        return null;
    }

    @Override
    public void clearGames() {

    }

    @Override
    public void clearUsers() {

    }

    @Override
    public void clearAuth() {

    }

    @Override
    public void clearDB() {

    }

    @Override
    public boolean noGamesInDB() {
        return false;
    }

    @Override
    public boolean noUsersInDB() {
        return false;
    }

    @Override
    public boolean noAuthInDB() {
        return false;
    }

    public void configureDatabase() throws SQLException {
        try (var conn = db.getConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess");
            createDbStatement.executeUpdate();

            conn.setCatalog("chess");

            var createGameTable = """
            CREATE TABLE  IF NOT EXISTS Games (
                gameName VARCHAR(255) NOT NULL,
                gameID INT NOT NULL,
                whiteUser VARCHAR(255),
                blackUser VARCHAR(255),
                currentTurn VARCHAR(255),
                gameState TEXT NOT NULL
            )""";


            try (var createTableStatement = conn.prepareStatement(createGameTable)) {
                createTableStatement.executeUpdate();
            }

            var createUserTable = """
            CREATE TABLE IF NOT EXISTS Users (
                PrimID INT NOT NULL,
                username VARCHAR(255) NOT NULL,
                
            )""";
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
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
