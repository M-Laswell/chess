package server;
import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData login(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData logout(AuthData auth) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", path, null, null, auth);
    }

    public GameData[] listGames(AuthData auth) throws ResponseException {
        var path = "/game";
        record ListGameResponse(GameData[] games) {
        }
        var response = this.makeRequest("GET", path, null, ListGameResponse.class, auth);
        return response.games;
    }

    public GameData createGame(AuthData auth, GameData gameData) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, gameData, GameData.class, auth);
    }

    public GameData joinGame(AuthData auth, ChessGame.TeamColor color, int gameID) throws ResponseException {
        var path = "/game";
        Map<String,Object> game = new HashMap<>();
        game.put("playerColor", color.toString());
        game.put("gameID", gameID);
        return this.makeRequest("PUT", path, game, GameData.class, auth);
    }

    public void clearApplication() throws ResponseException{
        var path = "/db";
        this.makeRequest("DELETE", path, null , null, null);

    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, AuthData authData) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if(authData != null) {
                http.addRequestProperty("Authorization", authData.getAuthToken());
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
