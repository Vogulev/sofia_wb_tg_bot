-- ensure that the column with this name is removed before creating a new one.
ALTER TABLE wb_user DROP COLUMN IF EXISTS state;
ALTER TABLE wb_user DROP COLUMN IF EXISTS state_update;

-- Add state column
ALTER TABLE wb_user ADD COLUMN state varchar;

-- Add state timestamp column
ALTER TABLE wb_user ADD COLUMN state_update timestamp;