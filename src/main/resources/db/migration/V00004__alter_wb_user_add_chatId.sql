-- ensure that the column with this name is removed before creating a new one.
ALTER TABLE wb_user DROP COLUMN IF EXISTS chat_id;

-- Add state column
ALTER TABLE wb_user ADD COLUMN chat_id bigint;