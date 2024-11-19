package com.example.migrations;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MigrationManager {

    /**
     * Возвращает список уже примененных миграций.
     *
     * @param connection Подключение к базе данных.
     * @return Список примененных миграций.
     * @throws SQLException Если возникает ошибка при работе с базой данных.
     */
    public List<Migration> getAppliedMigrations(Connection connection) throws SQLException {
        String query = "SELECT version, description, applied_at FROM migration_history ORDER BY version ASC";
        List<Migration> appliedMigrations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int version = resultSet.getInt("version");
                String description = resultSet.getString("description");
                Timestamp appliedAt = resultSet.getTimestamp("applied_at");

                appliedMigrations.add(new Migration(version, description, appliedAt));
            }
        }
        return appliedMigrations;
    }

    /**
     * Определяет список миграций, которые еще не применены.
     *
     * @param allMigrations    Список всех доступных файлов миграций.
     * @param appliedMigrations Список уже примененных миграций.
     * @return Список миграций, которые нужно применить.
     */
    public List<File> getPendingMigrations(List<File> allMigrations, List<Migration> appliedMigrations) {
        List<File> pendingMigrations = new ArrayList<>();
        int lastAppliedVersion = appliedMigrations.isEmpty() ? 0 :
                appliedMigrations.get(appliedMigrations.size() - 1).getVersion();

        for (File migrationFile : allMigrations) {
            int version = extractVersionFromFileName(migrationFile.getName());
            if (version > lastAppliedVersion) {
                pendingMigrations.add(migrationFile);
            }
        }

        return pendingMigrations;
    }

    private int extractVersionFromFileName(String fileName) {
        try {
            return Integer.parseInt(fileName.split("_")[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid file name format: " + fileName, e);
        }
    }
}
