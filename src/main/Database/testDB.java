package Database;

import Models.Game;
import chess.ChessBoardImp;
import chess.ChessGameImp;


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
        var thing = db.turnToJson(game);
        var newGame = db.turnToJava(thing, Game.class);

    }
}
