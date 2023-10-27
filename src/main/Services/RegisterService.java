package Services;

import DAOs.AuthDAO;
import DAOs.UserDAO;
import Models.AuthToken;
import Models.User;
import Req_and_Result.ClearAppServiceRes;
import Req_and_Result.RegisterServiceReq;
import Req_and_Result.RegisterServiceRes;
import dataAccess.DataAccessException;

import java.util.*;

/**
 * Class that registers a new user in the DB
 */
public class RegisterService {

    /**
     *  This function will register a new user.
     *
     * @param request A RegisterServiceRequest object.
     * @return A RegisterServiceResponse object.
     */
    public RegisterServiceRes register(RegisterServiceReq request) {
        try {
            var userDao = new UserDAO();

            userDao.insertNewUser(new User(request.getUsername(), request.getPassword(), request.getEmail()));
            var authToken = UUID.randomUUID().toString();
            var authDao = new AuthDAO();
            var authTokenOb = new AuthToken(authToken, request.getUsername());
            authDao.insertAuthToken(authTokenOb);

            var response = new RegisterServiceRes();
            response.setUsername(request.getUsername());
            response.setAuthToken(authToken);
            response.setMessage("success");

            return response;

        }
        catch (DataAccessException e) {
            var response = new RegisterServiceRes();
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
