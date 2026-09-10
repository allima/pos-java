CREATE TABLE IF NOT EXISTS app_users (
    id UUID PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_app_users_email ON app_users(email);
CREATE INDEX IF NOT EXISTS idx_app_users_role ON app_users(role);

MERGE INTO app_users (id, full_name, email, password_hash, role, enabled)
KEY (email)
VALUES (
    RANDOM_UUID(),
    'alexandre de Lima',
    'allima1991@gmail.com',
    '$2a$10$9rACHabJILg8t7zwxlv6YO290NAlirklciEK7yVtJ39fgU/VaqP9u',
    'ROLE_ADMIN',
    TRUE
);
