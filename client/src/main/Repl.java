import Models.Game;
import chess.*;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

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
        var gameID = Integer.parseInt(params[0]);
        String playerColor = null;
        if (params.length == 2) {
            playerColor = params[1];
        }

        if (playerColor != null) {
            runJoinedLoop(gameID, authToken, playerColor);
        } else {
            // observe
        }
    }

    public void runJoinedLoop(int gameID, String authToken, String playerColor) {
        // print chess board
        // don't allow to make move until other player joined.
        // once have two players, let white go first.

        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        while (!userInput.equals("leave")) {
            System.out.println();
            userInput = scanner.nextLine().toLowerCase();
            switch (userInput) {
                case "help" -> printHelpJoined();
                case "redraw chess board" -> ;
                case "leave" -> ;
                case "make move" -> ;
                case "resign" -> ;
                case "highlight legal moves" -> ;
            }
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










//        serverConn.updateGame();
//
//        var game = new Game();
//        game.setGame(new ChessGameImp());
//        var chessGame = game.getGame();
//        var chessBoard = (ChessBoardImp) chessGame.getBoard();
//        chessBoard.resetBoard();
//        System.out.print(SET_BG_COLOR_LIGHT_GREY);
//        System.out.print(SET_TEXT_COLOR_BLACK);
//        System.out.print("    h  g  f  e  d  c  b  a    ");
//        System.out.print(SET_BG_COLOR_BLACK);
//        System.out.print("\n");
//
//        for (int i = 0; i < 8; i++) {
//            System.out.print(SET_BG_COLOR_LIGHT_GREY);
//            System.out.printf(" %d ", i+1);
//            for (int j = 0; j < 8; j++) {
//                var pos = new ChessPositionImp(i, j);
//                setChessboardColor(i+j);
//                printPiece(chessBoard, pos);
//            }
//            System.out.print(SET_BG_COLOR_LIGHT_GREY);
//            System.out.print(SET_TEXT_COLOR_BLACK);
//            System.out.printf(" %d ", i+1);
//            System.out.print(SET_BG_COLOR_BLACK);
//            System.out.print("\n");
//        }
//        System.out.print(SET_BG_COLOR_LIGHT_GREY);
//        System.out.print(SET_TEXT_COLOR_BLACK);
//        System.out.print("    h  g  f  e  d  c  b  a    ");
//        System.out.print(SET_BG_COLOR_BLACK);
//        System.out.print("\n\n");
//
//
//        System.out.print(SET_BG_COLOR_LIGHT_GREY);
//        System.out.print(SET_TEXT_COLOR_BLACK);
//        System.out.print("    a  b  c  d  e  f  g  h    ");
//        System.out.print(SET_BG_COLOR_BLACK);
//        System.out.print("\n");
//
//        for (int i = 7; i >= 0; i--) {
//            System.out.print(SET_BG_COLOR_LIGHT_GREY);
//            System.out.printf(" %d ", i+1);
//            for (int j = 7; j >= 0; j--) {
//                var pos = new ChessPositionImp(i, j);
//                setChessboardColor(i+j);
//                printPiece(chessBoard, pos);
//            }
//            System.out.print(SET_BG_COLOR_LIGHT_GREY);
//            System.out.print(SET_TEXT_COLOR_BLACK);
//            System.out.printf(" %d ", i+1);
//            System.out.print(SET_BG_COLOR_BLACK);
//            System.out.print("\n");
//        }
//        System.out.print(SET_BG_COLOR_LIGHT_GREY);
//        System.out.print(SET_TEXT_COLOR_BLACK);
//        System.out.print("    a  b  c  d  e  f  g  h    ");
//        System.out.print("\u001b" + "[48;49;" + "15m");
//        System.out.print(SET_TEXT_COLOR_WHITE);

    public void setChessboardColor(int number) {
        if (((number) % 2) == 0) {
            System.out.print(SET_BG_COLOR_WHITE);
        }
        else {
            System.out.print(SET_BG_COLOR_BLACK);
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
