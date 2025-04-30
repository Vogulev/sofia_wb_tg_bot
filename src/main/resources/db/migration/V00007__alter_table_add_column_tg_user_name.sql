-- ensure that the column with this name is removed before creating a new one.
ALTER TABLE wb_user DROP COLUMN IF EXISTS tg_user_name;

-- Add tg_user_name column
ALTER TABLE wb_user ADD COLUMN tg_user_name varchar;