package ui;

import exception.ResponseException;
import model.AuthData;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class PreLoginClient implements Client{
    //private final ServerFacade server;
    private final String serverUrl;
    private final Repl repl;
    private ServerFacade server;

    public PreLoginClient(String serverUrl, Repl repl) {
        //server = new ServerFacade(serverUrl);
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
                case "login" -> login(params[0], params[1]);
                case "register" -> register(params[0], params[1], params[2]);
                case "quit", "q" -> "quit";
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String login(String username, String password){
        UserData data = new UserData(username, password ,null);
        server = new ServerFacade(this.serverUrl);
        try {
            AuthData auth = server.login(data);
            this.repl.setAuthData(auth);
            this.repl.changeState(State.SIGNEDIN);
        } catch (ResponseException e) {
            System.out.println(e);
        }

        return "Logging In";
    }

    private String register(String username, String email, String password){
        UserData data = new UserData(username, password ,email);
        server = new ServerFacade(this.serverUrl);
        try {
            AuthData auth = server.register(data);
            this.repl.setAuthData(auth);
            this.repl.changeState(State.SIGNEDIN);
        } catch (ResponseException e) {
            System.out.println(e);
        }
        return "Registering Offender";
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
