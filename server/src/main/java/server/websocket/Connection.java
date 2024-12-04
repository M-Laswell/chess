package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class Connection {
    public Integer gameID;
    public String userName;
    public Session session;

    public Connection(String userName, Integer gameID, Session session) {
        this.gameID = gameID;
        this.userName = userName;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        } else {
            System.out.println("Session is not open, message cannot be sent.");
        }
    }
}
