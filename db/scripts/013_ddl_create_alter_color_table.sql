CREATE TABLE color
(
    id          serial      primary key,
    name        varchar     not null
);

INSERT INTO color (name) VALUES ('Other');
INSERT INTO color (name) VALUES ('White');
INSERT INTO color (name) VALUES ('Black');
INSERT INTO color (name) VALUES ('Red');
INSERT INTO color (name) VALUES ('Grey');
INSERT INTO color (name) VALUES ('Blue');
INSERT INTO color (name) VALUES ('Brown');
INSERT INTO color (name) VALUES ('Green');
INSERT INTO color (name) VALUES ('Yellow');

ALTER TABLE car ADD COLUMN color_id int not null REFERENCES color(id)