CREATE TABLE file
(
    id      SERIAL PRIMARY KEY,
    path    VARCHAR not null,
    name    VARCHAR not null,
    post_id int     REFERENCES auto_post (id)
);
