package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {
    GameDAO gameDAO = MemoryGameDAO.getInstance();
    AuthService authService = new AuthService();

    public int createGame(GameData game, String token) throws DataAccessException {
        AuthData user = authService.authenticate(token);
        if(user == null){
            throw new DataAccessException("Authentication Failure");
        }
        ChessGame newGame = new ChessGame();
        game.setGame(newGame);
        return gameDAO.createGame(game).getGameID();
    }

    public Collection<GameData> getGames(String token) throws DataAccessException {
        //Authenticate User
        AuthData user = authService.authenticate(token);
        if(user == null){
            throw new DataAccessException("Authentication Failure");
        }
        return gameDAO.listGames();
    }

    public void joinGame(String token, String color, int gameID) throws DataAccessException {
        AuthData user = authService.authenticate(token);
        if(user == null){
            throw new DataAccessException("Authentication Failure");
        }
        GameData game = gameDAO.getGame(gameID);
        if(color.equals("\"BLACK\"")){
            game.setBlackUsername(user.getUsername());
        } else if(color.equals("\"WHITE\"")){
            game.setWhiteUsername(user.getUsername());
        }
        gameDAO.updateGame(gameID, game);
    }
}
