--DROP TABLE IF EXISTS borrows;
--DROP TABLE IF EXISTS items;
--DROP TABLE IF EXISTS category;
--DROP TABLE IF EXISTS users;

--CREATE TABLE "category"
--(
-- "id"   numeric NOT NULL,
-- "name" char(50) NOT NULL
--);
--
--CREATE UNIQUE INDEX "PK_category" ON "category"
--(
--  "id"
--);

--CREATE UNIQUE INDEX "PK_users" ON "users"
--(
-- "uid"
--);


--DROP TABLE borrow;
--DROP TABLE request;
--DROP TABLE account CASCADE;
--DROP TABLE item CASCADE;

CREATE TABLE IF NOT EXISTS account
(
  uid char(50) PRIMARY KEY,
  username char(50),
  status  char(50),
  image_path text
);

CREATE TABLE IF NOT EXISTS item
(
  iid SERIAL PRIMARY KEY,
  holder char(50) NOT NULL,
  item_name char(50) NOT NULL,
  description char(50),
  category  char(50) NOT NULL,
  image_path text NOT NULL,
  FOREIGN KEY (holder) REFERENCES account
);

CREATE TABLE IF NOT EXISTS borrow
(
   iid INTEGER,
   borrower char(50) NOT NULL,
   borrow_date date NOT NULL,
   agreed_ret_date date NOT NULL,
   actual_ret_date date,
   PRIMARY KEY(borrower, iid),
   FOREIGN KEY(borrower) REFERENCES account,
   FOREIGN KEY(iid) REFERENCES item
);

CREATE TABLE IF NOT EXISTS request
(
	requester char(50),
	iid INTEGER,
	req_date date,
	req_ret_date date,
	PRIMARY KEY(requester, iid),
	FOREIGN KEY(requester) REFERENCES account,
	FOREIGN KEY(iid) REFERENCES item
)

--CREATE UNIQUE INDEX "PK_items" ON "items"
--(
-- "iid"
--);
--
--CREATE INDEX "fkIdx_11" ON "items"
--(
-- "ownerId"
--);
--
--CREATE INDEX "fkIdx_37" ON "items"
--(
-- "categoryId"
--);
--
--CREATE INDEX "fkIdx_56" ON "borrows"
--(
-- "borrower"
--);
--
--CREATE INDEX "fkIdx_59" ON "borrows"
--(
-- "itemId"
--);


























