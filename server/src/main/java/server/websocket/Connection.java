package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

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
        session.getRemote().sendString(msg);
    }
}
