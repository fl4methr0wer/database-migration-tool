CREATE TABLE migration_history (
                                   id SERIAL PRIMARY KEY,
                                   version INTEGER NOT NULL,
                                   description VARCHAR(255) NOT NULL,
                                   applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
