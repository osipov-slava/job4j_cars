CREATE TABLE participates
(
    id      serial PRIMARY KEY,
    post_id int not null REFERENCES auto_post (id),
    user_id int not null REFERENCES auto_user (id),
    UNIQUE (post_id, user_id)
);