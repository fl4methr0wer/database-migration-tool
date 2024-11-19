package com.example.migrations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionManager {

    private final Map<String, Connection> connections = new HashMap<>();

    public synchronized Connection establishConnection(String url, String username, String password) throws SQLException {
        if (connections.containsKey(url)) {
            throw new IllegalStateException("Connection to the URL already exists: " + url);
        }

        Connection connection = DriverManager.getConnection(url, username, password);
        connections.put(url, connection);
        return connection;
    }

    public synchronized Connection getConnection(String url) {
        return connections.get(url);
    }

    public synchronized void closeAllConnections() {
        for (Map.Entry<String, Connection> entry : connections.entrySet()) {
            try {
                entry.getValue().close();
            } catch (SQLException e) {
                System.err.println("Failed to close connection to: " + entry.getKey());
                e.printStackTrace();
            }
        }
        connections.clear();
    }
}
