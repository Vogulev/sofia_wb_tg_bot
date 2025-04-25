-- ensure that the table with this name is removed before creating a new one.
DROP TABLE IF EXISTS wb_user;

-- Create user table
CREATE TABLE wb_user
(
    id bigserial primary key,
    phone varchar unique not null,
    name varchar unique not null
);