package DAOs;

import Database.*;
import Models.User;
import dataAccess.DataAccessException;

import java.sql.SQLException;

/**
 * Class that represents the Data Access Object that deals with user information.
 */
public class UserDAO {

    private Database db = new DatabaseSQL();

    /**
     * Takes the following parameters and stores them in the DB as a User.
     *
     * @param user A user object.
     * @throws DataAccessException If an error occurs trying to insert a new user.
     */
    public void insertNewUser(User user) throws DataAccessException {
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        if (userPassEmailNull(username, password, email) || userPassEmailEmpty(username, password, email)) {
            throw new DataAccessException("bad request");
        }

        if (userInDB(username)) {
            throw new DataAccessException("already taken");
        }

        db.writeUser(new User(username, password, email));
    }

    /**
     * given a username retrieve the User information from the DB.
     *
     * @param username A string that represents username.
     * @return Returns a User model object.
     * @throws DataAccessException If an error occurs trying to retrieve a user.
     */
    public User retrieveUser(String username) throws DataAccessException {
        if (userNull(username) || userEmpty(username)) {
            throw new DataAccessException("username can't be null or empty.");
        }

        if (!userInDB(username)) {
            throw new DataAccessException("unauthorized");
        }

        return db.readUser(username);
    }

    /**
     * Update User information in the DB.
     *
     * @param username A string that represents username.
     * @param password A string that represents a password.
     * @param email A string that represents a user's email.
     * @throws DataAccessException If an error occurs trying to update a user.
     */
    public void updateUser(String username, String password, String email) throws DataAccessException {
        if (userPassEmailNull(username, password, email) || userPassEmailEmpty(username, password, email)) {
            throw new DataAccessException("Must provide a username, password and email.");
        }

        if (!userInDB(username)) {
            throw new DataAccessException("This user isn't in the DB.");
        }

        else {
            db.updateUser(new User(username, password, email));
        }

    }

    /**
     * Given a username, delete the User information in the DB.
     *
     * @param username A string that represents username.
     * @throws DataAccessException If an error occurs trying to delete a user.
     */
    public void deleteUser(String username) throws DataAccessException{
        if (userNull(username) || userEmpty(username)) {
            throw new DataAccessException("username can't be null or empty.");
        }
        db.removeUser(username);
    }

    public void clearUsers() {
        db.clearUsers();
    }



    public boolean userInDB(String username) {
        return db.readUser(username) != null;
    }

    public boolean userNull(String username) {
        return username == null;
    }

    public boolean userEmpty(String username) {
        return username.isEmpty();
    }

    public boolean userPassEmailNull(String username, String password, String email) {
        return (userNull(username) || (password == null) || (email == null));
    }

    public boolean userPassEmailEmpty(String username, String password, String email) {
        return (userEmpty(username) || (password.isEmpty()) || (email.isEmpty()));
    }
}
