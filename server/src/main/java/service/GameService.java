package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.GameData;
import service.AuthService;

public class GameService {
    GameDAO gameDAO = new MemoryGameDAO();
    AuthService authService = new AuthService();

    public int createGame(GameData game, String token) throws DataAccessException {
        String username = authService.authenticate(token).getUsername();
        ChessGame newGame = new ChessGame();
        game.setWhiteUsername(username);
        game.setGame(newGame);
        return gameDAO.createGame(game).getGameID();
    }
}
