package service;

import model.AuthData;
import model.UserData;

public class UserService {
    public AuthData register(UserData user) {
        AuthData auth = new AuthData("123", user.getUsername());

        return auth;
    }

    public AuthData login(UserData user) {
        AuthData auth = new AuthData("123",user.getUsername());

        return auth;
    }

    public void logout(AuthData auth) {

    }
}
