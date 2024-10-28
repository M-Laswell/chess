package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceTest {

    private static TestUser existingUser;

    private static TestUser newUser;

    private static TestCreateRequest createRequest;

    private static TestServerFacade serverFacade;
    private static Server server;

    private String existingAuth;


    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));

        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");

        newUser = new TestUser("NewUser", "newUserPassword", "nu@mail.com");

        createRequest = new TestCreateRequest("testGame");

    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();

        //one user already logged in
        TestAuthResult regResult = serverFacade.register(existingUser);
        existingAuth = regResult.getAuthToken();
    }

    // Test for authenticate success
    @Test
    @Order(1)
    @DisplayName("Authenticate - Success")
    public void testAuthenticateSuccess() throws DataAccessException {
        AuthData expectedAuthData = new AuthData(testToken, testUsername);
        authDAO.createAuth(expectedAuthData);  // Seed the DAO with a valid token

        AuthData actualAuthData = authService.authenticate(testToken);

        Assertions.assertNotNull(actualAuthData, "AuthData should not be null for a valid token");
        Assertions.assertEquals(expectedAuthData, actualAuthData, "Returned AuthData should match expected");
    }
}