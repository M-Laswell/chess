package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MySqlAuthDAO;
import model.AuthData;
import java.util.UUID;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO){
        this.authDAO = authDAO;
    }

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
