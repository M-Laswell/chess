package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import service.AuthService;
import service.GameService;

import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class GameDaoTests {

    GameDAO gameDAO = new MySqlGameDAO();

    @Test
    @Order(1)
    @DisplayName("Create - Success")
    public void testCreateSuccess() throws DataAccessException {
        GameData newGame = new GameData(0,null,null,"HermoinesGame",null);
        int gameID = gameDAO.createGame(newGame).getGameID();

        Assertions.assertEquals(gameID, 1, "We should get a new game with an ID");
    }

    @Test
    @Order(2)
    @DisplayName("Create - Failure")
    public void testCreateFailure() throws DataAccessException {

        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    @Order(3)
    @DisplayName("Get - Success")
    public void testGetSuccess() throws DataAccessException {
        GameData newGame = new GameData(0,null,null,"HermoinesGame",null);
        int gameID = gameDAO.createGame(newGame).getGameID();
        gameDAO.getGame(gameID);


        Assertions.assertEquals(1, gameDAO.getGame(gameID).getGameID(), "Non empty Array");
    }

    @Test
    @Order(4)
    @DisplayName("Get - Failure")
    public void testGetFailure() throws DataAccessException {
        GameData newGame = new GameData(0,null,null,"HermoinesGame",null);
        int gameID = gameDAO.createGame(newGame).getGameID();

        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.getGame(0));
    }

    @Test
    @Order(5)
    @DisplayName("List - Success")
    public void testListSuccess() throws DataAccessException {
        GameData newGame = new GameData(0,null,null,"HermoinesGame",null);
        gameDAO.createGame(newGame);
        var result = gameDAO.listGames();

        Assertions.assertNotNull(result, "should be returning a list");


    }

    @Test
    @Order(6)
    @DisplayName("List - Failure")
    public void testListFailure() throws DataAccessException {
        boolean nothingToList = false;
        var result = gameDAO.listGames();
        if (result.isEmpty()){
            nothingToList = true;
        }

        Assertions.assertEquals(true, nothingToList, " should be an empty array");



    }

    @Test
    @Order(7)
    @DisplayName("Update - Success")
    public void testUpdateSuccess() throws DataAccessException {
        GameData oldGame = new GameData(0,null,null,"HermoinesGame",null);
        int gameID = gameDAO.createGame(oldGame).getGameID();
        GameData newGame = new GameData(0,"hermoine",null,"HermoinesGame",null);
        gameDAO.updateGame(gameID, newGame);

        Assertions.assertEquals(gameDAO.getGame(gameID).getWhiteUsername(), "hermoine", "username should match");

    }

    @Test
    @Order(8)
    @DisplayName("Update - Failure")
    public void testUpdateFailure() throws DataAccessException {
        GameData newGame = new GameData(0,null,null,"HermoinesGame",null);
        int gameID = gameDAO.createGame(newGame).getGameID();


        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(gameID, null));

    }

    @Test
    @Order(9)
    @DisplayName("Clear - Success")
    public void testClearSuccess() throws DataAccessException {
        GameData newGame = new GameData(0,null,null,"HermoinesGame",null);
        int gameID = gameDAO.createGame(newGame).getGameID();
        gameDAO.clear();

        Assertions.assertNull(gameDAO.getGame(gameID), "Should be null");

    }

    @BeforeEach
    public void testClear() throws DataAccessException {

        gameDAO.clear();

    }

}
