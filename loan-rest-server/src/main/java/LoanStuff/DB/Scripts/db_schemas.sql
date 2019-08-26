DROP TABLE admins CASCADE;
DROP TABLE items CASCADE;
DROP TABLE branches CASCADE;
DROP TABLE availabilities CASCADE;
DROP TABLE loans CASCADE;

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
  item_id INTEGER REFERENCES items(id),
  branch INTEGER REFERENCES branches(id),
  start_date date,
  end_date date
);

CREATE TABLE IF NOT EXISTS loans
(
  id SERIAL PRIMARY KEY,
  availability INTEGER REFERENCES availabilities(id),
  borrow_date date,
  return_date date,
  loaner_phone char(50),
  loaner_email char(50),
  loaner_name char(50)
);
