package ui;

public class PreLoginClient implements Client{
    //private final ServerFacade server;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        //server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    @Override
    public String eval(String command){
        return "evaluating";
    }

    @Override
    public String help(){
        return "help yo self";
    }
}
