package Services;

import DAOs.AuthDAO;
import DAOs.UserDAO;
import Models.AuthToken;
import Req_and_Result.LoginServiceReq;
import Req_and_Result.LoginServiceRes;
import dataAccess.DataAccessException;

import java.util.UUID;

/**
 * Class that handles logins on the server
 */
public class LoginService {

    /**
     * Logs in an existing user and returns a new authToken.
     *
     * @param request LoginServiceRequest object.
     * @return Returns a LoginServiceResponse object.
     */
    public LoginServiceRes login(LoginServiceReq request) {
        try {
            var userDao = new UserDAO();

            var user = userDao.retrieveUser(request.getUsername());
            if (!user.getPassword().equals(request.getPassword())) {
                throw new DataAccessException("unauthorized");
            }
            var authToken = UUID.randomUUID().toString();
            var authDao = new AuthDAO();
            var authTokenOb = new AuthToken(authToken, request.getUsername());
            authDao.insertAuthToken(authTokenOb);

            var response = new LoginServiceRes();
            response.setUsername(request.getUsername());
            response.setAuthToken(authToken);
            response.setMessage("success");

            return response;

        }
        catch (DataAccessException e) {
            var response = new LoginServiceRes();
            response.setMessage(e.getMessage());
            return response;
        }

    }

}
