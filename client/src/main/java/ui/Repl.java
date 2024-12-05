package ui;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;
import java.util.Scanner;

public class Repl implements NotificationHandler {
    private final PreLoginClient preLoginClient;
    private final PostLoginClient postLoginClient;
    private final GameplayClient gameplayClient;
    private Client client;
    private Client gameplayClient1;
    private State state;
    private AuthData authData;

    public Repl(String serverUrl) {
        preLoginClient = new PreLoginClient(serverUrl, this);
        postLoginClient = new PostLoginClient(serverUrl, this, this);
        gameplayClient = new GameplayClient(serverUrl, this);
        client = preLoginClient;
        gameplayClient1 = gameplayClient;
        state = State.SIGNEDOUT;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the CHESS'R'US. Sign in to start. \uD83D\uDC36");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }



    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[" + this.state + "] >>> " + SET_TEXT_COLOR_GREEN);
    }

    public void changeState(State newState) {
        this.state = newState;
        switch (newState) {
            case State.SIGNEDIN -> loggedIn();
            case State.SIGNEDOUT -> this.client = preLoginClient;
            case State.INGAME, State.OBSERVING -> this.client = gameplayClient;
            default -> this.client = postLoginClient;
        } ;
    }

    private void loggedIn() {
        try{
        this.client = postLoginClient;
        this.postLoginClient.loadGames();
        } catch (Exception e) {
            System.out.println("There was an error loading the list of games");
        }
    }

    public void joiningGame(GameData game){
        this.gameplayClient.setChessGame(game);
        this.gameplayClient.upgradeToWebsocket();
    }

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public State getState() {
        return state;
    }

    @Override
    public void notify(ServerMessage notification) {
        String message = null;
        switch(notification.getServerMessageType()){
            case NOTIFICATION -> message = notification.getMessage();
            case ERROR -> message = notification.getErrorMessage();
            case LOAD_GAME -> message = loadGame(notification);
            default -> message = "You fool How?";
        }
        System.out.println(SET_TEXT_COLOR_RED + message);
        printPrompt();
    }

    private String loadGame(ServerMessage notification){
        this.gameplayClient.chessGame = notification.getGame();
        return this.gameplayClient.printChessboard(null);
    }

}
