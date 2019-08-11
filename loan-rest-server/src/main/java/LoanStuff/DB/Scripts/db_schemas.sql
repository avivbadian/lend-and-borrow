CREATE TABLE users(
	id char(30) not null,
	pass char(30) not null,
	PRIMARY KEY (id)
);

CREATE TABLE items(
	iid numeric(5,0),
	title varchar(50) not null,
	description char(30),
	PRIMARY KEY(iid)
);