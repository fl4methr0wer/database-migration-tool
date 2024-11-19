package com.example.migrations;

import java.sql.Timestamp;

public class Migration {
    private final int version;
    private final String description;
    private final Timestamp appliedAt;

    public Migration(int version, String description, Timestamp appliedAt) {
        this.version = version;
        this.description = description;
        this.appliedAt = appliedAt;
    }

    public int getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getAppliedAt() {
        return appliedAt;
    }
}
