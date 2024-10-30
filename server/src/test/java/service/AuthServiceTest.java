package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceTest {


    AuthDAO authDAO = new MemoryAuthDAO();
    AuthService authService = new AuthService(authDAO);

    @Test
    @Order(1)
    @DisplayName("Create - Success")
    public void testCreateSuccess() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");

        Assertions.assertNotNull(token, "token should be returning a non-null value ");

    }

    @Test
    @Order(2)
    @DisplayName("Create - Failure")
    public void testCreateFailure() throws DataAccessException {

        Assertions.assertThrows(DataAccessException.class, () -> authService.createAuth(null));
    }

    @Test
    @Order(3)
    @DisplayName("Authenticate - Success")
    public void testAuthenticateSuccess() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");

        AuthData expectedAuthData = new AuthData(token.getAuthToken(), token.getUsername());

        AuthData actualAuthData = authService.authenticate(token.getAuthToken());

        Assertions.assertNotNull(actualAuthData, "AuthData should not be null for a valid token");
        Assertions.assertEquals(expectedAuthData, actualAuthData, "Returned AuthData should match expected");
    }

    @Test
    @Order(4)
    @DisplayName("Authenticate - Failure")
    public void testAuthenticateFailure() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");

        AuthData expectedAuthData = new AuthData("1234", token.getUsername());

        AuthData actualAuthData = authService.authenticate(token.getAuthToken());

        Assertions.assertNotNull(actualAuthData, "AuthData should not be null for a valid token");
        Assertions.assertNotEquals(expectedAuthData, actualAuthData, "Returned AuthData expected no match");
    }

    @Test
    @Order(5)
    @DisplayName("Delete - Success")
    public void testDeleteSuccess() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");
        authService.deleteAuth(token.getAuthToken());

        Assertions.assertNull(authService.authenticate(token.getAuthToken()), "token should be returning a non-null value ");

    }

    @Test
    @Order(6)
    @DisplayName("Delete - Failure")
    public void testDeleteFailure() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");
        authService.deleteAuth("1234");

        Assertions.assertNotNull(authService.authenticate(token.getAuthToken()), "token should be returning a non-null value ");

    }

    @Test
    @Order(7)
    @DisplayName("Clear - Success")
    public void testClearSuccess() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");
        authService.clear();

        Assertions.assertNull(authService.authenticate(token.getAuthToken()), "token should be returning a non-null value ");

    }
}