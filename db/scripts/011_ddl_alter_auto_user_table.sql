DELETE FROM auto_user WHERE id > 0;
ALTER TABLE auto_user ADD COLUMN email varchar unique not null;
ALTER TABLE auto_user ADD COLUMN timezone varchar;

ALTER TABLE auto_post ADD COLUMN is_active BOOLEAN not null;