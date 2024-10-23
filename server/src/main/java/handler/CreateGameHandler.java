package handler;

import dataaccess.DataAccessException;
import model.GameData;
import model.UserData;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;
import service.GameService;

import java.util.Map;


public class CreateGameHandler implements Route {
    GameService gameService = new GameService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json");
        String authorization = request.headers("authorization");
        var gameName = new Gson().fromJson(request.body(), GameData.class);
        try {
            int gameId = gameService.createGame(gameName, authorization);
            return new Gson().toJson(Map.of("gameID", gameId));
        } catch (DataAccessException e) {
            System.out.println(e);
            return null;
        }
    }
}
