package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO{
    private int nextID = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        game.setGameID(nextID++);
        games.put(game.getGameID(), game);
        return game;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public GameData updateGame(int gameID, ChessGame game) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {



    }
}
