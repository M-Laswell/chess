package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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
        if (username == null){
            throw new DataAccessException("username cant be null");
        }
        AuthData token = new AuthData(UUID.randomUUID().toString(), username);
        return authDAO.createAuth(token);
    }

    public void deleteAuth(String token) throws DataAccessException {
        authDAO.deleteAuth(token);
    }

    public void clear() throws DataAccessException{
        authDAO.clear();
    }
}
