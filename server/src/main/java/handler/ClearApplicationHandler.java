package handler;
import com.google.gson.Gson;
import dataaccess.*;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearApplicationHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json");
        GameDAO game = MemoryGameDAO.getInstance();
        AuthDAO auth = MemoryAuthDAO.getInstance();
        UserDAO user = MemoryUserDAO.getInstance();

        try {
            game.clear();
            auth.clear();
            user.clear();

            return "";
        } catch (DataAccessException e) {
            System.out.println(e);
            return null;
        }
    }
}
