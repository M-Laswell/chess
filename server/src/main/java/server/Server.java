package server;
import dataaccess.*;
import handler.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;
import server.websocket.WebSocketHandler;


public class Server {
    private UserService userService;
    private AuthService authService;
    private GameService gameService;
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private WebSocketHandler webSocketHandler;

    public Server() {
        try {
            this.userDAO = new MySqlUserDAO();
            this.authDAO = new MySqlAuthDAO();
            this.gameDAO = new MySqlGameDAO();

            this.authService = new AuthService(authDAO);
            this.userService = new UserService(userDAO, authService);
            this.gameService = new GameService(gameDAO, authService);
            webSocketHandler = new WebSocketHandler(userService,authService,gameService);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", new handler.RegisterHandler(userService));
        Spark.post("/session", new handler.LoginHandler(userService));
        Spark.delete("/session", new handler.LogoutHandler(userService));
        Spark.get("/game", new handler.ListGameHandler(gameService));
        Spark.post("/game", new handler.CreateGameHandler(gameService));
        Spark.put("/game", new handler.JoinGameHandler(gameService));
        Spark.delete("/db", new handler.ClearApplicationHandler(userService, authService, gameService));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
