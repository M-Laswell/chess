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

import java.util.Map;

public class JoinGameHandler implements Route {
    private GameService gameService;

    public JoinGameHandler(GameService gameService){
        this.gameService = gameService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.type("application/json");
        String authorization = request.headers("authorization");
        JsonObject bodyJson = new Gson().fromJson(request.body(), JsonObject.class);
        try {
            if(bodyJson.get("playerColor") == null){
                throw new DataAccessException("Error: bad request");
            }
            if(bodyJson.get("gameID") == null){
                throw new DataAccessException("Error: bad request");
            }
            gameService.joinGame(authorization, ChessGame.TeamColor.valueOf(bodyJson.get("playerColor").getAsString()),
                    Integer.parseInt(String.valueOf(bodyJson.get("gameID"))));
            return "";
        } catch (DataAccessException e) {
            switch (e.getMessage()) {
                case "Error: bad request" -> response.status(400);
                case "Error: unauthorized" -> response.status(401);
                case "Error: already taken" -> response.status(403);
                default -> response.status(500);
            };
            return new Gson().toJson(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            response.status(400);
            return new Gson().toJson(Map.of("message", e.getMessage()));
        }
    }
}
