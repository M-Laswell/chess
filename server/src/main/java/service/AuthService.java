package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import model.AuthData;
import java.util.UUID;

public class AuthService {
    AuthDAO authDAO = MemoryAuthDAO.getInstance();

    public AuthData authenticate(String token) throws DataAccessException {
        return authDAO.getAuth(token);
    }

    public AuthData createAuth(String username) throws DataAccessException {
        AuthData token = new AuthData(UUID.randomUUID().toString(), username);
        return authDAO.createAuth(token);
    }

    public void deleteAuth(String token) throws DataAccessException {
        authDAO.deleteAuth(token);
    }
}
