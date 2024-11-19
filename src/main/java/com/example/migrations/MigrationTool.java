package com.example.migrations;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

public class MigrationTool {

    private static final String MIGRATION_FOLDER_PATH = "src/main/resources/migrations"; // Папка с миграциями

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a command: migrate, rollback, or status.");
            return;
        }

        String command = args[0];

        // Загружаем параметры из конфигурации
        PropertiesUtils propertiesUtils = new PropertiesUtils("src/main/resources/application.properties");
        String dbUrl = propertiesUtils.getDatabaseUrl();
        String dbUser = propertiesUtils.getDatabaseUserName();
        String dbPassword = propertiesUtils.getDatabasePassword();

        try (Connection connection = new ConnectionManager().establishConnection(dbUrl, dbUser, dbPassword)) {

            switch (command) {
                case "migrate":
                    migrate(connection);
                    break;
                case "rollback":
                    rollback(connection);
                    break;
                case "status":
                    showStatus(connection);
                    break;
                default:
                    System.out.println("Invalid command. Available commands: migrate, rollback, status.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void migrate(Connection connection) throws Exception {
        // Читаем миграции из папки
        MigrationFileReader fileReader = new MigrationFileReader();
        List<File> allMigrations = fileReader.getSqlMigrations(new File(MIGRATION_FOLDER_PATH));

        // Получаем список уже примененных миграций
        MigrationManager migrationManager = new MigrationManager();
        List<Migration> appliedMigrations = migrationManager.getAppliedMigrations(connection);

        // Получаем список миграций, которые нужно применить
        List<File> pendingMigrations = migrationManager.getPendingMigrations(allMigrations, appliedMigrations);

        // Применяем миграции
        MigrationExecutor executor = new MigrationExecutor();
        executor.applyMigrations(connection, pendingMigrations);
        System.out.println("Migrations applied successfully.");
    }

    private static void rollback(Connection connection) throws Exception {
        // Реализуем откат (по сути - применяем миграции с наибольшим номером, которое меньше текущего)
        System.out.println("Rollback to previous version not implemented yet.");
    }

    private static void showStatus(Connection connection) throws Exception {
        // Получаем статус базы данных
        MigrationManager migrationManager = new MigrationManager();
        List<Migration> appliedMigrations = migrationManager.getAppliedMigrations(connection);

        if (appliedMigrations.isEmpty()) {
            System.out.println("No migrations applied.");
        } else {
            System.out.println("Current migrations:");
            for (Migration migration : appliedMigrations) {
                System.out.println("Version: " + migration.getVersion() + ", Description: " + migration.getDescription());
            }
        }
    }
}
