package com.example.migrations;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

    private final Properties properties = new Properties();

    public PropertiesUtils(String propertiesFilePath) {
        try (FileInputStream fis = new FileInputStream(propertiesFilePath)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + propertiesFilePath, e);
        }
    }

    public String getDatabaseUrl() {
        return properties.getProperty("db.url");
    }

    public String getDatabaseUserName() {
        return properties.getProperty("db.username");
    }

    public String getDatabasePassword() {
        return properties.getProperty("db.password");
    }

    public String getMigrationFolder() {
        return properties.getProperty("migration.folder");
    }
}
