DELETE FROM admins;
DELETE FROM Borrows;
DELETE FROM availabilities;
DELETE FROM items;
DELETE FROM branches;

set datestyle TO "ISO, DMY";

--- username, password
INSERT INTO admins VALUES
  ('yanivkrim', 'Aa123456'),
  ('avivbadian', 'Aa123456'),
  ('master', 'AdMataiMarch14');

--- id, title, category, description
INSERT INTO items (title, category, description, path) VALUES
  ('Tent', 'Camping Equipment', '4 people tent, green, okay condition.', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F12.jpg?alt=media&token=d795442f-f69e-4b89-830d-0c206033e9fc'),
  ('Harry Potter: The Philosophers stone', 'Books', 'Harry Potter book first book', 'https://media.bloomsbury.com/rep/bj/9781408855652.jpeg'),
  ('Harry Potter: The Chamber of Secrets', 'Books', 'Harry Potter book second book', 'https://media.bloomsbury.com/rep/bj/9781408855669.jpeg'),
  ('Hammer', 'Tools', 'A hammer with wooden handle in good condition, 5K','https://i.imgur.com/ORGInJP.png'),
  ('Screwdriver', 'Tools', 'Swiss screwdriver, small size', 'https://i.imgur.com/VnjgYTq.png'),
  ('Crutches', 'Mobility aids', 'Metal crutches, for adults', 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/Axillary_%28underarm%29_crutches.JPG/150px-Axillary_%28underarm%29_crutches.JPG'),
  ('Scooter', 'Mobility aids', 'Small scooter for people with mobility problems', 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Walking_Aid_Scooter_and_mobility_aid.jpg/330px-Walking_Aid_Scooter_and_mobility_aid.jpg'),
  ('Cat', 'Pets', 'A cat, so you wont get lonley, his name is Jeff', 'https://i.imgur.com/QCoM9Qu.png'),
  ('Dog', 'Pets', 'A dog, so you wont get lonley, his name is Moon', 'https://i.imgur.com/l1NdaKS.png'),
  ('Calculator', 'tools', 'A Brand new casio calculator, sceintific' , 'https://i.imgur.com/W37GCYM.png');


-- title, address
INSERT INTO branches VALUES
  ('Haifa', 'Kdoshey Yasi 32'),
  ('Haifa: Downtown', 'Hertzel 11'),
  ('Tel-Aviv', 'Dizengoff 44'),
  ('Disney', 'Paris');

-- id, item_id, start_date, end_date
INSERT INTO availabilities (item_id, start_date, end_date) VALUES
  ((SELECT id from items WHERE title = 'Tent') , '10/10/2019', '17/10/2019'),
  ((SELECT id from items WHERE title = 'Tent') , '18/10/2019', '25/10/2019'),
  ((SELECT id from items WHERE title = 'Tent') , '26/10/2019', '1/11/2019'),
  ((SELECT id from items WHERE title = 'Tent') , '2/11/2019', '9/11/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Philosophers stone') , '10/10/2019', '17/10/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Philosophers stone') , '18/10/2019', '25/10/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Philosophers stone') , '26/10/2019', '1/11/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Philosophers stone') , '2/11/2019', '9/11/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Chamber of Secrets') , '10/10/2019', '17/10/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Chamber of Secrets') , '18/10/2019', '25/10/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Chamber of Secrets') , '26/10/2019', '1/11/2019'),
  ((SELECT id from items WHERE title = 'Harry Potter: The Chamber of Secrets') , '2/11/2019', '9/11/2019'),
  ((SELECT id from items WHERE title = 'Hammer') , '10/10/2019', '17/10/2019'),
  ((SELECT id from items WHERE title = 'Hammer') , '18/10/2019', '25/10/2019'),
  ((SELECT id from items WHERE title = 'Hammer') , '26/10/2019', '1/11/2019'),
  ((SELECT id from items WHERE title = 'Hammer') , '2/11/2019', '9/11/2019'),
  ((SELECT id from items WHERE title = 'Screwdriver') , '10/10/2019', '17/10/2019'),
  ((SELECT id from items WHERE title = 'Screwdriver') , '18/10/2019', '25/10/2019'),
  ((SELECT id from items WHERE title = 'Screwdriver') , '26/10/2019', '1/11/2019'),
  ((SELECT id from items WHERE title = 'Screwdriver') , '2/11/2019', '9/11/2019'),
  ((SELECT id from items WHERE title = 'Crutches') , '10/10/2019', '17/10/2019'),
  ((SELECT id from items WHERE title = 'Crutches') , '18/10/2019', '25/10/2019'),
  ((SELECT id from items WHERE title = 'Crutches') , '26/10/2019', '1/11/2019'),
  ((SELECT id from items WHERE title = 'Crutches') , '2/11/2019', '9/11/2019'),
  ((SELECT id from items WHERE title = 'Scooter') , '10/10/2019', '17/10/2019'),
  ((SELECT id from items WHERE title = 'Scooter') , '18/10/2019', '25/10/2019'),
  ((SELECT id from items WHERE title = 'Scooter') , '26/10/2019', '1/11/2019'),
  ((SELECT id from items WHERE title = 'Scooter') , '2/11/2019', '9/11/2019'),
  ((SELECT id from items WHERE title = 'Cat') , '10/10/2019', '17/10/2019'),
  ((SELECT id from items WHERE title = 'Cat') , '18/10/2019', '25/10/2019'),
  ((SELECT id from items WHERE title = 'Cat') , '26/10/2019', '1/11/2019'),
  ((SELECT id from items WHERE title = 'Cat') , '2/11/2019', '9/11/2019'),
  ((SELECT id from items WHERE title = 'Dog') , '10/10/2019', '17/10/2019'),
  ((SELECT id from items WHERE title = 'Dog') , '18/10/2019', '25/10/2019'),
  ((SELECT id from items WHERE title = 'Dog') , '26/10/2019', '1/11/2019'),
  ((SELECT id from items WHERE title = 'Dog') , '2/11/2019', '9/11/2019'),
  ((SELECT id from items WHERE title = 'Calculator') , '10/10/2019', '17/10/2019'),
  ((SELECT id from items WHERE title = 'Calculator') , '18/10/2019', '25/10/2019'),
  ((SELECT id from items WHERE title = 'Calculator') , '26/10/2019', '1/11/2019'),
  ((SELECT id from items WHERE title = 'Calculator') , '2/11/2019', '9/11/2019');
  
--- id, availability, phone, email,first name, last name
INSERT INTO borrows (availability, branch, phone, email, first_name, last_name, status) VALUES
  (1, 'Haifa', '0545556668', 'theif@gmail.com', 'Gani', 'Nav', 'pending'),
  (1, 'Haifa', '0531234567', 'yossi@gmail.com', 'Yossi', 'Luchim', 'pending'),
  (1, 'Tel-Aviv', '0546259696', 'yanivkrim@gmail.com', 'Yaniv', 'Krim', 'pending'),
  (7, 'Tel-Aviv', '0527585867', 'Benjamin@gmail.com', 'Bejnamin', 'Franlkin', 'pending'),
  (7, 'Tel-Aviv', '0509491233', 'Juila@gmail.com', 'Julia', 'Adams', 'pending'),
  (8, 'Tel-Aviv', '0509491233', 'Juila@gmail.com', 'Julia', 'Adams', 'declined');
