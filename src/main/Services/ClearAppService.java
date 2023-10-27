package Services;

import DAOs.AuthDAO;
import DAOs.GameDAO;
import DAOs.UserDAO;
import Req_and_Result.ClearAppServiceReq;
import Req_and_Result.ClearAppServiceRes;
import dataAccess.DataAccessException;

/**
 * This Class is intended to clear the DB.
 */
public class ClearAppService {

    /**
     * This function will clear the database. It removes all users, games and authTokens.
     *
     * @param request A ClearAppServiceRequest object.
     * @return A ClearAppServiceResponse object.
     */
    public ClearAppServiceRes clearApp(ClearAppServiceReq request) {
        try {
            var gameDao = new GameDAO();
            var userDao = new UserDAO();
            var authDao = new AuthDAO();

            gameDao.ClearGames();
            userDao.clearUsers();
            authDao.clearAuth();
            return new ClearAppServiceRes();
        }
        catch (DataAccessException e) {
            var response = new ClearAppServiceRes();
            response.setMessage(String.format("Error: %s", e.getMessage()));
            return response;
        }

    }
}
