package handler;

import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class ListGameHandler implements Route {
    GameService gameService = new GameService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String authorization = request.headers("authorization");
        gameService.getGames(authorization);
        return new Gson().toJson(gameService.getGames(authorization));
    }
}
