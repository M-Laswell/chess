package server;
import dataaccess.*;
import handler.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;


public class Server {
    private UserService userService;
    private AuthService authService;
    private GameService gameService;
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public Server() {
        this.userDAO = new MemoryUserDAO();
        this.authDAO = new MemoryAuthDAO();
        this.gameDAO = new MemoryGameDAO();

        this.authService = new AuthService(authDAO);
        this.userService = new UserService(userDAO, authService);
        this.gameService = new GameService(gameDAO, authService);
    }



    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", new handler.RegisterHandler(userService));
        Spark.post("/session", new handler.LoginHandler(userService));
        Spark.delete("/session", new handler.LogoutHandler(userService));
        Spark.get("/game", new handler.ListGameHandler(gameService));
        Spark.post("/game", new handler.CreateGameHandler(gameService));
        Spark.put("/game", new handler.JoinGameHandler(gameService));
        Spark.delete("/db", new handler.ClearApplicationHandler(userDAO, authDAO, gameDAO));

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
