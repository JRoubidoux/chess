package Services;

import DAOs.AuthDAO;
import DAOs.GameDAO;
import Models.Game;
import Req_and_Result.CreateGameServiceReq;
import Req_and_Result.CreateGameServiceRes;
import chess.ChessGameImp;
import dataAccess.DataAccessException;

/**
 * Class that Creates a new game on the server.
 */
public class CreateGameService {

    //private static int gameIDCounter = 1;

    //public void resetGameCounter() {gameIDCounter = 1;}
    /**
     * Creates a new game.
     *
     * @param request A CreateGameServiceRequest object.
     * @return A CreateGameServiceResponse object.
     */
    public CreateGameServiceRes createGame(CreateGameServiceReq request) {

        try {

            var authDAO = new AuthDAO();
            if (!authDAO.authInDB(request.getAuthToken())) {
                throw new DataAccessException("unauthorized");
            }

            var gameDao = new GameDAO();
            var game = new Game();
            if ((request.getGameName() == null) || (request.getGameName().isEmpty())) {
                throw new DataAccessException("bad request");
            }
            game.setGameName(request.getGameName());
            var chessGame = new ChessGameImp();
            var chessBoard = chessGame.getBoard();
            chessBoard.resetBoard();
            game.setGame(chessGame);
            var gameIDCounter = gameDao.insertNewGame(game);
            var response = new CreateGameServiceRes();
            response.setGameID(gameIDCounter);
            response.setMessage("success");
            gameIDCounter++;
            return response;
        }
        catch (DataAccessException e){
            var response = new CreateGameServiceRes();
            response.setMessage(e.getMessage());
            return response;
        }

    }
}
