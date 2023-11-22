package Services;

import DAOs.AuthDAO;
import DAOs.GameDAO;
import Req_and_Result.JoinGameServiceReq;
import Req_and_Result.JoinGameServiceRes;
import dataAccess.DataAccessException;

/**
 * Class that allows a user to join a game on the server.
 */
public class JoinGameService {

    /**
     * Verifies that the specified game exists, and, if a color is specified,
     * adds the caller as the requested color to the game.
     * If no color is specified the user is joined as an observer.
     * This request is idempotent.
     *
     * @param request A JoinGameServiceRequest object.
     * @return A JoinGameServiceResponse object.
     */
    public JoinGameServiceRes joinGame(JoinGameServiceReq request) {
        try {

            var authDAO = new AuthDAO();
            if (!authDAO.authInDB(request.getAuthToken())) {
                throw new DataAccessException("unauthorized");
            }
            var username = authDAO.retrieveAuthToken(request.getAuthToken()).getUsername();

            var gameDao = new GameDAO();
            if (!gameDao.gameInDB(request.getGameID())) {
                throw new DataAccessException("bad request");
            }
            var game = gameDao.findGame(request.getGameID());

            if (request.getColor()!=null) {
                if (request.getColor().equals("WHITE")) {
                    if (game.getWhiteUsername() != null) {
                        throw new DataAccessException("already taken");
                    } else {
                        game.setWhiteUsername(username);
                        gameDao.UpdateGame(game);
                    }
                } else if (request.getColor().equals("BLACK")) {
                    if (game.getBlackUsername() != null) {
                        throw new DataAccessException("already taken");
                    } else {
                        game.setBlackUsername(username);
                        gameDao.UpdateGame(game);
                    }
                }
            }
            var response = new JoinGameServiceRes();
            response.setMessage("success");
            return response;
        }
        catch (DataAccessException e){
            var response = new JoinGameServiceRes();
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
