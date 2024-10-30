package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;


import java.util.UUID;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthDaoTests {


    AuthDAO authDAO = new MySqlAuthDAO();

    @Test
    @Order(1)
    @DisplayName("Create - Success")
    public void testCreateSuccess() throws DataAccessException {
        AuthData token = new AuthData(UUID.randomUUID().toString(), "Hermoine");
        authDAO.createAuth(token);
        AuthData tokenGot = authDAO.getAuth(token.getAuthToken());

        Assertions.assertNotNull(tokenGot, "token should be returning a non-null value ");

    }

    @Test
    @Order(2)
    @DisplayName("Create - Failure")
    public void testCreateFailure() throws DataAccessException {

        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(null));
    }

    @Test
    @Order(3)
    @DisplayName("Get - Success")
    public void testAuthenticateSuccess() throws DataAccessException {
        AuthData token = new AuthData(UUID.randomUUID().toString(), "Hermoine");
        authDAO.createAuth(token);
        AuthData tokenGot = authDAO.getAuth(token.getAuthToken());

        Assertions.assertNotNull(tokenGot, "token should be returning a non-null value ");
    }

    @Test
    @Order(4)
    @DisplayName("Get - Failure")
    public void testAuthenticateFailure() throws DataAccessException {
        AuthData token = new AuthData(UUID.randomUUID().toString(), "Hermoine");
        authDAO.createAuth(token);

        Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuth(null));
    }

    @Test
    @Order(5)
    @DisplayName("Delete - Success")
    public void testDeleteSuccess() throws DataAccessException {
        AuthData token = new AuthData(UUID.randomUUID().toString(), "Hermoine");
        authDAO.createAuth(token);
        authDAO.deleteAuth(token.getAuthToken());
        AuthData tokenGot = authDAO.getAuth(token.getAuthToken());

        Assertions.assertNull(tokenGot, "token should be returning a non-null value ");

    }

    @Test
    @Order(6)
    @DisplayName("Delete - Failure")
    public void testDeleteFailure() throws DataAccessException {
        AuthData token = new AuthData(UUID.randomUUID().toString(), "Hermoine");
        authDAO.createAuth(token);

        Assertions.assertThrows(DataAccessException.class, () -> authDAO.deleteAuth(null));

    }

    @BeforeEach
    public void testClear() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    @Order(7)
    @DisplayName("Clear - Success")
    public void testClearSuccess() throws DataAccessException {
        AuthData token = new AuthData(UUID.randomUUID().toString(), "Hermoine");
        authDAO.createAuth(token);

        authDAO.clear();

        AuthData tokenGot = authDAO.getAuth(token.getAuthToken());

        Assertions.assertNull(tokenGot, "token should be returning a non-null value ");


    }

}
