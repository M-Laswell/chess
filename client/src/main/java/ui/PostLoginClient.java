package ui;

import chess.ChessGame;
import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

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
                case "playgame" -> playGame(params[0], ChessGame.TeamColor.valueOf(params[1].toUpperCase()));
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
        return "Create Game";
    }

    private String listGames(){
        server = new ServerFacade(this.serverUrl);
        try {
            System.out.println(server.listGames(this.repl.getAuthData()));
        } catch (ResponseException e) {
            System.out.println(e);
        }
        return "List Games";
    }

    private String playGame(String gameID, ChessGame.TeamColor teamColor){
        this.repl.changeState(State.INGAME);
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
