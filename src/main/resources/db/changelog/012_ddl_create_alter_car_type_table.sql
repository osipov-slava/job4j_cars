CREATE TABLE car_type
(
    id          serial      primary key,
    name        varchar     not null
);

INSERT INTO car_type (name) VALUES ('Sedan');
INSERT INTO car_type (name) VALUES ('SUV');
INSERT INTO car_type (name) VALUES ('Truck');
INSERT INTO car_type (name) VALUES ('Coupe');
INSERT INTO car_type (name) VALUES ('Hatchback');
INSERT INTO car_type (name) VALUES ('Van');
INSERT INTO car_type (name) VALUES ('Sport');
INSERT INTO car_type (name) VALUES ('Luxury');
INSERT INTO car_type (name) VALUES ('Electric');

ALTER TABLE car ADD COLUMN type_id int not null REFERENCES car_type(id)