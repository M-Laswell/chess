package handler;

import dataaccess.DataAccessException;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

import java.util.Map;


public class LoginHandler implements Route {
    private UserService userService;

    public LoginHandler(UserService userService){
        this.userService = userService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json");
        var user = new Gson().fromJson(request.body(), UserData.class);
        try {
            return new Gson().toJson(userService.login(user));
        } catch (DataAccessException e) {
            response.status(401);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}