CREATE TABLE history_owner (
    id          serial PRIMARY KEY,
    owner_id    int not null REFERENCES owner(id),
    car_id      int not null REFERENCES car(id),
    start_at    timestamp not null,
    end_at      timestamp not null,
    UNIQUE (owner_id, car_id)
);