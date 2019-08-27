﻿DROP TABLE admins CASCADE;
DROP TABLE items CASCADE;
DROP TABLE branches CASCADE;
DROP TABLE availabilities CASCADE;
DROP TABLE Borrows CASCADE;

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
  id SERIAL PRIMARY KEY,
  title char(50),
  address char(50)
);

CREATE TABLE IF NOT EXISTS availabilities
(
  id SERIAL PRIMARY KEY,
  item_id INTEGER REFERENCES items(id) ON DELETE CASCADE,
  branch INTEGER REFERENCES branches(id) ON DELETE CASCADE,
  start_date date,
  end_date date
);

CREATE TABLE IF NOT EXISTS Borrows
(
  id SERIAL PRIMARY KEY,
  availability INTEGER REFERENCES availabilities(id) ON DELETE CASCADE,
  borrow_date date,
  return_date date,
  phone char(50),
  email char(50),
  first_name char(50),
  last_name char(50)
);
