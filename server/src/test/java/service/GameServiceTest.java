package service;


/*In addition to the HTTP server pass off tests provided in the starter code,
you need to write tests that execute directly against your service classes.
These tests skip the HTTP server network communication
and will help you in the development of your service code for this phase.

Good tests extensively show that we get the expected behavior.
This could be asserting that data put into the database is really there,
or that a function throws an error when it should.
Write a positive and a negative JUNIT test case for each public method on your Service classes,
except for Clear which only needs a positive test case.
A positive test case is one for which the action happens successfully
(e.g., successfully claiming a spot in a game).
A negative test case is one for which the operation fails (e.g., trying to claim an already claimed spot).

The service unit tests must directly call the methods on your service classes.
They should not use the HTTP server pass off test code that is provided with the starter code.
Create Games, Get Games, Join Games, Clear*/

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTest {


    AuthDAO authDAO = new MemoryAuthDAO();
    AuthService authService = new AuthService(authDAO);
    GameDAO gameDAO = new MemoryGameDAO();
    GameService gameService = new GameService(gameDAO, authService);

    @Test
    @Order(1)
    @DisplayName("Create - Success")
    public void testCreateSuccess() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");
        GameData newGame = new GameData(0,null,null,"HermoinesGame",null);
        int gameID = gameService.createGame(newGame, token.getAuthToken());

        Assertions.assertEquals(gameID, 1, "We should get a new game with an ID");
    }

    @Test
    @Order(2)
    @DisplayName("Create - Failure")
    public void testCreateFailure() throws DataAccessException {

        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(null,null));
    }

    @Test
    @Order(3)
    @DisplayName("Get - Success")
    public void testGetSuccess() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");
        GameData newGame = new GameData(0,null,null,"HermoinesGame",null);
        int gameID = gameService.createGame(newGame, token.getAuthToken());
        gameService.getGames(token.getAuthToken());


        Assertions.assertEquals(1, gameService.getGames(token.getAuthToken()).size(), "Non empty Array");

    }

    @Test
    @Order(4)
    @DisplayName("Get - Failure")
    public void testGetFailure() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");
        gameService.getGames(token.getAuthToken());


        Assertions.assertNotEquals(1, gameService.getGames(token.getAuthToken()).size(), "Non empty Array");
    }

    @Test
    @Order(5)
    @DisplayName("Join - Success")
    public void testJoinSuccess() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");
        GameData newGame = new GameData(0,null,null,"HermoinesGame",null);
        int gameID = gameService.createGame(newGame, token.getAuthToken());
        gameService.joinGame(token.getAuthToken(), ChessGame.TeamColor.WHITE, gameID);

        Assertions.assertEquals(gameDAO.getGame(gameID).getWhiteUsername(), "hermoine", "username should match");

    }

    @Test
    @Order(6)
    @DisplayName("Join - Failure")
    public void testJoinFailure() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");
        GameData newGame = new GameData(0,null,null,"HermoinesGame",null);
        int gameID = gameService.createGame(newGame, token.getAuthToken());


        Assertions.assertThrows(DataAccessException.class, () -> gameService.joinGame(token.getAuthToken(), ChessGame.TeamColor.WHITE, 7));

    }

    @Test
    @Order(7)
    @DisplayName("Clear - Success")
    public void testClearSuccess() throws DataAccessException {
        AuthData token = authService.createAuth("hermoine");
        GameData newGame = new GameData(0,null,null,"HermoinesGame",null);
        int gameID = gameService.createGame(newGame, token.getAuthToken());
        gameService.clear();

        Assertions.assertNull(gameDAO.getGame(gameID), "Should be null");

    }

}
