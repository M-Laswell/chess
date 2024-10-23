package handler;

import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;


public class LoginHandler implements Route {
    UserService userService = new UserService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var user = new Gson().fromJson(request.body(), UserData.class);
        return new Gson().toJson(userService.login(user));
    }
}