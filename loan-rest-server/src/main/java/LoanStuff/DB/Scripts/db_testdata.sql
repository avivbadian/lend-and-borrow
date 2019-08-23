DELETE FROM user_item;
DELETE FROM borrow;
DELETE FROM item;
DELETE FROM account;

INSERT INTO account VALUES
("avivbadian", "Haifa", "Available",""),
("yanivkrim", "Haifa" "Hey im using handy", ""),
("moshep", "Jerusalem" "Hey buddy", ""),
("myoav", "Tel Aviv" "Hey my friend", "");

INSERT INTO items VALUES 
(31, "avivbadian" ,'Tent', '6 people tent', 'travelling' , ""),
(22, "avivbadian" ,'Book', 'Harry Potter 3rd book', 'books', ""),
(13, "yanivkrim",'Book', 'Advanced java', 'books', ""),
(54, "myoav",'Chair', 'Keter chair, good for throwing', 'tools', "");
(65, "myoav",'Chair', 'sitting chair, good for sitting', 'tools', "");
(66, "myoav",'Chair', 'baby chair, good for baby sitting', 'tools', "");

INSERT INTO borrowed_item VALUES
(31, "myoav", '18/2/2019', '20/2/2019', NULL),
(22, "tanivkrim", '12/02/2019', '22/02/2019', '22/02/2019'),
(13, "moshpe", '23/2/2014', '21/09/2019', '22/09/2019');
(54, "avivbadian", '21/12/2018', '22/12/2018', NULL);

INSERT INTO request VALUES
("avivbadian", 65 ,'21/12/2019', '22/12/2019')
("yanivkrim", 65 ,'21/12/2019', '22/12/2019')
("yanivkrim", 66 ,'23/12/2019', '27/12/2019')


