DROP TABLE IF EXISTS borrows;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;

CREATE TABLE "category"
(
 "id"   numeric NOT NULL,
 "name" char(50) NOT NULL
);

CREATE UNIQUE INDEX "PK_category" ON "category"
(
  "id"
);

CREATE TABLE "users"
(
 "uid"      numeric NOT NULL,
 "username" char(50) NOT NULL,
 "status"   char(50) NOT NULL

);

CREATE UNIQUE INDEX "PK_users" ON "users"
(
 "uid"
);


CREATE TABLE "items"
(
 "iid"         numeric(18,0) NOT NULL,
 "ownerId"     numeric NOT NULL,
 "name"        char(50) NOT NULL,
 "description" char(50) NOT NULL,
 "categoryId"  numeric NOT NULL,
 CONSTRAINT "FK_11" FOREIGN KEY ( "ownerId" ) REFERENCES "users" ( "uid" ),
 CONSTRAINT "FK_37" FOREIGN KEY ( "categoryId" ) REFERENCES "category" ( "id" )
);

CREATE UNIQUE INDEX "PK_items" ON "items"
(
 "iid"
);

CREATE INDEX "fkIdx_11" ON "items"
(
 "ownerId"
);

CREATE INDEX "fkIdx_37" ON "items"
(
 "categoryId"
);

CREATE TABLE "borrows"
(
 "borrower"   numeric NOT NULL,
 "itemId"     numeric(18,0) NOT NULL,
 "borrowDate" date NOT NULL,
 "returnDate" date NOT NULL,
 "status"     numeric NOT NULL,
 CONSTRAINT "FK_56" FOREIGN KEY ( "borrower" ) REFERENCES "users" ( "uid" ),
 CONSTRAINT "FK_59" FOREIGN KEY ( "itemId" ) REFERENCES "items" ( "iid" )
);


CREATE INDEX "fkIdx_56" ON "borrows"
(
 "borrower"
);

CREATE INDEX "fkIdx_59" ON "borrows"
(
 "itemId"
);


























