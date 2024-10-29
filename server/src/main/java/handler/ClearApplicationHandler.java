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
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public ClearApplicationHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json");
        GameDAO game = this.gameDAO;
        AuthDAO auth = this.authDAO;
        UserDAO user = this.userDAO;

        try {
            game.clear();
            auth.clear();
            user.clear();

            return "";
        } catch (DataAccessException e) {
            System.out.println(e);
            return "";
        }
    }
}
