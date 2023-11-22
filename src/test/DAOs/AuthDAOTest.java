package DAOs;

import Models.AuthToken;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOTest {


    @BeforeEach
    public void clearALl() {
        var authDAO = new AuthDAO();
        authDAO.clearAuth();
    }

    @Test
    public void insertDataPos() {
        var authDAO = new AuthDAO();

        String authToken = "authToken1";
        String username = "Jonnie";
        try {
            authDAO.insertAuthToken(new AuthToken(authToken, username));
            var secondAuth = authDAO.retrieveAuthToken(authToken);
            assertEquals(username, secondAuth.getUsername());
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    public void insertDataNeg() {
        var authDAO = new AuthDAO();

        String authToken = "authToken1";
        String username = "Jonnie";
        try {
            authDAO.insertAuthToken(new AuthToken(authToken, username));
            var secondAuth = authDAO.retrieveAuthToken(authToken);
            assertNotEquals("Johnie", secondAuth.getUsername());
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    public void readDataPos() {
        var authDAO = new AuthDAO();

        String authToken = "authToken1";
        String username = "Jonnie";
        try {
            authDAO.insertAuthToken(new AuthToken(authToken, username));
            var secondAuth = authDAO.retrieveAuthToken(authToken);
            assertEquals("Jonnie", secondAuth.getUsername());
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    public void readDataNeg() {
        var authDAO = new AuthDAO();

        String authToken = "authToken1";
        String username = "Jonnie";
        String authToken2 = "authToken2";
        try {
            authDAO.insertAuthToken(new AuthToken(authToken, username));
            var secondAuth = authDAO.retrieveAuthToken(authToken2);
            assertNull(secondAuth);
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    public void deleteDataPos() {
        var authDAO = new AuthDAO();

        String authToken = "authToken1";
        String username = "Jonnie";
        try {
            authDAO.insertAuthToken(new AuthToken(authToken, username));
            authDAO.deleteAuthToken(authToken);
            var secondAuth = authDAO.retrieveAuthToken(authToken);
            assertNull(secondAuth);

        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    public void deleteDataNeg() {
        var authDAO = new AuthDAO();
        String authToken = "authToken1";
        String username = "Jonnie";
        String authToken2 = "authToken2";

        try {
            authDAO.insertAuthToken(new AuthToken(authToken, username));
            authDAO.deleteAuthToken(authToken2);

        }
        catch (DataAccessException e) {
            assertEquals(e.getMessage(), "unauthorized");
        }
    }

    @Test
    public void clearAuth() {
        var authDAO = new AuthDAO();

        String authToken = "authToken1";
        String username = "Jonnie";
        try {
            authDAO.insertAuthToken(new AuthToken(authToken, username));
            authDAO.clearAuth();
            var secondAuth = authDAO.retrieveAuthToken(authToken);
            assertNull(secondAuth);
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    public void authInDBPos() {
        var authDAO = new AuthDAO();

        String authToken = "authToken1";
        String username = "Jonnie";
        try {
            authDAO.insertAuthToken(new AuthToken(authToken, username));
            assertTrue(authDAO.authInDB(authToken));
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Test
    public void authInDBNeg() {
        var authDAO = new AuthDAO();

        String authToken = "authToken1";
        String username = "Jonnie";
        try {
            authDAO.insertAuthToken(new AuthToken(authToken, username));
            assertFalse(authDAO.authInDB("authToken2"));
        }
        catch (DataAccessException e) {
            System.out.println(e);
        }
    }

}