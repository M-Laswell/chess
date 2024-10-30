package service;


import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

/*In addition to the HTTP server pass off tests provided in the starter code,
you need to write tests that execute directly against your service classes. These tests skip the HTTP server network communication
and will help you in the development of your service code for this phase.

Good tests extensively show that we get the expected behavior. This could be asserting that data put into the database is really there,
or that a function throws an error when it should. Write a positive and a negative JUNIT test case for each public method on your Service classes,
except for Clear which only needs a positive test case. A positive test case is one for which the action happens successfully
(e.g., successfully claiming a spot in a game). A negative test case is one for which the operation fails (e.g., trying to claim an already claimed spot).

The service unit tests must directly call the methods on your service classes.
They should not use the HTTP server pass off test code that is provided with the starter code.
Create Games, Get Games, Join Games*/

public class GameServiceTest {

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
}