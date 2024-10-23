package handler;

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
        String authorization = request.headers("authorization");
        var gameName = new Gson().fromJson(request.body(), GameData.class);
        int gameId = gameService.createGame(gameName, authorization);
        return new Gson().toJson(Map.of("gameId", gameId));
    }
}
