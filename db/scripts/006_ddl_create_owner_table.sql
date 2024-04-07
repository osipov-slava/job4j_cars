CREATE TABLE owner
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR not null,
    user_id INT not null REFERENCES auto_user (id)
);