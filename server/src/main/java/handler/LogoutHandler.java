package handler;

import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class LogoutHandler implements Route {
    UserService userService = new UserService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String authorization = request.headers("authorization");
        userService.logout(authorization);
        return new Gson().toJson("{}");
    }
}
