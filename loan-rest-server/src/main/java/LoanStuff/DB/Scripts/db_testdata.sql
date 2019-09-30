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
  ('master', 'AdMataiMarch15');

--- id, title, category, description
INSERT INTO items (title, category, description, path) VALUES
  ('Tent', 'Camping', 'HOSPORT Camping Tent 2-3 Person Tent Instant Pop Up Tent Automatic Backpacking Dome Tents Waterproof Canopy Tent for Camping Outdoor Sports Travel Beach', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2Ftent.jpg?alt=media&token=d8f51276-0174-4e86-8b6e-1e4db0d48874'),
  ('Harry Potter: The Philosophers stone', 'Books', 'Harry Potter book first book', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F9781408855652.jpeg?alt=media&token=d1d3968e-42d0-4ea5-bada-faf55a760cbb'),
  ('Harry Potter: The Chamber of Secrets', 'Books', 'Harry Potter book second book', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F9781408855669.jpeg?alt=media&token=fb947acd-02df-4945-8f47-28a209db4e11'),
  ('Hand weights', 'Sports', '3 lbs Neoprene Dumbbells + Resistance Exercise Bands + Adjustable Jump Rope + Slip-Proof Gloves - Body Workout Equipment Set for Home, Gym & Outdoor Sports – Skytree','https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F51KmnJ2NCLL._AC_SL1000_.jpg?alt=media&token=1a070abe-d95e-4cc5-8189-8132bc989613'),
  ('Pan', 'Coocking', 'Lodge 10.5 Inch Cast Iron Griddle. Pre-seasoned Round Cast Iron Pan Perfect for Pancakes, Pizzas, and Quesadillas.', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F71lb5I5fhyL._AC_SL1500_.jpg?alt=media&token=8a0e7a04-de5c-4b57-a495-373e5a698539'),
  ('Sports watch', 'Sports', 'YANCH Compatible with for Apple Watch Band 38mm 42mm 40mm 44mm, Soft Silicone Sport Band Replacement Wrist Strap Compatible with for iWatch Series 4/3/2/1, Nike+,Sport,Edition', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F61Pbybg6CsL._AC_SL1200_.jpg?alt=media&token=19b82d54-3d0b-4f1f-af76-a7ab84360708'),
  ('Flashlight', 'Camping', 'Anker Rechargeable Bolder LC90 LED Flashlight, Pocket-Sized Torch with Super Bright 900 Lumens CREE LED, IP65 Water-Resistant, Zoomable, 5 Light Modes, 18650 Battery Included', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F7113yxOuyKL._AC_SL1500_.jpg?alt=media&token=94ee13f4-c936-43cd-8f6a-34868f53d099'),
  ('Airbed', 'Sleep', 'Intex Dura-Beam Standard Series Deluxe Single-High Airbed', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F71icCvGLCjL._AC_SL1500_.jpg?alt=media&token=387a92c3-de59-4a75-8113-f8462a6ae4de'),
  ('Sleeping bag', 'Camping', 'Kelty Boys & Girls Big Dipper Sleeping Bag, Childrens Sleeping Bag for Sleepovers, Camping, Backpacking and More', 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F81EuTaT47KL._AC_SL1500_.jpg?alt=media&token=f538f7ea-90fd-4bbe-8a0d-259ff13a0e28'),
  ('Water bottle', 'Sports', 'Hydro Flask Standard Mouth Water Bottle, Flex Cap - Multiple Sizes & Colors' , 'https://firebasestorage.googleapis.com/v0/b/handy-d7cdd.appspot.com/o/Items%2F61Cgdjpo-gL._AC_SL1500_.jpg?alt=media&token=8da2b02d-bc2a-4847-bfb0-228387ca9efe');


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
