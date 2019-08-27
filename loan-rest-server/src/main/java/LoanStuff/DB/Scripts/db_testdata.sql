DELETE FROM admins;
DELETE FROM Borrows;
DELETE FROM availabilities;
DELETE FROM items;
DELETE FROM branches;

--- username, password
INSERT INTO admins VALUES
('yanivkrim', '1234'),
('avivbadian', 'KobiZNoti');
	
--- id, title, category, description
INSERT INTO items VALUES
(DEFAULT,'Keter Chair', 'Camping Equipment', 'Original Keter chair'),
(DEFAULT, 'Harry Potter: The Philosophers stone', 'Books', 'Harry Potter book first book'),
(DEFAULT, 'Harry Potter: The Chamber of Secrets', 'Books', 'Harry Potter book second book'),
(DEFAULT, 'Harry Potter: The Prisoner of Azkaban', 'Books', 'Harry Potter book third book');


-- title, address
INSERT INTO branches VALUES
('Haifa-Main', 'Kdoshey Yasi'),
('Haifa:Downtown', 'Downtown'),
('Tel-Aviv:Dizengoff', 'Dizengoff 44'),
('Disney', 'Paris');

-- id, item_id, start_date, end_date
INSERT INTO availabilities VALUES
(DEFAULT,1, '01/10/2019', '08/10/2019'),
(DEFAULT,1, '10/10/2019', '17/10/2019'),
(DEFAULT,1, '01/11/2019', '08/11/2019'),
(DEFAULT,1, '10/11/2019', '17/11/2019'),
(DEFAULT,2, '01/10/2019', '08/10/2019'),
(DEFAULT,2, '10/10/2019', '17/10/2019'),
(DEFAULT,2, '01/11/2019', '08/11/2019'),
(DEFAULT,2, '10/11/2019', '17/11/2019'),
(DEFAULT,3, '01/10/2019', '08/10/2019'),
(DEFAULT,3, '10/10/2019', '17/10/2019'),
(DEFAULT,3, '01/11/2019', '08/11/2019'),
(DEFAULT,3, '10/11/2019', '17/11/2019'),
(DEFAULT,4, '01/10/2019', '08/10/2019'),
(DEFAULT,4, '10/10/2019', '17/10/2019'),
(DEFAULT,4, '01/11/2019', '08/11/2019'),
(DEFAULT,4, '10/11/2019', '17/11/2019');

--- id, availability, phone, email,first name, last name
INSERT INTO Borrows VALUES
(DEFAULT, 1, '054-555666', 'theif@gmail.com', 'Gani', 'Nav'),
(DEFAULT, 2, '053-123456', 'yossi@gmail.com', 'Yossi', 'Luchim'),
(DEFAULT, 5, '052-777888', 'racheli@gmail.com', 'Racheli', 'krakdfsdfki'),
(DEFAULT, 7, '050-409040', 'ronit@gmail.com', 'Ronit', 'Gal-Or'),
(DEFAULT, 9, '04-8470475', 'eitan@gmail.com', 'Eitan', 'Barak'),
(DEFAULT, 10, '058-401474', 'ahmed@gmail.com', 'Ahmed', 'Abbas');