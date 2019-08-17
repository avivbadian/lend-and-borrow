DROP TABLE users;
DROP TABLE items;

CREATE TABLE users(
    uid char(30) not null,
	username char(30) not null,
	status char(30) not null,
	PRIMARY KEY (uid)
);

CREATE TABLE items(
	iid numeric(5,0),
	title varchar(50) not null,
	description char(30),
	PRIMARY KEY(iid)
);