package handler;

import dataaccess.DataAccessException;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

import java.util.Map;

public class LogoutHandler implements Route {
    UserService userService = new UserService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json");
        String authorization = request.headers("authorization");
        try {
            userService.logout(authorization);
            return "";
        } catch (DataAccessException e) {
            response.status(401);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}
