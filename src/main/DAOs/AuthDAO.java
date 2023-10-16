package DAOs;

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
     */
    public void retrieveAuthToken(String username) {}

    /**
     * Given a username, delete the authToken that corresponds to it.
     *
     * @param username A string that represents a username.
     */
    public void deleteAuthToken(String username) {}


}
