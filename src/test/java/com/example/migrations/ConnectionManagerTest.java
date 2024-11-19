package com.example.migrations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionManagerTest {

    private final ConnectionManager connectionManager = new ConnectionManager();
    PropertiesUtils propertiesUtils =
            new PropertiesUtils("src/main/resources/application.properties");
    String url = propertiesUtils.getDatabaseUrl();
    String username = propertiesUtils.getDatabaseUserName();
    String password = propertiesUtils.getDatabasePassword();

    @AfterEach
    public void tearDown() {
        connectionManager.closeAllConnections();
    }

    @Test
    public void testEstablishAndRetrieveConnection() throws SQLException {
        Connection connection = connectionManager.establishConnection(url, username, password);
        assertNotNull(connection);
        assertFalse(connection.isClosed());

        Connection retrievedConnection = connectionManager.getConnection(url);
        assertSame(connection, retrievedConnection);
    }

    @Test
    public void testDuplicateConnectionThrowsException() throws SQLException {

        connectionManager.establishConnection(url, username, password);
        assertThrows(IllegalStateException.class, () -> {
            connectionManager.establishConnection(url, username, password);
        });
    }

    @Test
    public void testCloseAllConnections() throws SQLException {
        connectionManager.establishConnection(url, username, password);
        connectionManager.closeAllConnections();

        assertNull(connectionManager.getConnection(url));
    }
}
