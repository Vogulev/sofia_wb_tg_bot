-- ensure that the column with this name is removed before creating a new one.
ALTER TABLE wb_user DROP COLUMN IF EXISTS about;

-- Add about column
ALTER TABLE wb_user ADD COLUMN about TEXT;
