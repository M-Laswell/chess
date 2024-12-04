package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.management.Notification;
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
        System.out.println("Message: " + message);
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

    @OnWebSocketError
    public void onError(Throwable error){
        System.out.println(error);
        //notify();
    }

    private void connect(AuthData user, Integer gameID, Session session) throws IOException {
        System.out.println("Connected: " + session);
        try {
            var connection = new Connection(user.getUsername(), gameID, session);
            connections.add(connection);
            var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            for(GameData game: gameService.getGames(user.getAuthToken())){
                if(game.getGameID() == gameID){
                    loadGame.setGame(game);
                }
            }
            connection.send(new Gson().toJson(loadGame));;
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            String joinType = null;
            for (GameData game : gameService.getGames(user.getAuthToken())){
                if(game.getGameID() == gameID){
                    if(game.getBlackUsername().equals(user.getUsername())){
                        joinType = "black pieces";
                    } else if (game.getWhiteUsername().equals(user.getUsername())){
                        joinType = "white pieces";
                    } else {
                        joinType = "an observer";
                    }
                }
            }
            notification.setMessage(user.getUsername() + " joined the game as " + joinType);
            connections.broadcast(gameID, user.getUsername(), notification);

        } catch (Exception e){
            System.out.println(e);
        }

    }

    public void makeMove(Integer gameID, ChessMove move) throws ResponseException {
        try {
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            connections.broadcast(gameID, "", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void leave(Integer gameID, AuthData user) throws IOException {
        try {
            connections.removeUser(user.getUsername(), gameID);
            gameService.leaveGame(user.getAuthToken(), gameID);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            notification.setMessage(user.getUsername() + " has left the game");
            connections.broadcast(gameID, user.getUsername(), notification);
        } catch (Exception e) {
            System.out.println(e);
        }
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