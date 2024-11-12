package ui;

public class GameplayClient implements Client{
    private final String serverUrl;
    private final Repl repl;

    public GameplayClient(String serverUrl, Repl repl) {
        //server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    @Override
    public String eval(String command) {
        return "Gameplay Eval";
    }

    @Override
    public String help() {
        return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
    }
}
