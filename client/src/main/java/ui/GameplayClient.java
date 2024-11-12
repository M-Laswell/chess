package ui;

public class GameplayClient implements Client{
    private final String serverUrl;

    public GameplayClient(String serverUrl) {
        //server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
