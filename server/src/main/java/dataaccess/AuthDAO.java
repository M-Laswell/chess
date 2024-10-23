package dataaccess;

import model.AuthData;

import java.util.Collection;

public interface AuthDAO {
    AuthData createAuth(AuthData token) throws DataAccessException;
    AuthData getAuth(String token) throws DataAccessException;
    void deleteAuth(String token) throws DataAccessException;
    void clear() throws DataAccessException;
}
