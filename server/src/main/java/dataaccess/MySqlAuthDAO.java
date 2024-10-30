package dataaccess;

import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

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
        var statement = "INSERT INTO auth (uuid, username) VALUES (?, ?)";
        var id = executeUpdate(statement, token.getAuthToken(), token.getUsername());
        return token;
    }

    @Override
    public AuthData getAuth(String uuid) throws DataAccessException {
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
        var statement = "DELETE FROM auth WHERE uuid=?";
        executeUpdate(statement, uuid);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var uuid = rs.getString("uuid");
        var username = rs.getString("username");
        return new AuthData(uuid, username);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof AuthData p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
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
