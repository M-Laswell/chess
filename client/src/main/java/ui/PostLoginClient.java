package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import server.ServerFacade;
import websocket.WebSocketFacade;
import websocket.NotificationHandler;

import java.util.Arrays;

import static com.sun.management.HotSpotDiagnosticMXBean.ThreadDumpFormat.JSON;

public class PostLoginClient implements Client{
    private final String serverUrl;
    private ServerFacade server;
    private final Repl repl;
    private GameData[] games;
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;

    public PostLoginClient(String serverUrl, Repl repl, NotificationHandler notificationHandler) {
        this.serverUrl = serverUrl;
        this.repl = repl;
        this.notificationHandler = notificationHandler;
    }

    @Override
    public String eval(String command) {
        try {
            help();
            var tokens = command.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "creategame" -> createGame(params[0]);
                case "listgames" -> listGames();
                case "playgame" -> playGame(Integer.parseInt(params[0]),
                        ChessGame.TeamColor.valueOf(params[1].toUpperCase()));
                case "observegame", "o" -> observeGame(Integer.parseInt(params[0]));
                case "quit", "q" -> "quit";
                default -> help();
            };
        } catch (IllegalArgumentException e){

            if(e.getMessage().contains("No enum constant")){
                return "Please enter BLACK or WHITE as your color";

            } else if (e.getMessage().contains("For input string")){
                return "Please select an existing game number or listgames again for an updated list";
            } else {
                return "Command formatted incorrectly, please try again";
            }
        } catch (IndexOutOfBoundsException e) {
            return "Command formatted incorrectly, please try again";
        } catch (Exception e) {
            return switch (e.getMessage()) {
                case "failure: 403" -> "Tried to join a taken color please try again with an OPEN color " +
                        "or listgames again for an updated list";
                case "Connection refused: connect" -> "Our Servers are currently down";
                case "Invalid Game Number" -> "Please select an existing game number " +
                        "or listgames again for an updated list";
                case "failure: 401" -> "You are unauthorized please leave";
                default -> "OOPS something went wrong please try again " +
                        "if problem persists please restart the client";
            };
        }
    }
    private String logout() throws Exception{
        server = new ServerFacade(this.serverUrl);
        try {
            server.logout(this.repl.getAuthData());
            this.repl.changeState(State.SIGNEDOUT);
            return "Logged out";
        } catch (ResponseException e) {
            throw e;
        }
    }

    private String createGame(String gameName) throws Exception{
        server = new ServerFacade(this.serverUrl);
        try {
            GameData newGame = new GameData(0, null, null, gameName, null);
            server.createGame(this.repl.getAuthData(), newGame);
            return "Created game " + gameName;

        } catch (ResponseException e) {
            throw e;
        }

    }

    public void loadGames() throws Exception{
        server = new ServerFacade(this.serverUrl);
        try{
            this.games = server.listGames(this.repl.getAuthData());
        } catch (Exception e){
            throw e;
        }
    }

    private String listGames() throws Exception{
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

    private String playGame(int gameNumber, ChessGame.TeamColor teamColor) throws  Exception{
        server = new ServerFacade(this.serverUrl);
        try {
            server.joinGame(this.repl.getAuthData(), teamColor, games[gameNumber-1].getGameID());
            this.repl.changeState(State.INGAME);
            this.repl.joiningGame(games[gameNumber-1]);
            return "Joining Game #" + gameNumber;
        } catch (IndexOutOfBoundsException e) {
            throw new Exception("Invalid Game Number");
        } catch (Exception e) {
            throw e;
        }
    }

    private String observeGame(int gameNumber) throws Exception{
        try {
            if(games[gameNumber - 1] == null){
                throw new Exception("Invalid Game Number");
            }
            this.repl.changeState(State.OBSERVING);
            this.repl.joiningGame(games[gameNumber - 1]);
            return "Observing Game #" + gameNumber;
        } catch (IndexOutOfBoundsException e) {
            throw new Exception("Invalid Game Number");
        } catch (Exception e){
            throw e;
        }
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
