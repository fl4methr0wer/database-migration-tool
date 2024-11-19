package com.example.migrations;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MigrationManagerTest {

    @Test
    public void testGetPendingMigrations() {
        MigrationManager manager = new MigrationManager();

        List<File> allMigrations = List.of(
                new File("001_create_users_table.sql"),
                new File("002_add_email_to_users.sql"),
                new File("003_create_orders_table.sql")
        );

        List<Migration> appliedMigrations = List.of(
                new Migration(1, "Create users table", Timestamp.valueOf("2023-01-01 00:00:00")),
                new Migration(2, "Add email to users", Timestamp.valueOf("2023-01-02 00:00:00"))
        );

        List<File> pendingMigrations = manager.getPendingMigrations(allMigrations, appliedMigrations);

        assertEquals(1, pendingMigrations.size());
        assertEquals("003_create_orders_table.sql", pendingMigrations.get(0).getName());
    }
}
