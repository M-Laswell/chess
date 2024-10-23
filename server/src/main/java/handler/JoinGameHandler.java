package handler;

import com.google.gson.JsonObject;
import model.GameData;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class JoinGameHandler implements Route {
    GameService gameService = new GameService();
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String authorization = request.headers("authorization");
        JsonObject bodyJson = new Gson().fromJson(request.body(), JsonObject.class);
        gameService.joinGame(authorization, bodyJson.get("playerColor").toString(), Integer.parseInt(bodyJson.get("gameID").toString()));
        return new Gson().toJson("{}");
    }
}
