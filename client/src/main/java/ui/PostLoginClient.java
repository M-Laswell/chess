package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ResponseException;
import model.GameData;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Map;

import static java.lang.String.valueOf;

public class PostLoginClient implements Client{
    private final String serverUrl;
    private ServerFacade server;
    private final Repl repl;

    public PostLoginClient(String serverUrl, Repl repl) {
        //server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    @Override
    public String eval(String command) {
        try {
            var tokens = command.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "creategame" -> createGame(params[0]);
                case "listgames" -> listGames();
                case "playgame" -> playGame(Integer.parseInt(params[0]), ChessGame.TeamColor.valueOf(params[1].toUpperCase()));
                case "observegame" -> observeGame(params[0]);
                case "quit", "q" -> "quit";
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    private String logout(){
        server = new ServerFacade(this.serverUrl);
        try {
            System.out.println(this.repl.getAuthData());
            server.logout(this.repl.getAuthData());
            this.repl.changeState(State.SIGNEDOUT);
        } catch (ResponseException e) {
            System.out.println(e);
        }
        return "Logging Out";
    }
    private String createGame(String gameName){
        server = new ServerFacade(this.serverUrl);
        GameData newGame = new GameData(0, null, null, gameName, null);
        try {
            server.createGame(this.repl.getAuthData(), newGame);
        } catch (ResponseException e) {
            System.out.println(e);
        }
        return "Create Game";
    }

    private String listGames(){
        server = new ServerFacade(this.serverUrl);
        try {
            var games = server.listGames(this.repl.getAuthData());
            for(GameData game : games){
                System.out.print(" Game ID: " + game.getGameID());
                System.out.print("\t Game Name: " + game.getGameName());
                System.out.print("\t White Player: " + game.getWhiteUsername());
                System.out.println("\t Black Player: " + game.getBlackUsername());
            }

        } catch (ResponseException e) {
            System.out.println(e);
        }
        return "List Games";
    }

    private String playGame(int gameID, ChessGame.TeamColor teamColor){
        server = new ServerFacade(this.serverUrl);
        try {
            server.joinGame(this.repl.getAuthData(), teamColor, gameID);
            this.repl.changeState(State.INGAME);
        } catch (ResponseException e) {
            System.out.println(e);
        }
        return "Shall We Play A Game?";
    }

    private String observeGame(String gameID){
        this.repl.changeState(State.OBSERVING);
        return "Observing Game";
    }

    @Override
    public String help() {
        return """
                    - help - lists all possible commands
                    - quit - ends the client
                    - logout - logs out the user
                    - creategame <gamename> - creates a game with the user inputted gamename
                    - listgames - lists all the games that currently exist on the server
                    - playgame <gameid, desiredcolor> - joins game at gameid with color desiredcolor
                    - observegame <gameid> - observes the game at gameid
                    
                    """;
    }
}
