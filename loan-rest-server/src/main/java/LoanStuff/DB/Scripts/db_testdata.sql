DELETE FROM admins;
DELETE FROM availabilities;
DELETE FROM items;
DELETE FROM branches;
DELETE FROM loans;


--- username, password
INSERT INTO admins VALUES
('yanivkrim', '1234'),
('avivbadian', 'KobiZNoti');
	
--- id, title, category, description
INSERT INTO items VALUES
(1,'Keter Chair', 'Camping Equipment', 'Original Keter chair'),
(2, 'Harry Potter: The Philosophers stone', 'Books', 'Harry Potter book first book'),
(3, 'Harry Potter: The Chamber of Secrets', 'Books', 'Harry Potter book second book'),
(4, 'Harry Potter: The Prisoner of Azkaban', 'Books', 'Harry Potter book third book');


-- id, title, address
INSERT INTO branches VALUES
(1, 'Haifa Main Branch', 'Kdoshey Yasi'),
(2, 'Haifa: Downtown', 'Downtown'),
(3, 'Tel Aviv: Dizengoff', 'Dizengoff 44'),
(4, 'Disney', 'Paris');

-- id, item_id, branch, start_date, end_date
INSERT INTO availabilities VALUES
(1,1, 1, '01/01/2019', '31/12/2019'),
(2,2, 1, '01/01/2019', '31/12/2019'),
(3,3, 1, '01/01/2019', '31/12/2019'),
(4,4, 1, '01/01/2019', '31/12/2019'),
(5,2, 2, '01/01/2019', '31/12/2019'),
(6,2, 3, '01/01/2019', '31/12/2019'),
(7,3, 3, '01/01/2019', '31/12/2019'),
(8,3, 4, '01/01/2019', '31/12/2019'),
(9,4, 2, '01/01/2019', '31/12/2019'),
(10,4, 4, '01/01/2019', '31/12/2019');

--- id, availability, borrow_date, return_date, loaner_phone, loaner_email,lonaer_name
INSERT INTO loans VALUES
(1, 1, '02/01/2019', '04/01/2019', '054-555666', 'theif@gmail.com', 'Gani Nav'),
(2, 1, '02/02/2019', '04/12/2019', '053-123456', 'yossi@gmail.com', 'Yossi Luchim'),
(3, 5, '15/03/2019', '16/03/2019', '052-777888', 'racheli@gmail.com', 'Racheli krakdfsdfki'),
(4, 7, '09/07/2019', '09/08/2019', '050-409040', 'ronit@gmail.com', 'Ronit Gal-Or'),
(5, 9, '12/12/2019', '31/12/2019', '04-8470475', 'eitan@gmail.com', 'Eitan Barak'),
(6, 10, '05/05/2019', '06/06/2019', '058-401474', 'ahmed@gmail.com', 'Ahmed Abbas');