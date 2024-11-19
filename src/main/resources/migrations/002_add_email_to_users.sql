-- Migration: Add email column to users table
ALTER TABLE users
    ADD COLUMN email VARCHAR(150);

-- ROLLBACK
ALTER TABLE users
DROP COLUMN email;
