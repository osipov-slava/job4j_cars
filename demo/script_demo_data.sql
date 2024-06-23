insert into auto_user (login, password, email, timezone)
values ('John40', 'john', 'john@gmail.com', null),
       ('Kate22', 'kate', 'kate@gmail.com', null);

insert into owner (name, user_id)
values ('John Smith', 4),
       ('Kate Forest', 5);

insert into engine (name)
values ('V6 2.5L 230HP'),
       ('Hybrid'),
       ('Electric 500HP'),
       ('V8 6.2L 420HP'),
       ('3.0 L EcoBoost V6'),
       ('3.0 V6 TFSI');

insert into car (name, engine_id, owner_id, type_id, color_id)
values ('Tesla Model 3', 3, 1, 1, 6),
       ('GMC Yukon', 4, 1, 2, 3),
       ('Toyota Camry', 1, 1, 1, 3),
       ('Ford Explorer', 5, 2, 2, 6),
       ('Kia K5', 2, 2, 1, 3),
       ('Audi A4', 6, 2, 1, 2);

insert into auto_post (description, created, auto_user_id, car_id, is_active)
values ('Brand new car. Leather', '12-06-24 10:00:00', 4, 1, true),
       ('Brand new car.', '14-06-24 10:00:00', 4, 2, true),
       ('2023 after accident', '14-06-24 10:00:00', 4, 3, false),
       ('One owner 2020', '15-06-24 10:00:00', 5, 4, true),
       ('2023 red leather, 18 wheels', '15-06-24 11:00:00', 5, 5, true),
       ('2021 year', '16-06-24 10:00:00', 5, 6, false);

insert into price_history (before, after, created, post_id)
values
    (0, 60000, '12-06-24 10:00:00', 1),
    (0, 59000, '13-06-24 10:00:00', 1),
    (0, 80000, '12-06-24 10:00:00', 2),
    (0, 78000, '15-06-24 10:00:00', 2),
    (0, 12000, '12-06-24 10:00:00', 3),
    (0, 8000, '16-06-24 10:00:00', 3),
    (0, 55000, '12-06-24 10:00:00', 4),
    (0, 39000, '12-06-24 10:00:00', 5),
    (0, 38950, '14-06-24 10:00:00', 5),
    (0, 47000, '12-06-24 10:00:00', 6),
    (0, 46000, '16-06-24 10:00:00', 6);

insert into file (path, name, post_id)
values ('files\tesla.jpg', 'tesla.jpg', 1),
       ('files\tesla2.jpg', 'tesla2.jpg', 1),
       ('files\gmc.jpg', 'gmc.jpg', 2),
       ('files\gmc2.jpg', 'gmc2.jpg', 2),
       ('files\toyota.jpg', 'toyota.jpg', 3),
       ('files\ford.jpg', 'ford.jpg', 4),
       ('files\ford2.jpg', 'ford2.jpg', 4),
       ('files\kia.jpg', 'kia.jpg', 5),
       ('files\kia2.jpg', 'kia2.jpg', 5),
       ('files\audi.jpg', 'audi.jpg', 6),
       ('files\audi2.jpg', 'audi2.jpg', 6);
