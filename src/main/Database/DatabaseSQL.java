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
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseSQL implements Database {

    private static dataAccess.Database db = new dataAccess.Database();
    @Override
    public void writeGame(Game game) {
        var gameName = game.getGameName();
        var gameID = game.getGameID();
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

        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("INSERT INTO games (gameName, gameID, currentTurn, blackUser, whiteUser, gameState) VALUES(?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setString(1, gameName);
                preparedStatement.setInt(2, gameID);
                preparedStatement.setString(3, currTurn);
                preparedStatement.setString(4, blackU);
                preparedStatement.setString(5, whiteU);
                preparedStatement.setString(6, (String) json);

                preparedStatement.executeUpdate();
                db.closeConnection(conn);

            }
        }
        catch (DataAccessException | SQLException e) {
            System.out.println(e);
        }

    }


    @Override
    public void writeAuth(AuthToken authToken) {
        var username = authToken.getUsername();
        var authTokenS = authToken.getAuthToken();

        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("INSERT INTO authtokens (authToken, username) VALUES(?, ?)")) {
                preparedStatement.setString(1, authTokenS);
                preparedStatement.setString(2, username);

                preparedStatement.executeUpdate();
                db.closeConnection(conn);

            }
        }
        catch (DataAccessException | SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void writeUser(User user) {
        var username = user.getUsername();
        var email = user.getEmail();
        var password = user.getPassword();

        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");

            try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);

                preparedStatement.executeUpdate();
                db.closeConnection(conn);

            }
        }
        catch (DataAccessException | SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public Game readGame(Integer gameID) {
        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("SELECT gameName, whiteUser, blackUser, currentTurn, gameState FROM games WHERE gameID=?")) {
                preparedStatement.setString(1, String.valueOf(gameID));
                try (var rs = preparedStatement.executeQuery()) {

                    if (!rs.next()) {
                        db.closeConnection(conn);
                        return null;
                    }
                    else {
                        var gameName = rs.getString("gameName");
                        var whiteUser = rs.getString("whiteUser");
                        var blackUser = rs.getString("blackUser");
                        var currentTurn = rs.getString("currentTurn");
                        var gameState = rs.getString("gameState");
                        ChessGame.TeamColor color = null;
                        if (currentTurn.equals("WHITE")) {
                            color = ChessGame.TeamColor.WHITE;
                        }
                        if (currentTurn.equals("BLACK")) {
                            color = ChessGame.TeamColor.BLACK;
                        }
                        var chessBoard = turnToJava(gameState);
                        var gameImp = new ChessGameImp();
                        gameImp.setBoard(chessBoard);
                        gameImp.setTeamTurn(color);
                        var gameModel = new Game();
                        gameModel.setGameName(gameName);
                        gameModel.setGameID(gameID);
                        gameModel.setWhiteUsername(whiteUser);
                        gameModel.setBlackUsername(blackUser);
                        gameModel.setGame(gameImp);
                        db.closeConnection(conn);
                        return gameModel;
                    }
                }
            }
        }
        catch (DataAccessException | SQLException e) {
            return null;
        }
    }

    @Override
    public User readUser(String username) {
        //
        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("SELECT password, email FROM users WHERE username=?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {

                    if (!rs.next()) {
                        db.closeConnection(conn);
                        return null;
                    }
                    else {
                        var password = rs.getString("password");
                        var email = rs.getString("email");
                        db.closeConnection(conn);
                        return new User(username, password, email);
                    }
                }
            }
        }
        catch (DataAccessException | SQLException e) {
            return null;
        }
    }

    @Override
    public AuthToken readAuth(String authToken) {
        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM authtokens WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()) {

                    if (!rs.next()) {
                        db.closeConnection(conn);
                        return null;
                    }
                    else {
                        var username = rs.getString("username");
                        db.closeConnection(conn);
                        return new AuthToken(authToken, username);
                    }
                }
            }
        }
        catch (DataAccessException | SQLException e) {
            return null;
        }
    }

    @Override
    public void removeGame(Integer gameID) {
        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("DELETE FROM games WHERE gameID=?")) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.executeUpdate();
            }
        }
        catch (DataAccessException | SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void removeAuth(String authToken) {
        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authTokens WHERE authToken=?")) {
                preparedStatement.setString(1,authToken);
                preparedStatement.executeUpdate();
            }
        }
        catch (DataAccessException | SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void removeUser(String username) {
        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("DELETE FROM users WHERE username=?")) {
                preparedStatement.setString(1,username);
                preparedStatement.executeUpdate();
            }
        }
        catch (DataAccessException | SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void updateGame(Game game) {
        var gameID = game.getGameID();
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

        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("UPDATE games SET whiteUser=?, blackUser=?, gameState=?, currentTurn=?  WHERE gameID=?")) {
                preparedStatement.setString(1, whiteU);
                preparedStatement.setString(2, blackU);
                preparedStatement.setString(3, (String) json);
                preparedStatement.setString(4, currTurn);
                preparedStatement.setInt(5, gameID);

                preparedStatement.executeUpdate();
            }
        }
        catch (DataAccessException | SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public ArrayList<Game> readAllGames() {
        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("SELECT gameID FROM games")) {
                try (var rs = preparedStatement.executeQuery()) {

                    var games = new ArrayList<Game>();
                    while (rs.next()) {
                        games.add(readGame(rs.getInt("gameID")));
                    }
                    db.closeConnection(conn);
                    return games;
                }
            }
        } catch (SQLException | DataAccessException e) {
            return null;
        }
    }

    @Override
    public void clearGames() {
        try (var conn = db.getConnection()) {
            configureDatabase();

            conn.setCatalog("chess");
            Statement statement = conn.createStatement();

            // SQL statement to drop the table
            String sql = "DROP TABLE " + "games";

            // Execute the SQL statement
            statement.execute(sql);
            configureDatabase();
            db.closeConnection(conn);
        } catch (SQLException | DataAccessException e) {
            System.out.println(e);
        }
    }

        @Override
    public void clearUsers() {
            try (var conn = db.getConnection()) {
                configureDatabase();

                conn.setCatalog("chess");
                Statement statement = conn.createStatement();

                // SQL statement to drop the table
                String sql = "DROP TABLE " + "users";

                // Execute the SQL statement
                statement.execute(sql);
                configureDatabase();
                db.closeConnection(conn);
            } catch (SQLException | DataAccessException e) {
                System.out.println(e);
            }
    }

    @Override
    public void clearAuth() {
        try (var conn = db.getConnection()) {
            configureDatabase();

            conn.setCatalog("chess");
            Statement statement = conn.createStatement();

            // SQL statement to drop the table
            String sql = "DROP TABLE " + "authtokens";

            // Execute the SQL statement
            statement.execute(sql);
            configureDatabase();
            db.closeConnection(conn);
        } catch (SQLException | DataAccessException e) {
            System.out.println(e);
        }
    }

    @Override
    public void clearDB() {
        clearAuth();
        clearUsers();
        clearGames();

    }

    @Override
    public boolean noGamesInDB() {
        return readAllGames() == null;
    }

    @Override
    public boolean noUsersInDB() {
        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM users")) {
                try (var rs = preparedStatement.executeQuery()) {

                    if (!rs.next()) {
                        db.closeConnection(conn);
                        return false;
                    }
                    else {
                        db.closeConnection(conn);
                        return true;
                    }
                }
            }
        }
        catch (DataAccessException | SQLException e) {
            return false;
        }
    }

    @Override
    public boolean noAuthInDB() {
        try (var conn = db.getConnection()) {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("SELECT authToken FROM authTokens")) {
                try (var rs = preparedStatement.executeQuery()) {

                    if (!rs.next()) {
                        db.closeConnection(conn);
                        return false;
                    }
                    else {
                        db.closeConnection(conn);
                        return true;
                    }
                }
            }
        }
        catch (DataAccessException | SQLException e) {
            return false;
        }
    }

    public void configureDatabase() throws SQLException {
        try (var conn = db.getConnection()) {
            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess");
            createDbStatement.executeUpdate();

            conn.setCatalog("chess");

            var createGameTable = """
            CREATE TABLE IF NOT EXISTS Games (
                gameName VARCHAR(255) NOT NULL,
                gameID INT NOT NULL,
                whiteUser VARCHAR(255),
                blackUser VARCHAR(255),
                currentTurn VARCHAR(255),
                gameState LONGTEXT NOT NULL
            )""";


            try (var createTableStatement = conn.prepareStatement(createGameTable)) {
                createTableStatement.executeUpdate();
            }

            var createUserTable = """
            CREATE TABLE IF NOT EXISTS Users (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL
            )""";

            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                createTableStatement.executeUpdate();
            }

            var createAuthTable = """
            CREATE TABLE IF NOT EXISTS AuthTokens (
                authToken VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL
            )""";

            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }
            db.closeConnection(conn);

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
