DELETE FROM borrow;
DELETE FROM request;
DELETE FROM item;
DELETE FROM account;
DELETE FROM category;

--- username, bio, image_path
INSERT INTO account VALUES
('avivbadian', 'Piano enthusiats', 'aviv.jpeg'),
('yanivkrim', 'Chess and wine fan', 'yaniv.jpeg'),
('moshep', 'From Jerusalem' ,'moshe.jpeg'),
('myoav', 'I love Tel Aviv', 'yoav.jpeg');

--- id, name
INSERT INTO category VALUES
(1, 'Camping Equipment'),
(2, 'Musical instruments'),
(3, 'Books'),
(4, 'Board Games');

--- itemId, owner_username, item_name, descripition, category, image_path
INSERT INTO item VALUES 
(1, 'avivbadian', 'Harry Potter 3rd Edition', 'HP book in a good condition in hebrew', 3, '1.jpeg'),
(2, 'avivbadian', 'Piano', 'Electrical piano, slightly damaged', 2, '2.jpeg'),
(3, 'yanivkrim', 'Settlers Of Catan', 'A board game for people ages 3-99 in mint condition', 4, '3.jpeg'),
(4, 'moshep', 'Chess set', '32 Pieces chess set with rubber board', 4, '4.jpeg'),
(5, 'myoav', 'Keter Chair', 'Good for throwing', 1, '5.jpeg');

--- itemId, borrower, borrow_date, agreed_ret_date, actual_ret_date
INSERT INTO borrow VALUES
(1, 'myoav', '18/2/2019', '20/2/2019', NULL),
(2, 'yanivkrim', '12/02/2019', '22/02/2019', '22/02/2019'),
(3, 'moshep', '23/2/2014', '21/09/2019', NULL),
(4, 'avivbadian', '21/12/2018', '22/12/2018', NULL);

-- requester, itemId, req_date, req_ret_date
INSERT INTO request VALUES
('avivbadian', 5 ,'21/12/2019', '22/12/2019'),
('yanivkrim', 5 ,'21/12/2019', '22/12/2019'),
('moshep', 5 ,'23/12/2019', '27/12/2019');


