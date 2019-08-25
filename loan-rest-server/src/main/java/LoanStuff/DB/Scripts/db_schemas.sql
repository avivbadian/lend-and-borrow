DROP TABLE borrow;
DROP TABLE request;
DROP TABLE account CASCADE;
DROP TABLE item CASCADE;
DROP TABLE category CASCADE;

CREATE TABLE IF NOT EXISTS account
(
  username char(50) PRIMARY KEY,
  bio char(500),
  image_path text
);

CREATE TABLE IF NOT EXISTS category
(
 id numeric NOT NULL,
 name char(50) NOT NULL,
 PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS item
(
  item_id SERIAL PRIMARY KEY,
  owner_username char(50) NOT NULL,
  item_name char(50) NOT NULL,
  description char(500),
  category  numeric NOT NULL,
  image_path text NOT NULL,
  FOREIGN KEY (owner_username) REFERENCES account,
  FOREIGN KEY (category) REFERENCES category
);

CREATE TABLE IF NOT EXISTS borrow
(
   item_id INTEGER,
   borrower char(50) NOT NULL,
   borrow_date date NOT NULL,
   agreed_ret_date date NOT NULL,
   actual_ret_date date,
   PRIMARY KEY(borrower, itemId),
   FOREIGN KEY(borrower) REFERENCES account,
   FOREIGN KEY(itemId) REFERENCES item
);

CREATE TABLE IF NOT EXISTS request
(
	requester char(50),
	itemId INTEGER,
	req_date date,
	req_ret_date date,
	PRIMARY KEY(requester, itemId),
	FOREIGN KEY(requester) REFERENCES account,
	FOREIGN KEY(itemId) REFERENCES item
);
