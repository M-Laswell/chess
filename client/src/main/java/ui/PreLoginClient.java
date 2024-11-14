package ui;

import exception.ResponseException;
import model.AuthData;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class PreLoginClient implements Client{
    private final String serverUrl;
    private final Repl repl;
    private ServerFacade server;

    public PreLoginClient(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    @Override
    public String eval(String command){
        try {
            var tokens = command.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login", "l" -> login(params[0], params[1]);
                case "register" -> register(params[0], params[1], params[2]);
                case "quit", "q" -> "quit";
                default -> help();
            };
        } catch (Exception e) {
            return switch (e.getMessage()) {
                case "Connection refused: connect" -> "Our Servers are currently down";
                case "failure: 403" -> "Username already taken choose a different one";
                case "failure: 401" -> "Incorrect Username or Password are you sure you registered?";
                default -> "Command formatted incorrectly, please try again";
            };
        }
    }

    private String login(String username, String password) throws Exception{
        try {
            UserData data = new UserData(username, password ,null);
            server = new ServerFacade(this.serverUrl);
            AuthData auth = server.login(data);
            this.repl.setAuthData(auth);
            this.repl.changeState(State.SIGNEDIN);
            return "Logged in as " + username;
        } catch (ResponseException e) {
            throw e;
        }
    }

    private String register(String username, String email, String password) throws Exception{
        UserData data = new UserData(username, password ,email);
        server = new ServerFacade(this.serverUrl);
        try {
            AuthData auth = server.register(data);
            this.repl.setAuthData(auth);
            this.repl.changeState(State.SIGNEDIN);
            return "Logged in as " + username;
        } catch (ResponseException e) {
            throw e;
        }
    }

    @Override
    public String help(){
        return """
                    - help - lists all possible commands
                    - quit - ends the client
                    - login <username> <password> - logins the user
                    - register <username> <email> <password> - registers a new user
                    """;
    }

}
