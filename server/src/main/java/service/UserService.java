package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.AuthService;

public class UserService {
    AuthService authService = new AuthService();
    UserDAO userDAO = MemoryUserDAO.getInstance();

    public AuthData register(UserData user) throws DataAccessException {
        //Check if User already exists
        //Create Auth Token
        if(userDAO.getUser(user.getUsername()) == null) {
            userDAO.createUser(user);
            return authService.createAuth(user.getUsername());
        }
        return null;
    }

    public AuthData login(UserData user) throws DataAccessException {
        //Check if user exists
        if(userDAO.getUser(user.getUsername()).getPassword().equals(user.getPassword())) {
            return authService.createAuth(user.getUsername());
        }
        return null;
    }

    public void logout(AuthData auth) {

    }
}
