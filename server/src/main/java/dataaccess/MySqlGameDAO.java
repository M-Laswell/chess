package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlGameDAO implements GameDAO{

    public MySqlGameDAO() {
        try {
            DatabaseManager.configureDatabase(createStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {

        if (game == null){
            throw new DataAccessException(" Game cannot be null");
        }

        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, gameJSON) VALUES (?, ?, ?, ?, ?)";
        var gameJSON = new Gson().toJson(game.getGame());

        var id = DatabaseManager.executeUpdate(
                statement,
                game.getGameID(),
                game.getWhiteUsername(),
                game.getBlackUsername(),
                game.getGameName(),
                gameJSON);

        game.setGameID(id);
        return game;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {

        if (gameID == 0){
            throw new DataAccessException(" GameID cannot be null");
        }
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public GameData updateGame(int gameID, GameData game) throws DataAccessException {
        if (game == null) {
            throw new DataAccessException("game cant be null");
        }
        var statement = "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameJSON = ? WHERE gameID = ?";
        var gameJSON = new Gson().toJson(game.getGame());
        DatabaseManager.executeUpdate(statement, game.getWhiteUsername(), game.getBlackUsername(), gameJSON, gameID);
        return getGame(gameID);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        DatabaseManager.executeUpdate(statement);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int UNIQUE NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `gameJSON` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameID),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };



    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        String whiteUsername = null;
        String blackUsername = null;
        ChessGame game = null;
        if(rs.getString("whiteUsername") != null) {
            whiteUsername = rs.getString("whiteUsername");
        }
        if(rs.getString("blackUsername") != null) {
            blackUsername = rs.getString("blackUsername");
        }
        var gameName = rs.getString("gameName");
        if(rs.getString("gameJSON") != null) {
            var gameJSON = rs.getString("gameJSON");
            game  = new Gson().fromJson(gameJSON, ChessGame.class);
        }
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

}
