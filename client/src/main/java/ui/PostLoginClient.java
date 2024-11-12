package ui;

import chess.ChessGame;

import java.util.Arrays;

import static java.lang.String.valueOf;

public class PostLoginClient implements Client{
    private final String serverUrl;

    public PostLoginClient(String serverUrl) {
        //server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
        return "Logging Out";
    }
    private String createGame(String gameName){
        return "Create Game";
    }

    private String listGames(){
        return "List Games";
    }

    private String playGame(String gameID, ChessGame.TeamColor teamColor){
        return "Shall We Play A Game?";
    }

    private String observeGame(String gameID){
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
