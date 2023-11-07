package Database;

import Models.Game;
import chess.ChessBoardImp;
import chess.ChessGameImp;

import java.sql.SQLDataException;
import java.sql.SQLException;


public class testDB {

    public static void main(String[] args) {
        var game = new Game();
        var db = new DatabaseSQL();
        game.setBlackUsername("b");
        game.setWhiteUsername("w");
        game.setGameID(1);
        var gameimp = new ChessGameImp();
        var chessboardimp = new ChessBoardImp();
        chessboardimp.resetBoard();
        gameimp.setBoard(chessboardimp);
        game.setGame(gameimp);
        game.setGameName("gamename");
//        var thing = db.turnToJson((ChessBoardImp) game.getGame().getBoard());
//        var newGame = db.turnToJava((String) thing);
        var dummy = "heet";
        try {
            db.configureDatabase();
            var userOb = db.readUser("jackie");
            db.writeGame(game);
            var gameOb = db.readGame(game.getGameID());
            dummy = "yeehaw";
            gameOb.setBlackUsername("newBlack");
            db.updateGame(gameOb);
            db.clearDB();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }
}
