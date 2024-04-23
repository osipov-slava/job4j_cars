DELETE FROM auto_user WHERE id > 0;
ALTER TABLE auto_user ADD COLUMN email varchar unique not null;
