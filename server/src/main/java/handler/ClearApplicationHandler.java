package handler;
import com.google.gson.Gson;
import dataaccess.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearApplicationHandler implements Route {
    private UserService userService;
    private AuthService authService;
    private GameService gameService;

    public ClearApplicationHandler(UserService userService, AuthService authService, GameService gameService){
        this.userService = userService;
        this.authService = authService;
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json");

        try {
            gameService.clear();
            authService.clear();
            userService.clear();

            return "";
        } catch (DataAccessException e) {
            System.out.println(e);
            return "";
        }
    }
}
