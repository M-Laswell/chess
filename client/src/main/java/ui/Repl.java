package ui;

import static ui.EscapeSequences.*;
import java.util.Scanner;

public class Repl{
    private final PreLoginClient preLoginClient;
    private final PostLoginClient postLoginClient;
    private final GameplayClient gameplayClient;
    private Client client;

    public Repl(String serverUrl) {
        preLoginClient = new PreLoginClient(serverUrl);
        postLoginClient = new PostLoginClient(serverUrl);
        gameplayClient = new GameplayClient(serverUrl);
        client = preLoginClient;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the CHESS'R'US. Sign in to start.");
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
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}
