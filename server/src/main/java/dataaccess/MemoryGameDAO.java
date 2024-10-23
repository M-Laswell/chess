package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO{
    private static MemoryGameDAO instance;
    private int nextID = 1;
    private final HashMap<Integer, GameData> games = new HashMap<>();

    public static MemoryGameDAO getInstance(){
        if (instance == null) {
            instance = new MemoryGameDAO();
        }
        return instance;
    }


    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        game.setGameID(nextID++);
        games.put(game.getGameID(), game);
        return game;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public GameData updateGame(int gameID, GameData game) throws DataAccessException {
        games.replace(gameID, game);
        return games.get(gameID);
    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        games.remove(gameID);

    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();



    }
}
