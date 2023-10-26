package DAOs;

import Models.AuthToken;
import dataAccess.DataAccessException;

import java.util.HashMap;

/**
 * Class that represents the Data Access Object that deals with authTokens.
 */
public class AuthDAO {

    private static HashMap<String, String> authTokens;

    /**
     * Given a username and an authToken, insert them into the DB.
     *
     * @param username A string that represents a user.
     * @param authToken A string that represents an authToken
     * @throws DataAccessException If an error occurs trying to insert an authToken
     */
    public void insertAuthToken(String username, String authToken) throws DataAccessException {
        if (authUserNull(authToken, username) || authUserEmpty(authToken, username)) {
            throw new DataAccessException("authToken and username must not by null or empty.");
        }
        if (authInDB(authToken)) {
            throw new DataAccessException("Cannot have duplicate authTokens.");
        }
        authTokens.put(authToken, username);
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
        var authTokenOb = new AuthToken();
        authTokenOb.setAuthToken(authToken);
        authTokenOb.setUsername(authTokens.get(authToken));
        return authTokenOb;
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
        authTokens.remove(authToken);
    }

    public void deleteAllAuth() throws DataAccessException {
        if (authTokens != null) { authTokens.clear(); }
    }

    public boolean authInDB(String authToken) {
        return authTokens.get(authToken) != null;
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
