package handler;

import chess.ChessGame;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
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
        response.type("application/json");
        String authorization = request.headers("authorization");
        JsonObject bodyJson = new Gson().fromJson(request.body(), JsonObject.class);
        try {
            gameService.joinGame(authorization, ChessGame.TeamColor.valueOf(bodyJson.get("playerColor").getAsString()), Integer.parseInt(String.valueOf(bodyJson.get("gameID"))));
            return "";
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            return null;
        }
    }
}
