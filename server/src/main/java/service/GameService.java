package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MySqlGameDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthService authService;

    public GameService(GameDAO gameDAO, AuthService authService){
        this.gameDAO = gameDAO;
        this.authService = authService;

    }

    public int createGame(GameData game, String token) throws DataAccessException {
        AuthData user = authService.authenticate(token);
        if(user == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if(game.getGameName() == null){
            throw new DataAccessException("Error: bad request");
        }
        ChessGame newGame = new ChessGame();
        game.setGame(newGame);
        return gameDAO.createGame(game).getGameID();
    }

    public Collection<GameData> getGames(String token) throws DataAccessException {
        //Authenticate User
        AuthData user = authService.authenticate(token);
        if(user == null){
            throw new DataAccessException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }

    public void joinGame(String token, ChessGame.TeamColor color, Integer gameID) throws DataAccessException {
        AuthData user = authService.authenticate(token);
        if(user == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if(gameID == null || gameID == 0){
            throw new DataAccessException("Error: bad request");
        }
        GameData game = gameDAO.getGame(gameID);
        if(game == null){
            throw new DataAccessException("Error: bad request");
        }
        if(color.equals(ChessGame.TeamColor.BLACK)){
            if(game.getBlackUsername() != null){
                throw new DataAccessException("Error: already taken");
            }
            game.setBlackUsername(user.getUsername());
        } else if(color.equals(ChessGame.TeamColor.WHITE)){
            if(game.getWhiteUsername() != null){
                throw new DataAccessException("Error: already taken");
            }
            game.setWhiteUsername(user.getUsername());
        } else {
            throw new DataAccessException("Error: bad request");
        }
        gameDAO.updateGame(gameID, game);
    }
}
