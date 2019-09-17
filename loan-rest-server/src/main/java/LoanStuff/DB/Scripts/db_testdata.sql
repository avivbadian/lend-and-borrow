DELETE FROM admins;
DELETE FROM Borrows;
DELETE FROM availabilities;
DELETE FROM items;
DELETE FROM branches;

set datestyle TO "ISO, DMY";

--- username, password
INSERT INTO admins VALUES
  ('yanivkrim', 'Aa123456'),
  ('avivbadian', 'Aa123456');

--- id, title, category, description
INSERT INTO items (title, category, description, path) VALUES
  ('Keter Chair', 'Camping Equipment', 'Original Keter chair', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F12.jpg?alt=media&token=d795442f-f69e-4b89-830d-0c206033e9fc'),
  ('Harry Potter: The Philosophers stone', 'Books', 'Harry Potter book first book', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F12.jpg?alt=media&token=d795442f-f69e-4b89-830d-0c206033e9fc'),
  ('Harry Potter: The Chamber of Secrets', 'Books', 'Harry Potter book second book', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F12.jpg?alt=media&token=d795442f-f69e-4b89-830d-0c206033e9fc'),
  ('Harry Potter: The Prisoner of Azkaban', 'Books', 'Harry Potter book third book', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F12.jpg?alt=media&token=d795442f-f69e-4b89-830d-0c206033e9fc');


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
