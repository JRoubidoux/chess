package DAOs;

import Models.User;
import dataAccess.DataAccessException;

/**
 * Class that represents the Data Access Object that deals with user information.
 */
public class UserDAO {

    /**
     * Takes the following parameters and stores them in the DB as a User.
     *
     * @param username A string that represents username.
     * @param password A string that represents a password.
     * @param email A string that represents a user's email.
     * @throws DataAccessException If an error occurs trying to insert a new user.
     */
    public void insertNewUser(String username, String password, String email) throws DataAccessException {}

    /**
     * given a username retrieve the User information from the DB.
     *
     * @param username A string that represents username.
     * @return Returns a User model object.
     * @throws DataAccessException If an error occurs trying to retrieve a user.
     */
    public User retrieveUser(String username) throws DataAccessException {return null;}

    /**
     * Update User information in the DB.
     *
     * @param username A string that represents username.
     * @param password A string that represents a password.
     * @param email A string that represents a user's email.
     * @throws DataAccessException If an error occurs trying to update a user.
     */
    public void updateUser(String username, String password, String email) throws DataAccessException {}

    /**
     * Given a username, delete the User information in the DB.
     *
     * @param username A string that represents username.
     * @throws DataAccessException If an error occurs trying to delete a user.
     */
    public void deleteUser(String username) throws DataAccessException{}
}
