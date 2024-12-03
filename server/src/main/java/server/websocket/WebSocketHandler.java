package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private UserService userService;
    private AuthService authService;
    private GameService gameService;

    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(UserService userService, AuthService authService, GameService gameService){
        this.userService = userService;
        this.authService = authService;
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, ResponseException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        try{
            AuthData userData = authService.authenticate(command.getAuthToken());

            switch (command.getCommandType()) {
                case CONNECT -> connect(userData, command.getGameID(), session);
                case MAKE_MOVE -> makeMove(command.getGameID(), command.getChessMove());
                case LEAVE -> leave(command.getGameID(), userData);
                case RESIGN -> resign(command.getGameID(), userData);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);//Return Message User does not exist you trickster you
        }
    }

    private void connect(AuthData user, Integer gameID, Session session) throws IOException {
        connections.add(user.getUsername(), gameID, session);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(gameID, user.getUsername(), notification);
    }

public void makeMove(Integer gameID, ChessMove move) throws ResponseException {                                                                                                                                 nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn
        try {
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            connections.broadcast(gameID, "", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void leave(Integer gameID, AuthData user) throws IOException {
        connections.removeUser(user.getUsername(), gameID);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(gameID, user.getUsername(), notification);
    }

    public void resign(Integer gameID, AuthData user) throws ResponseException {
        try {
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            connections.broadcast(gameID,"", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}