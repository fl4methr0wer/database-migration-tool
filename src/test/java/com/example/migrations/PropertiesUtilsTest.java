package com.example.migrations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertiesUtilsTest {

    @Test
    public void testLoadProperties() {
        PropertiesUtils propertiesUtils = new PropertiesUtils("src/main/resources/application.properties");

        assertEquals("jdbc:postgresql://localhost:5432/your_database", propertiesUtils.getDatabaseUrl());
        assertEquals("your_username", propertiesUtils.getDatabaseUserName());
        assertEquals("your_password", propertiesUtils.getDatabasePassword());
        assertEquals("src/main/resources/migrations", propertiesUtils.getMigrationFolder());
    }
}
