package handler;

import chess.ChessPiece;
import dataaccess.DataAccessException;
import model.UserData;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;
import service.UserService;

import java.util.Map;

public class RegisterHandler implements Route {
    UserService userService = new UserService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json");
        var user = new Gson().fromJson(request.body(), UserData.class);

        try {
            return new Gson().toJson(userService.register(user));
        } catch (DataAccessException e) {
            switch (e.getMessage()) {
                case "Error: bad request" -> response.status(400);
                case "Error: already taken" -> response.status(403);
                default -> response.status(500);
            };
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}
