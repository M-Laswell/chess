package client;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import ui.Repl;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        var serverUrl = "http://localhost:8080";
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(serverUrl);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @AfterEach
    void clearServer() throws Exception{
        facade.clearApplication();
    }

    @Test
    void register() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        assertTrue(authData.getAuthToken().length() > 10);

    }

    @Test
    void badRegister() throws Exception {
        UserData data = new UserData("username", null ,"email9@email.org");
        assertThrows(ResponseException.class ,() -> facade.register(data));
    }

    @Test
    void login() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        assertTrue(authData.getAuthToken().length() > 10);

    }

    @Test
    void badLogin() throws Exception {
        UserData data = new UserData("username", null ,"email9@email.org");
        //var authData = facade.register(data);
        assertThrows(ResponseException.class ,() -> facade.register(data));

    }

    @Test
    void logout() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        assertTrue(authData.getAuthToken().length() > 10);

    }

    @Test
    void badLogout() throws Exception {
        UserData data = new UserData("username", null ,"email9@email.org");
        //var authData = facade.register(data);
        assertThrows(ResponseException.class ,() -> facade.register(data));

    }

    @Test
    void listGames() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        assertTrue(authData.getAuthToken().length() > 10);

    }

    @Test
    void badListGames() throws Exception {
        UserData data = new UserData("username", null ,"email9@email.org");
        //var authData = facade.register(data);
        assertThrows(ResponseException.class ,() -> facade.register(data));

    }

    @Test
    void createGame() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        assertTrue(authData.getAuthToken().length() > 10);

    }

    @Test
    void badCreateGame() throws Exception {
        UserData data = new UserData("username", null ,"email9@email.org");
        //var authData = facade.register(data);
        assertThrows(ResponseException.class ,() -> facade.register(data));

    }

    @Test
    void joinGame() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        assertTrue(authData.getAuthToken().length() > 10);

    }

    @Test
    void badJoinGame() throws Exception {
        UserData data = new UserData("username", null ,"email9@email.org");
        //var authData = facade.register(data);
        assertThrows(ResponseException.class ,() -> facade.register(data));

    }



}
