DROP TABLE IF EXISTS admins CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS branches CASCADE;
DROP TABLE IF EXISTS availabilities CASCADE;
DROP TABLE IF EXISTS Borrows CASCADE;
DROP TYPE IF EXISTS status CASCADE;

CREATE TABLE IF NOT EXISTS admins
(
  username char(50) PRIMARY KEY,
  password char(50)
);

CREATE TABLE IF NOT EXISTS items
(
  id SERIAL PRIMARY KEY,
  title char(50),
  category char(50),
  description char(300)
);

CREATE TABLE IF NOT EXISTS branches
(
  title char(50) PRIMARY KEY,
  address char(50)
);

CREATE TABLE IF NOT EXISTS availabilities
(
  id SERIAL PRIMARY KEY,
  item_id INTEGER REFERENCES items(id) ON DELETE CASCADE,
  start_date date,
  end_date date
);

CREATE TYPE status AS ENUM ('pending', 'approved', 'declined');

CREATE TABLE IF NOT EXISTS borrows
(
  id SERIAL PRIMARY KEY,
  availability INTEGER REFERENCES availabilities(id) ON DELETE CASCADE,
  branch char(50) REFERENCES branches(title) ON DELETE CASCADE,
  phone char(50),
  email char(50),
  first_name char(50),
  last_name char(50),
  status status
);
