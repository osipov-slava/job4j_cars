create table auto_user
(
    id          serial  primary key,
    login       varchar not null unique,
    password    varchar not null
);