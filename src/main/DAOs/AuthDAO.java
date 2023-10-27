package DAOs;

import Database.*;
import Models.AuthToken;
import dataAccess.DataAccessException;


/**
 * Class that represents the Data Access Object that deals with authTokens.
 */
public class AuthDAO {

    private Database db = new DataBaseRAM();

    /**
     * Given a username and an authToken, insert them into the DB.
     *
     * @param authTokenOb an instance of an authToken model.
     * @throws DataAccessException If an error occurs trying to insert an authToken
     */
    public void insertAuthToken(AuthToken authTokenOb) throws DataAccessException {
        String username = authTokenOb.getUsername();
        String authToken = authTokenOb.getAuthToken();
        if (authUserNull(authToken, username) || authUserEmpty(authToken, username)) {
            throw new DataAccessException("authToken and username must not by null or empty.");
        }
        if (authInDB(authToken)) {
            throw new DataAccessException("Cannot have duplicate authTokens.");
        }

        db.writeAuth(new AuthToken(authToken, username));
    }

    /**
     * Given an authToken, return a username
     *
     * @param authToken A string that represents an authToken.
     * @return An AuthToken object that represents the authToken and user
     * @throws DataAccessException If an error occurs trying to retrieve an authToken
     */
    public AuthToken retrieveAuthToken(String authToken) throws DataAccessException {
        if (authNull(authToken) || authEmpty(authToken)) {
            throw new DataAccessException("authToken can't be empty or null.");
        }
        return db.readAuth(authToken);
    }

    /**
     * Given an authToken, delete the authToken that corresponds to it.
     *
     * @param authToken A string that represents a username.
     * @throws DataAccessException If an error occurs trying to delete an authToken
     */
    public void deleteAuthToken(String authToken) throws DataAccessException {
        if (authNull(authToken) || authEmpty(authToken)) {
            throw new DataAccessException("authToken can't be null or empty.");
        }
        if (!authInDB(authToken)) {
            throw new DataAccessException("unauthorized");
        }
        db.removeAuth(authToken);
    }

    public void clearAuth() {
        db.clearAuth();
    }


    public boolean authInDB(String authToken) {
        return db.readAuth(authToken) != null;
    }

    public boolean authNull(String authToken) {
        return authToken == null;
    }

    public boolean authEmpty(String authToken) {
        return authToken.isEmpty();
    }

    public boolean authUserNull(String authToken, String username) {
        return (authNull(authToken) || (username == null));
    }

    public boolean authUserEmpty(String authToken, String username) {
        return (authNull(authToken) || (username.isEmpty()));
    }
}
