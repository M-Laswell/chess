package handler;

import com.google.gson.GsonBuilder;
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
        String authorization = request.headers("authorization");
        gameService.getGames(authorization);
        var games = gameService.getGames(authorization);
        return new GsonBuilder().serializeNulls().create().toJson(games);
    }
}
