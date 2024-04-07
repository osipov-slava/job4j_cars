CREATE TABLE car
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR not null,
    engine_id   INT not null unique REFERENCES engine (id)
);

ALTER TABLE auto_post ADD COLUMN car_id int REFERENCES car(id);