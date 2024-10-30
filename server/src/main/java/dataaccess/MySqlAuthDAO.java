package dataaccess;

import model.AuthData;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MySqlAuthDAO implements AuthDAO{

    public MySqlAuthDAO() {
        try {
            DatabaseManager.configureDatabase(createStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthData createAuth(AuthData token) throws DataAccessException {
        if (token == null){
            throw new DataAccessException( "user cant be null " );
        }
        var statement = "INSERT INTO auth (uuid, username) VALUES (?, ?)";
        var id = DatabaseManager.executeUpdate(statement, token.getAuthToken(), token.getUsername());
        return token;
    }

    @Override
    public AuthData getAuth(String uuid) throws DataAccessException {
        if (uuid == null){
            throw new DataAccessException( "uuid cant be null " );
        }
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT uuid, username FROM auth WHERE uuid=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, uuid);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void deleteAuth(String uuid) throws DataAccessException {
        if (uuid == null){
            throw new DataAccessException( "uuid cant be null " );
        }
        var statement = "DELETE FROM auth WHERE uuid=?";
        DatabaseManager.executeUpdate(statement, uuid);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        DatabaseManager.executeUpdate(statement);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var uuid = rs.getString("uuid");
        var username = rs.getString("username");
        return new AuthData(uuid, username);
    }


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `uuid`  varchar(256) UNIQUE NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`uuid`),
              INDEX(uuid),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


}
