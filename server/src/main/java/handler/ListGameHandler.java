package handler;

import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import model.GameData;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

import java.util.Map;

public class ListGameHandler implements Route {
    GameService gameService = new GameService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json");
        String authorization = request.headers("authorization");
        try {
            gameService.getGames(authorization);
            var games = gameService.getGames(authorization);
            return new GsonBuilder().serializeNulls().create().toJson(Map.of("games", games));
        } catch (DataAccessException e) {
            System.out.println(e);
        }
        return null;
    }
}
