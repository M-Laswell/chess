package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    GameData updateGame(int gameID, GameData game) throws DataAccessException;
    void deleteGame(int gameID) throws DataAccessException;
    void clear() throws DataAccessException;
}
