CREATE TABLE IF NOT EXISTS role
(
    id   INT PRIMARY KEY,
    name VARCHAR(255)
);

MERGE INTO role (id, name) KEY(id) VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN')
