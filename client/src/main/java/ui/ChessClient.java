package ui;

public class ChessClient {
    //private final ServerFacade server;
    private final String serverUrl;

    public ChessClient(String serverUrl) {
        //server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String command){
        return "evaluating";
    }

    public String help(){
        return "help yo self";
    }
}
