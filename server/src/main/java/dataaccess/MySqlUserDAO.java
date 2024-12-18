package dataaccess;


import model.UserData;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MySqlUserDAO implements UserDAO {

    public MySqlUserDAO() {
        try {
            DatabaseManager.configureDatabase(createStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData createUser(UserData user) throws DataAccessException {

        if(user.getPassword() == null) {
            throw new DataAccessException("Error: bad request");
        }
        if(user.getUsername() == null) {
            throw new DataAccessException("Error: bad request");
        }
        if(user.getEmail() == null) {
            throw new DataAccessException("Error: bad request");
        }

        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        var id = DatabaseManager.executeUpdate(statement, user.getUsername(), user.getPassword(), user.getEmail());
        return user;

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {

        if(username == null) {
            throw new DataAccessException("Error: bad request");
        }

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
        DatabaseManager.executeUpdate(statement);

    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) UNIQUE NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(username),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var hashedPassword = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, hashedPassword, email);
    }

}
