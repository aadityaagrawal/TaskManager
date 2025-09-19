CREATE TABLE task (
    id BIGINT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(255),
    priority VARCHAR(255),
    due_date date,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (title, status)
);