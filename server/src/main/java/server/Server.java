package server;
import handler.*;
import spark.*;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", new handler.RegisterHandler());
        Spark.post("/session", new handler.LoginHandler());
        Spark.delete("/session", new handler.LogoutHandler());
        Spark.get("/game", new handler.ListGameHandler());
        Spark.post("/game", new handler.CreateGameHandler());
        Spark.put("/game", new handler.JoinGameHandler());
        Spark.delete("/db", new handler.ClearApplicationHandler());

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
