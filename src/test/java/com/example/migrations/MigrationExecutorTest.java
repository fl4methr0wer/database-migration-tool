package com.example.migrations;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.*;

public class MigrationExecutorTest {

    @Test
    public void testApplyMigrations() throws Exception {
        // Создаем мок подключений и миграций
        //Connection mockConnection = mock(Connection.class);
        PropertiesUtils propertiesUtils = new PropertiesUtils("src/main/resources/application.properties");
        Connection connection = new ConnectionManager().getConnection(
                propertiesUtils.getDatabaseUrl()
        );
        MigrationExecutor executor = new MigrationExecutor();
        List<File> migrations = List.of(new File("src/main/resources/migrations/001_create_users_table.sql"));

        executor.applyMigrations(connection, migrations);

        verify(connection, times(1)).prepareStatement(anyString());
    }

    @Test
    public void testHandleError() throws Exception {
        Connection mockConnection = mock(Connection.class);
        MigrationExecutor executor = new MigrationExecutor();
        List<File> migrations = List.of(new File("src/main/resources/migrations/invalid_migration.sql"));

        try {
            executor.applyMigrations(mockConnection, migrations);
        } catch (Exception e) {
            verify(mockConnection, times(1)).rollback();
        }
    }
}
