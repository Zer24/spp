CREATE TABLE users (
    user_id       UUID PRIMARY KEY,
    username      VARCHAR(64)  NOT NULL,
    email         VARCHAR(128) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(32)  NOT NULL,
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_users_username_active ON users (username) WHERE is_deleted = false;
CREATE UNIQUE INDEX idx_users_email_active    ON users (email)    WHERE is_deleted = false;