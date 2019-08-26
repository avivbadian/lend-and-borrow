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


-- id, title, address
INSERT INTO branches VALUES
(DEFAULT, 'Haifa Main Branch', 'Kdoshey Yasi'),
(DEFAULT, 'Haifa: Downtown', 'Downtown'),
(DEFAULT, 'Tel Aviv: Dizengoff', 'Dizengoff 44'),
(DEFAULT, 'Disney', 'Paris');

-- id, item_id, branch, start_date, end_date
INSERT INTO availabilities VALUES
(DEFAULT,1, 1, '01/01/2019', '31/12/2019'),
(DEFAULT,2, 1, '01/01/2019', '31/12/2019'),
(DEFAULT,3, 1, '01/01/2019', '31/12/2019'),
(DEFAULT,4, 1, '01/01/2019', '31/12/2019'),
(DEFAULT,2, 2, '01/01/2019', '31/12/2019'),
(DEFAULT,2, 3, '01/01/2019', '31/12/2019'),
(DEFAULT,3, 3, '01/01/2019', '31/12/2019'),
(DEFAULT,3, 4, '01/01/2019', '31/12/2019'),
(DEFAULT,4, 2, '01/01/2019', '31/12/2019'),
(DEFAULT,4, 4, '01/01/2019', '31/12/2019');

--- id, availability, borrow_date, return_date, phone, email,first name, last name
INSERT INTO Borrows VALUES
(DEFAULT, 1, '02/01/2019', '04/01/2019', '054-555666', 'theif@gmail.com', 'Gani', 'Nav'),
(DEFAULT, 1, '02/02/2019', '04/12/2019', '053-123456', 'yossi@gmail.com', 'Yossi', 'Luchim'),
(DEFAULT, 5, '15/03/2019', '16/03/2019', '052-777888', 'racheli@gmail.com', 'Racheli', 'krakdfsdfki'),
(DEFAULT, 7, '09/07/2019', '09/08/2019', '050-409040', 'ronit@gmail.com', 'Ronit', 'Gal-Or'),
(DEFAULT, 9, '12/12/2019', '31/12/2019', '04-8470475', 'eitan@gmail.com', 'Eitan', 'Barak'),
(DEFAULT, 10, '05/05/2019', '06/06/2019', '058-401474', 'ahmed@gmail.com', 'Ahmed', 'Abbas');