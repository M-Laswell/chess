package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.AuthService;

public class UserService {
    AuthService authService = new AuthService();

    public AuthData register(UserData user) throws DataAccessException {
        //Check if User already exists
        //Create Auth Token
        var auth = authService.createAuth(user.getUsername());
        return auth;
    }

    public AuthData login(UserData user) {
        AuthData auth = new AuthData("123",user.getUsername());

        return auth;
    }

    public void logout(AuthData auth) {

    }
}
