package ui;

public class PostLoginClient implements Client{
    private final String serverUrl;

    public PostLoginClient(String serverUrl) {
        //server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    @Override
    public String eval(String command) {
        return "Post Login Eval";
    }

    @Override
    public String help() {
        return "Post Login Help";
    }
}
