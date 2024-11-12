package ui;

import java.util.Arrays;

public class PreLoginClient implements Client{
    //private final ServerFacade server;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        //server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
        return "Logging In";
    }

    private String register(String username, String email, String paswword){
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
