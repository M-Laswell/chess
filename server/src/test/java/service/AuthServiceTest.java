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

    // Test for authenticate success
    @Test
    @Order(1)
    @DisplayName("Authenticate - Success")
    public void testAuthenticateSuccess() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");

        AuthData expectedAuthData = new AuthData(token.getAuthToken(), token.getUsername());

        AuthData actualAuthData = authService.authenticate(token.getAuthToken());

        Assertions.assertNotNull(actualAuthData, "AuthData should not be null for a valid token");
        Assertions.assertEquals(expectedAuthData, actualAuthData, "Returned AuthData should match expected");
    }
}