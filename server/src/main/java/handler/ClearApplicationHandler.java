package handler;
import com.google.gson.Gson;
import dataaccess.*;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearApplicationHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        GameDAO game = new MemoryGameDAO();
        AuthDAO auth = new MemoryAuthDAO();
        UserDAO user = new MemoryUserDAO();

        game.clear();
        auth.clear();
        user.clear();

        return new Gson().toJson("{}");
    }
}
