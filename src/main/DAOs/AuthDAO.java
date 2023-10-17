package DAOs;

/**
 * Class that represents the Data Access Object that deals with authTokens.
 */
public class AuthDAO {

    /**
     * Given a username and an authToken, insert them into the DB.
     *
     * @param username A string that represents a user.
     * @param authToken A string that represents an authToken
     */
    public void insertAuthToken(String username, String authToken) {}

    /**
     * Given a username, retrieve an authToken
     *
     * @param username A string that represents a username.
     * @return A string that represents the authToken
     */
    public String retrieveAuthToken(String username) {return null;}

    /**
     * Given a username, delete the authToken that corresponds to it.
     *
     * @param username A string that represents a username.
     */
    public void deleteAuthToken(String username) {}


}
