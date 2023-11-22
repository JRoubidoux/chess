package Services;

import DAOs.AuthDAO;
import Req_and_Result.LogoutServiceReq;
import Req_and_Result.LogoutServiceRes;
import dataAccess.DataAccessException;

/**
 * Class that handles logouts on the server.
 */
public class LogoutService {

    /**
     * Logs out the user represented by the authToken.
     *
     * @param request A LogoutServiceRequest object.
     * @return A LogoutServiceResponse object.
     */
    public LogoutServiceRes logout(LogoutServiceReq request) {

        try {
            var authDao = new AuthDAO();

            authDao.deleteAuthToken(request.getAuthToken());

            var response = new LogoutServiceRes();
            response.setMessage("success");

            return response;

        }
        catch (DataAccessException e) {
            var response = new LogoutServiceRes();
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
