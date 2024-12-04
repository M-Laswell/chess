package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;


public class ConnectionManager {
    public final ConcurrentHashMap<Integer, List<Connection>> connections = new ConcurrentHashMap<>();

    public void add(Connection connection) {
        if(connections.get(connection.gameID) != null) {
            connections.get(connection.gameID).add(connection);
        } else {
            ArrayList<Connection> conns = new ArrayList<>();
            conns.add(connection);
            connections.put(connection.gameID, conns);
        }
    }

    public void removeGame(Integer gameID) {
        connections.remove(gameID);
    }

    public void removeUser(String userName, Integer gameID) {
        connections.get(gameID).removeIf(connection -> Objects.equals(connection.userName, userName));
    }

    public void broadcast(Integer gameId, String excludeVisitorName, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
            for (var c: connections.get(gameId)) {
                if (c.session.isOpen()) {
                    if (!c.userName.equals(excludeVisitorName)) {
                        c.send(new Gson().toJson(notification));

                    }
                } else {
                    removeList.add(c);
                }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.gameID);
        }
    }
}