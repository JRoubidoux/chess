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
        var gameID = serverConn.list(params, authToken);
        return "";
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
                    case "list" -> System.out.println("to be imp");
                    case "join" -> System.out.println("to be imp");
                    case "observe" -> System.out.println("to be imp");
                    case "logout" -> result = "logout";
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
}
