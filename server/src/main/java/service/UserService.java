package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.MySqlUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.AuthService;

public class UserService {
    AuthService authService;
    UserDAO userDAO;

    public UserService(UserDAO userDAO, AuthService authService){
        this.userDAO = userDAO;
        this.authService = authService;
    }


    public AuthData register(UserData user) throws DataAccessException {
        //Check if User already exists
        //Create Auth Token
        if(user.getPassword() == null) {
            throw new DataAccessException("Error: bad request");
        }
        if(user.getUsername() == null) {
            throw new DataAccessException("Error: bad request");
        }
        if(user.getEmail() == null) {
            throw new DataAccessException("Error: bad request");
        }



        if(userDAO.getUser(user.getUsername()) == null) {
            userDAO.createUser(user);
            return authService.createAuth(user.getUsername());
        }
        throw new DataAccessException("Error: already taken");
    }

    public AuthData login(UserData user) throws DataAccessException {
        //Check if user exists
        if(userDAO.getUser(user.getUsername()) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if(BCrypt.checkpw(user.getPassword(), userDAO.getUser(user.getUsername()).getPassword())) {
            return authService.createAuth(user.getUsername());
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public void logout(String token) throws DataAccessException {
        AuthData user = authService.authenticate(token);
        if(user == null){
            throw new DataAccessException("Error: unauthorized");
        }
        authService.deleteAuth(token);

    }
}
