package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
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
import java.net.Authenticator;
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
                case MAKE_MOVE -> makeMove(command.getGameID(), command.getChessMove(), userData, session);
                case LEAVE -> leave(command.getGameID(), userData);
                case RESIGN -> resign(command.getGameID(), userData, session);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);//Return Message User does not exist you trickster you
        }
    }

    @OnWebSocketError
    public void onError(Throwable error){
        System.out.println("We reached a Websocket Thrown Error");
        System.out.println(error);
    }

    private void connect(AuthData user, Integer gameID, Session session) throws IOException, DataAccessException {
        System.out.println("Connected: " + session);
        user = checkForUser(user,session);
        if(user != null) {
            var connection = new Connection(user.getUsername(), gameID, session);
            GameData game = checkForGame(gameID, session, user);
            if (game != null) {
                connections.add(connection);
                var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                loadGame.setGame(game);
                connection.send(new Gson().toJson(loadGame));
                String joinType = null;
                    if (game.getGameID() == gameID) {
                        if (game.getBlackUsername() != null && game.getBlackUsername().equals(user.getUsername())) {
                            joinType = "black pieces";
                        } else if (game.getWhiteUsername() != null && game.getWhiteUsername().equals(user.getUsername())) {
                            joinType = "white pieces";
                        } else {
                            joinType = "an observer";
                        }
                    }
                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                notification.setMessage(user.getUsername() + " joined the game as " + joinType);
                connections.broadcast(gameID, user.getUsername(), notification);
            }
        }


    }

    private AuthData checkForUser(AuthData user, Session session) throws IOException {
        try {
            AuthData userToCheck = authService.authenticate(user.getAuthToken());
            return userToCheck;
        } catch (Exception e){
            var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            notification.setErrorMessage("You're Not Authorized For This Action");
            session.getRemote().sendString(new Gson().toJson(notification));

            return null;
        }
    }

    private GameData checkForGame(Integer gameID, Session session, AuthData user) throws DataAccessException, IOException {

        for(GameData game: gameService.getGames(user.getAuthToken())){
            if(game.getGameID() == gameID){

                return game;
            }
        }

        var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        notification.setErrorMessage("Invalid Game ID");
        session.getRemote().sendString(new Gson().toJson(notification));
        return null;
    }

    private String convertColToLetter(Integer letter){
        return switch(letter) {
            case 1 -> "A";
            case 2 -> "B";
            case 3 -> "C";
            case 4 -> "D";
            case 5 -> "E";
            case 6 -> "F";
            case 7 -> "G";
            case 8 -> "H";
            default -> throw new RuntimeException(" ");
        };
    }

    public void makeMove(Integer gameID, ChessMove move, AuthData user, Session session) throws IOException, DataAccessException {
        Integer startRow = move.getStartPosition().getRow();
        Integer startCol = move.getStartPosition().getColumn();
        Integer endRow = move.getEndPosition().getRow();
        Integer endCol = move.getEndPosition().getColumn();
        String startPos = convertColToLetter(startCol) + startRow;
        String endPos = convertColToLetter(endCol) + endRow;
        user = checkForUser(user,session);
        boolean valid = false;
        try {
            if (user == null) {
                return;
            }
            var connection = new Connection(user.getUsername(), gameID, session);
            GameData game = checkForGame(gameID, session, user);
            if (game == null) {
                return;
            }
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
            if (user.getUsername().equals(game.getWhiteUsername()) && game.getGame().getTeamTurn().equals(ChessGame.TeamColor.WHITE)) {
                if (checkValidMove(game, ChessGame.TeamColor.WHITE, move)) {
                    if (!game.getGame().isGameWon()) {
                        game.getGame().makeMove(move);
                        gameService.updateGame(gameID, game);
                        notification.setMessage(user.getUsername() + " moved from " + startPos + " to " + endPos);
                        //session.getRemote().sendString(new Gson().toJson(notification));
                        valid = true;
                    } else {
                        var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                        error.setErrorMessage("Game is already over");
                        session.getRemote().sendString(new Gson().toJson(error));
                        return;
                    }
                }

            } else if (user.getUsername().equals(game.getBlackUsername()) && game.getGame().getTeamTurn().equals(ChessGame.TeamColor.BLACK)) {
                if (checkValidMove(game, ChessGame.TeamColor.BLACK, move)) {
                    if (!game.getGame().isGameWon()) {
                        game.getGame().makeMove(move);
                        gameService.updateGame(gameID, game);
                        notification.setMessage(user.getUsername() + " moved from " + startPos + " to " + endPos);
                        //session.getRemote().sendString(new Gson().toJson(notification));
                        valid = true;
                    } else {
                        var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                        error.setErrorMessage("Game is already over");
                        session.getRemote().sendString(new Gson().toJson(error));
                        return;
                    }
                }
            }
            if (!valid) {
                var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                error.setErrorMessage("Invalid Game Move");
                session.getRemote().sendString(new Gson().toJson(error));
            } else {
                connections.broadcast(gameID, user.getUsername(), notification);
                if (game.getGame().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                    game.getGame().setGameWon(true);
                    game.getGame().setWinner(ChessGame.TeamColor.WHITE);
                    gameService.updateGame(gameID, game);
                    notification.setMessage(game.getWhiteUsername() + " has won the game through checkmate");
                    connections.broadcast(gameID, "", notification);
                } else if (game.getGame().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                    game.getGame().setGameWon(true);
                    game.getGame().setWinner(ChessGame.TeamColor.BLACK);
                    gameService.updateGame(gameID, game);
                    notification.setMessage( game.getBlackUsername() + " has won the game through checkmate");
                    connections.broadcast(gameID, "", notification);
                } else if (game.getGame().isInCheck(ChessGame.TeamColor.BLACK)){
                    notification.setMessage(game.getBlackUsername() + " is in check");
                    connections.broadcast(gameID, "", notification);
                } else if (game.getGame().isInCheck(ChessGame.TeamColor.WHITE)) {
                    notification.setMessage(game.getWhiteUsername() + " is in check");
                    connections.broadcast(gameID, "", notification);
                }
                var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                loadGame.setGame(game);
                connections.broadcast(gameID, "", loadGame);
            }
        } catch (Exception e) {
            var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            notification.setErrorMessage("Invalid move attempted");
            session.getRemote().sendString(new Gson().toJson(notification));
        }
    }

    private Boolean checkValidMove(GameData game, ChessGame.TeamColor color, ChessMove move){
        return (game.getGame().validMoves(move.getStartPosition()).contains(move));

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

    //trust,autonomy,purpose,psychological safety key principles for building efficient productive organizations

    public void resign(Integer gameID, AuthData user, Session session) throws IOException, DataAccessException {
        user = checkForUser(user, session);
        boolean valid = false;
        if (user != null) {
            var connection = new Connection(user.getUsername(), gameID, session);
            GameData game = checkForGame(gameID, session, user);
            if (game != null) {
                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                if (user.getUsername().equals(game.getWhiteUsername())) {
                    if (!game.getGame().isGameWon()) {
                        game.getGame().setWinner(ChessGame.TeamColor.BLACK);
                        valid = true;
                    } else {
                        var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                        error.setErrorMessage("Game is already over");
                        session.getRemote().sendString(new Gson().toJson(error));
                        return;
                    }

                } else if (user.getUsername().equals(game.getBlackUsername())) {
                    if (!game.getGame().isGameWon()) {
                        game.getGame().setWinner(ChessGame.TeamColor.WHITE);
                        valid = true;
                    } else {
                        var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                        error.setErrorMessage("Game is already over");
                        session.getRemote().sendString(new Gson().toJson(error));
                        return;
                    }
                }
                if (valid) {
                    game.getGame().setGameWon(true);
                    gameService.updateGame(gameID, game);
                    notification.setMessage(user.getUsername() + " has resigned");
                    connections.broadcast(gameID, "", notification);
                } else {
                    var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
                    error.setErrorMessage("You Can't Resign Foolish Mortal");
                    session.getRemote().sendString(new Gson().toJson(error));
                }
            }
        }
    }
}