package DAOs;

import Models.User;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private User getClassicUser(String u, String p, String e) {
        return new User(u, p, e);
    }

    @BeforeEach
    public void clearAllUsers() {
        var userDAO = new UserDAO();
        userDAO.clearUsers();
    }

    @Test
    void insertNewUserPos() {
        var userDao = new UserDAO();
        try {
            userDao.insertNewUser(getClassicUser("Joe", "Pass", "gmail"));
            var user = userDao.retrieveUser("Joe");
            assertEquals(user.getPassword(), "Pass");
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void insertNewUserNeg() {
        var userDao = new UserDAO();
        try {
            userDao.insertNewUser(getClassicUser("Joe", "Pass", "gmail"));
            var user = userDao.retrieveUser("Joe");
            assertNotEquals(user.getPassword(), "Pass1");
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void retrieveUserPos() {
        var userDao = new UserDAO();
        try {
            userDao.insertNewUser(getClassicUser("Joe", "Pass", "gmail"));
            var user = userDao.retrieveUser("Joe");
            assertEquals(user.getPassword(), "Pass");
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void retrieveUserNeg() {
        var userDao = new UserDAO();
        try {
            userDao.insertNewUser(getClassicUser("Joe", "Pass", "gmail"));
            var user = userDao.retrieveUser("Joe");
            assertNotEquals(user.getPassword(), "Pass1");
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void deleteUserPos() {
        var userDao = new UserDAO();
        try {
            userDao.insertNewUser(getClassicUser("Joe", "Pass", "gmail"));
            var user = userDao.retrieveUser("Joe");
            assertEquals(user.getPassword(), "Pass");
            userDao.deleteUser("Joe");
            var result = userDao.userInDB("Joe");
            assertFalse(result);
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void deleteUserNeg() {
        var userDao = new UserDAO();
        try {
            userDao.insertNewUser(getClassicUser("Joe", "Pass", "gmail"));
            var user = userDao.retrieveUser("Joe");
            assertEquals(user.getPassword(), "Pass");
            userDao.deleteUser("Joe2");
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void clearUsers() {
        var userDao = new UserDAO();
        try {
            userDao.insertNewUser(getClassicUser("Joe", "Pass", "gmail"));
            userDao.insertNewUser(getClassicUser("Joe2", "Pass2", "gmail2"));
            var user = userDao.retrieveUser("Joe");
            assertEquals(user.getPassword(), "Pass");
            userDao.clearUsers();
            userDao.retrieveUser("Joe");
        } catch (DataAccessException e) {
            assertEquals(e.getMessage(), "unauthorized");
        }
    }

    @Test
    void userInDBPos() {
        var userDao = new UserDAO();
        try {
            userDao.insertNewUser(getClassicUser("Joe", "Pass", "gmail"));
            var result = userDao.userInDB("Joe");
            assertTrue(result);
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    void userInDBNeg() {
        var userDao = new UserDAO();
        try {
            userDao.insertNewUser(getClassicUser("Joe", "Pass", "gmail"));
            var result = userDao.userInDB("Joe2");
            assertFalse(result);
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }
}