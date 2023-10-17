package DAOs;

import dataAccess.DataAccessException;

/**
 * Class that represents the Data Access Object that deals with authTokens.
 */
public class AuthDAO {

    /**
     * Given a username and an authToken, insert them into the DB.
     *
     * @param username A string that represents a user.
     * @param authToken A string that represents an authToken
     * @throws DataAccessException If an error occurs trying to insert an authToken
     */
    public void insertAuthToken(String username, String authToken) throws DataAccessException {}

    /**
     * Given a username, retrieve an authToken
     *
     * @param username A string that represents a username.
     * @return A string that represents the authToken
     * @throws DataAccessException If an error occurs trying to retrieve an authToken
     */
    public String retrieveAuthToken(String username) throws DataAccessException {return null;}

    /**
     * Given a username, delete the authToken that corresponds to it.
     *
     * @param username A string that represents a username.
     * @throws DataAccessException If an error occurs trying to delete an authToken
     */
    public void deleteAuthToken(String username) throws DataAccessException {}


}
