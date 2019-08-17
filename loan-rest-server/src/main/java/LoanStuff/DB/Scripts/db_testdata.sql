DELETE FROM users;
DELETE FROM items;


# uid, username,satus
INSERT INTO users VALUES 
(1, 'avivbadian','Gever Gever'),
(2, 'yanivkrim', 'Totah');

#iid, ownerId (fk uid),name,description
INSERT INTO items VALUES 
(1, ,1,'Tent', '6 people tent'),
(2, ,1,'Book', 'Harry Potter 3rd book')
(3, ,2,'Book', 'Advanced java')
(4, ,2,'Chair', 'Keter chair, good for throwing');