package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import server.ServerFacade;
import java.util.Arrays;

public class PostLoginClient implements Client{
    private final String serverUrl;
    private ServerFacade server;
    private final Repl repl;
    private GameData[] games;

    public PostLoginClient(String serverUrl, Repl repl) {
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
                case "observegame", "o" -> observeGame(params[0]);
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
        try {
            GameData newGame = new GameData(0, null, null, gameName, null);
            server.createGame(this.repl.getAuthData(), newGame);
            return "Created game " + gameName;

        } catch (ResponseException e) {
            System.out.println(e);
        }
        return "Tried to create game " + gameName + "but failed";
    }

    public void loadGames(){
        server = new ServerFacade(this.serverUrl);
        try{
            this.games = server.listGames(this.repl.getAuthData());
        } catch (Exception e){
            System.out.println("Error Loading Games");
        }
    }

    private String listGames() {
        loadGames();
        StringBuilder gamesList = new StringBuilder();
        for (int i = 0; i < this.games.length; i++) {
            GameData game = this.games[i];
            int gameNumber = i + 1;
            String whitePlayer = "OPEN";
            String blackPlayer = "OPEN";
            if (game.getWhiteUsername() != null) {
                whitePlayer = game.getWhiteUsername();
            }
            if (game.getBlackUsername() != null) {
                blackPlayer = game.getBlackUsername();
            }
            gamesList.append(" Game #: " + gameNumber);
            gamesList.append("\t Game Name: " + game.getGameName());
            gamesList.append("\t White Player: " + whitePlayer);
            gamesList.append("\t\t Black Player: " + blackPlayer);
            gamesList.append("\n");
        }
        return gamesList.toString();
    }

    private String playGame(int gameNumber, ChessGame.TeamColor teamColor){
        server = new ServerFacade(this.serverUrl);
        try {
            server.joinGame(this.repl.getAuthData(), teamColor, games[gameNumber-1].getGameID());
            this.repl.changeState(State.INGAME);
            return "Joining Game #" + gameNumber;
        } catch (ResponseException e) {
            System.out.println(e);
        }
        return "Failed to join Game # " + gameNumber;
    }

    private String observeGame(String gameID){
        this.repl.changeState(State.OBSERVING);
        return "Observing Game #" + gameID;
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
