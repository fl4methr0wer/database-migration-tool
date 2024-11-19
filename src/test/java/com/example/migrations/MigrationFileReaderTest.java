package com.example.migrations;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MigrationFileReaderTest {

    @Test
    public void testGetSqlMigrations() {
        MigrationFileReader reader = new MigrationFileReader();
        File folder = new File("src/main/resources/migrations");

        List<File> migrations = reader.getSqlMigrations(folder);

        assertEquals(3, migrations.size());
        assertEquals("001_create_users_table.sql", migrations.get(0).getName());
        assertEquals("002_add_email_to_users.sql", migrations.get(1).getName());
        assertEquals("003_create_orders_table.sql", migrations.get(2).getName());
    }

    @Test
    public void testInvalidDirectory() {
        MigrationFileReader reader = new MigrationFileReader();
        File invalidFolder = new File("non_existent_folder");

        assertThrows(IllegalArgumentException.class, () -> {
            reader.getSqlMigrations(invalidFolder);
        });
    }
}
