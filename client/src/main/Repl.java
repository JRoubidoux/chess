import chess.ChessMoveImp;
import chess.ChessPositionImp;
import exception.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

public class Repl {

    private String server_URL;
    private serverFacade serverConn;

    public Repl(String server_URL) {
        this.server_URL = server_URL;
        this.serverConn = new serverFacade(server_URL);
    }


    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to chess. Sign in to start.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            try {
                switch (cmd) {
                    case "help" -> helpPromptLoggedOut();
                    case "register" -> result = register(params);
                    case "login" -> result = login(params);
                    case "quit" -> result = "quit";
                    default -> System.out.println("invalid input, try again.");
                }
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    public String register(String[] params) {
        var authToken = serverConn.register(params);
        if (authToken != null) {
            return runLoggedInLoop(authToken);
        }
        else {
            return "";
        }
    }

    public String login(String[] params) {
        var authToken = serverConn.login(params);
        if (authToken != null) {
            return runLoggedInLoop(authToken);
        }
        else {
            return "";
        }
    }

    public String create(String[] params, String authToken) {
        var gameID = serverConn.create(params, authToken);
        return "";
    }

    public String list(String[] params, String authToken) {
        var gameID = serverConn.list(authToken);
        return "";
    }

    public void joinOrObserve(String[] params, String authToken) {
        var joinOrObservingGame = serverConn.joinOrObserve(params, authToken);
        if (joinOrObservingGame) {
            joinedOrObservedGame(params, authToken);
        }
    }

    public String logout(String authToken) {
        var result = serverConn.logout(authToken);
        if (result) {
            return "logout";
        }
        else {
            return "";
        }
    }

    public void helpPromptLoggedOut() {
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("login <USERNAME> <PASSWORD> - to play chess");
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }

    public void helpPromptLoggedIn() {
        System.out.println("create <NAME> - a game");
        System.out.println("list - games");
        System.out.println("join <ID> [WHITE|BLACK|<empty>] - a game");
        System.out.println("observe <ID> - a game");
        System.out.println("logout - when you are done");
        System.out.println("quit - playing chess");
        System.out.println("help - with possible commands");
    }

    public String runLoggedInLoop(String authToken) {
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while ((!result.equals("logout")) && (!result.equals("quit"))) {
            System.out.println();
            String line = scanner.nextLine();
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            try {
                switch (cmd) {
                    case "help" -> helpPromptLoggedIn();
                    case "create" -> result = create(params, authToken);
                    case "list" -> result = list(params, authToken);
                    case "join" -> joinOrObserve(params, authToken);
                    case "observe" -> joinOrObserve(params, authToken);
                    case "logout" -> result = logout(authToken);
                    case "quit" -> result = "quit";
                    default -> System.out.println("invalid input, try again.");
                }
            } catch (Throwable e) {
                System.out.print(e.getMessage());
                result = "error";
            }
        }
        return result;
    }

    public void joinedOrObservedGame(String[] params, String authToken) {
        try {
            var gameID = Integer.parseInt(params[0]);
            String playerColor = null;
            if (params.length == 2) {
                playerColor = params[1];
            }
            var wsConn = new WSFacade("ws://localhost:8080/connect", playerColor);
            if (playerColor != null) {
                runJoinedLoop(gameID, authToken, playerColor, wsConn);
            } else {
                runObservedLoop(gameID, authToken, wsConn);
            }
        }
        catch (ResponseException e) {
            var temp = e;
        }
    }

    public void runObservedLoop(int gameID, String authToken, WSFacade wsConn) {
        try {
            wsConn.joinObserve(gameID, authToken);

            Scanner scanner = new Scanner(System.in);
            String userInput = "";
            while (!userInput.equals("leave")) {
                System.out.println();
                userInput = scanner.nextLine().toLowerCase();
                switch (userInput) {
                    case "help" -> printHelpJoined();
                    case "redraw chess board" -> drawChessBoard(wsConn);
                    //case "leave" -> ;
                    //                case "make move" -> ;
                    //                case "resign" -> ;
                    //                case "highlight legal moves" -> ;
                    default -> System.out.println("Invalid, try again.");
                }
            }
        }
        catch (ResponseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void runJoinedLoop(int gameID, String authToken, String playerColor, WSFacade wsConn) {
        // print chess board
        // don't allow to make move until other player joined.
        // once have two players, let white go first.
        try {
            wsConn.joinPlayer(gameID, authToken, playerColor);

            Scanner scanner = new Scanner(System.in);
            String userInput = "";
            while (!userInput.equals("leave")) {
                System.out.println();
                userInput = scanner.nextLine().toLowerCase();
                switch (userInput) {
                    case "help" -> printHelpJoined();
                    case "redraw chess board" -> drawChessBoard(wsConn);
                    case "make move" -> makeMove(gameID, authToken, playerColor, wsConn);
                    //case "leave" -> ;
                    //                case "make move" -> ;
                    //                case "resign" -> ;
                    //                case "highlight legal moves" -> ;
                    default -> System.out.println("Invalid, try again.");
                }
            }
        }
        catch (ResponseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printHelpJoined() {
        System.out.println("help");
        System.out.println("redraw chess board");
        System.out.println("leave");
        System.out.println("make move");
        System.out.println("resign");
        System.out.println("highlight legal moves");
    }


    public void drawChessBoard(WSFacade wsConn) {
        wsConn.drawChessboard();
    }

    public void makeMove(int gameID, String authToken, String teamColor, WSFacade wsConn) {
        try {
            var inValid = true;
            String[] tokens = new String[2];

            while (inValid) {
                inValid = false;

                System.out.println("Enter position from (a1) then position to (a2): ");
                var scanner = new Scanner(System.in);
                var userInput = scanner.nextLine().toLowerCase();
                tokens = userInput.split(" ");

                for (String token : tokens) {
                    if (token.length() != 2) {
                        inValid = true;
                        break;
                    }
                    if (!charIsCorrectLetter(token.charAt(0))) {
                        inValid = true;
                        break;
                    }
                    if (!charIsCorrectNumber(token.charAt(1))) {
                        inValid = true;
                        break;
                    }
                }
            }

            var startPos = getPosFromInput(tokens[0]);
            var endPos = getPosFromInput(tokens[1]);

            var chessMove = new ChessMoveImp(startPos, endPos);
            wsConn.makeMove(chessMove, gameID, authToken, teamColor);
        }
        catch (ResponseException e) {
            var temp = e;
        }
    }

    public boolean charIsCorrectLetter(char c) {
        return (c == 'a') || (c == 'b') || (c == 'c') || (c == 'd') || (c == 'e') || (c == 'f') || (c == 'g') || (c == 'h');
    }

    public boolean charIsCorrectNumber(char c) {
        return (c == '1') || (c == '2') || (c == '3') || (c == '4') || (c == '5') || (c == '6') || (c == '7') || (c == '8');
    }

    public ChessPositionImp getPosFromInput(String token) {
        int row = -1;
        int column = -1;
        String letters = "abcdefgh";
        String numbers = "12345678";

        for (int i = 0; i < 8; i++) {
            if (token.charAt(0) == letters.charAt(i)) {
                row = i;
            }
            if (token.charAt(1) == numbers.charAt(i)) {
                column = i;
            }
        }

        return new ChessPositionImp(row, column);
    }
}
