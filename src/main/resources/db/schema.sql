DROP TABLE IF EXISTS programmers CASCADE;
DROP TABLE IF EXISTS projects    CASCADE;
DROP TABLE IF EXISTS customers   CASCADE;
DROP TABLE IF EXISTS users       CASCADE;

CREATE TABLE users (
    user_id       UUID         PRIMARY KEY,
    username      VARCHAR(64)  NOT NULL UNIQUE,
    email         VARCHAR(128) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(32)  NOT NULL,
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_users_username ON users (username);
CREATE UNIQUE INDEX idx_users_email    ON users (email);

CREATE TABLE projects (
    project_id        UUID           PRIMARY KEY,
    name              VARCHAR(256)   NOT NULL,
    customer          VARCHAR(256)   NOT NULL,
    programmers_count INTEGER        NOT NULL DEFAULT 0,
    start_date        DATE           NOT NULL,
    end_date          DATE           NOT NULL,
    cost              NUMERIC(19,2)  NOT NULL DEFAULT 0,
    CONSTRAINT chk_projects_dates CHECK (end_date > start_date)
);

CREATE INDEX idx_projects_name     ON projects (name);

CREATE TABLE programmers (
    programmer_id   UUID           PRIMARY KEY,
    project_id      UUID,
    last_name       VARCHAR(64)    NOT NULL,
    first_name      VARCHAR(64)    NOT NULL,
    middle_name     VARCHAR(64),
    position        VARCHAR(32)    NOT NULL,
    work_start_date DATE,
    work_end_date   DATE,
    hourly_rate     NUMERIC(19,2)  NOT NULL,
    employment_type VARCHAR(32)    NOT NULL,
    salary          NUMERIC(19,2)  NOT NULL DEFAULT 0,
    CONSTRAINT chk_programmers_dates CHECK (work_end_date > work_start_date),
    CONSTRAINT chk_hourly_rate CHECK (hourly_rate > 0)
);

CREATE INDEX idx_programmers_project   ON programmers (project_id);
CREATE INDEX idx_programmers_last_name ON programmers (last_name);