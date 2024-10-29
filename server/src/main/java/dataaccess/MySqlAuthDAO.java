package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAO{

    @Override
    public AuthData createAuth(AuthData token) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

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


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            //throw new DataAccessException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
