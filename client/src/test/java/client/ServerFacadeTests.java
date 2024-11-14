package client;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
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
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var serverUrl = "http://localhost:" + server.port();
        facade = new ServerFacade(serverUrl);

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach @AfterEach
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
        facade.register(data);
        var authData = facade.login(data);
        assertTrue(authData.getAuthToken().length() > 10);

    }

    @Test
    void badLogin() throws Exception {
        UserData goodData = new UserData("username", "password" ,"email@email.org");
        UserData data = new UserData("username", null ,"email9@email.org");
        facade.register(goodData);
        assertThrows(ResponseException.class ,() -> facade.login(data));

    }

    @Test
    void logout() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        assertDoesNotThrow(() -> facade.logout(authData));

    }

    @Test
    void badLogout() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        AuthData badAuthData = null;
        assertThrows(ResponseException.class ,() -> facade.logout(badAuthData));

    }

    @Test
    void listGames() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        assertDoesNotThrow(() -> facade.listGames(authData));
    }

    @Test
    void badListGames() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        AuthData badAuthData = null;
        assertThrows(ResponseException.class ,() -> facade.listGames(badAuthData));
    }

    @Test
    void createGame() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        GameData newGame = new GameData(1, null, null, "wild wild west", null);
        assertDoesNotThrow(() -> facade.createGame(authData, newGame));
    }

    @Test
    void badCreateGame() throws Exception {
        UserData data = new UserData("username", "password" ,"email9@email.org");
        var authData = facade.register(data);
        GameData newGame = new GameData(1, null, null, "wild wild west", null);
        facade.createGame(authData, newGame);
        GameData newGame2 = new GameData(1, null, null, "wild wild west", null);
        assertThrows(ResponseException.class ,() -> facade.createGame(authData, newGame2));

    }

    @Test
    void joinGame() throws Exception {
        UserData data = new UserData("username", "password" ,"email@email.org");
        var authData = facade.register(data);
        GameData newGame = new GameData(1, null, null, "wild wild west", null);
        facade.createGame(authData, newGame);
        assertDoesNotThrow(() -> facade.joinGame(authData, ChessGame.TeamColor.WHITE, 1));
    }

    @Test
    void badJoinGame() throws Exception {
        UserData data = new UserData("username", "password" ,"email9@email.org");
        var authData = facade.register(data);
        GameData newGame = new GameData(1, null, null, "wild wild west", null);
        facade.createGame(authData, newGame);
        assertThrows(ResponseException.class ,() -> facade.joinGame(authData, ChessGame.TeamColor.WHITE, 10));
    }



}
