CREATE TABLE car
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR not null,
    engine_id   INT not null unique REFERENCES engine(id),
    owner_id    INT not null REFERENCES owner(id)
);

ALTER TABLE auto_post ADD COLUMN car_id int not null REFERENCES car(id);