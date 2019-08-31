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
INSERT INTO items (title, category, description) VALUES
  ('Keter Chair', 'Camping Equipment', 'Original Keter chair'),
  ('Harry Potter: The Philosophers stone', 'Books', 'Harry Potter book first book'),
  ('Harry Potter: The Chamber of Secrets', 'Books', 'Harry Potter book second book'),
  ('Harry Potter: The Prisoner of Azkaban', 'Books', 'Harry Potter book third book');


-- title, address
INSERT INTO branches VALUES
  ('Haifa-Main', 'Kdoshey Yasi'),
  ('Haifa:Downtown', 'Downtown'),
  ('Tel-Aviv:Dizengoff', 'Dizengoff 44'),
  ('Disney', 'Paris');

-- id, item_id, start_date, end_date
INSERT INTO availabilities (item_id, start_date, end_date) VALUES
  ((SELECT id from items WHERE title = 'Keter Chair') , '01/10/2019', '08/10/2019'),
  ((SELECT id from items WHERE title = 'Keter Chair'), '10/10/2019', '12/10/2019'),
  ((SELECT id from items WHERE title = 'Keter Chair'), '01/11/2019', '08/11/2019'),
  ((SELECT id from items WHERE title = 'Keter Chair'), '10/11/2019', '12/11/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Philosophers stone'), '01/10/2019', '08/10/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Philosophers stone'), '10/10/2019', '12/10/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Philosophers stone'), '01/11/2019', '08/11/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Philosophers stone'), '10/11/2019', '12/11/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Chamber of Secrets'), '01/10/2019', '08/10/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Chamber of Secrets'), '10/10/2019', '12/10/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Chamber of Secrets'), '01/11/2019', '08/11/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Chamber of Secrets'), '10/11/2019', '12/11/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Prisoner of Azkaban'), '01/10/2019', '08/10/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Prisoner of Azkaban'), '10/10/2019', '12/10/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Prisoner of Azkaban'), '01/11/2019', '08/11/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Prisoner of Azkaban'), '10/11/2019', '12/11/2019');

--- id, availability, phone, email,first name, last name
INSERT INTO borrows (availability, branch, phone, email, first_name, last_name, status) VALUES
  ((SELECT max(id) from availabilities), 'Haifa-Main', '054-555666', 'theif@gmail.com', 'Gani', 'Nav', 'pending'),
  ((SELECT min(id) from availabilities), 'Haifa-Main', '053-123456', 'yossi@gmail.com', 'Yossi', 'Luchim', 'pending'),
  ((SELECT min(id) from availabilities), 'Haifa-Main', '00', 'ff', 'a', 'b', 'pending'),
  ((SELECT min(id) from availabilities), 'Haifa-Main', '11', 'ee', 'c', 'd', 'pending');
