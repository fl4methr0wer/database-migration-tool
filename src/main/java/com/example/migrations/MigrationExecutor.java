package com.example.migrations;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MigrationExecutor {

    /**
     * Выполняет все миграции, которые необходимо применить.
     * Это синхронизированный метод для предотвращения одновременного выполнения миграций.
     *
     * @param connection Подключение к базе данных.
     * @param migrations Список миграций для применения.
     * @throws Exception Если возникла ошибка при применении миграций.
     */
    public synchronized void applyMigrations(Connection connection, List<File> migrations) throws Exception {
        for (File migrationFile : migrations) {
            try {
                applyMigration(connection, migrationFile);
                logMigrationHistory(connection, migrationFile);
            } catch (Exception e) {
                handleError(connection, e);
                throw e;
            }
        }
    }

    /**
     * Применяет одну миграцию.
     *
     * @param connection Подключение к базе данных.
     * @param migration  Миграционный файл.
     * @throws Exception Если возникла ошибка при применении миграции.
     */
    private void applyMigration(Connection connection, File migration) throws Exception {
        String sql = new String(Files.readAllBytes(Paths.get(migration.getAbsolutePath())));

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            System.out.println("Migration applied: " + migration.getName());
        } catch (SQLException e) {
            throw new Exception("Error applying migration: " + migration.getName(), e);
        }
    }

    /**
     * Логирует успешное применение миграции в таблице migration_history.
     *
     * @param connection Подключение к базе данных.
     * @param migration  Миграционный файл.
     * @throws SQLException Если не удалось выполнить запрос.
     */
    private void logMigrationHistory(Connection connection, File migration) throws SQLException {
        String version = migration.getName().split("_")[0];
        String description = migration.getName().substring(version.length() + 1, migration.getName().lastIndexOf("."));

        String insertQuery = "INSERT INTO migration_history (version, description) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setInt(1, Integer.parseInt(version));
            statement.setString(2, description);
            statement.executeUpdate();
        }
    }

    /**
     * Обрабатывает ошибку миграции и откатывает изменения.
     *
     * @param connection Подключение к базе данных.
     * @param e          Исключение.
     */
    private void handleError(Connection connection, Exception e) {
        System.err.println("Migration failed: " + e.getMessage());
        // Можно реализовать откат миграции, если необходимо
        try {
            connection.rollback();
        } catch (SQLException rollbackException) {
            System.err.println("Failed to rollback: " + rollbackException.getMessage());
        }
    }
}
