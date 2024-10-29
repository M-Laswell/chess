import chess.*;
import dataaccess.*;
import server.Server;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        try {
            var port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }


            var server = new Server().run(port);
            port = server;
            System.out.printf("Server started on port %d", port);
            return;
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
        System.out.println("""
                Chess Server
                java ServerMain <port> [sql]
                """);
    }
    }