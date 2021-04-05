INSERT INTO location (city, state, zipcode) VALUES ('Atlanta', 'GA', '30043');
INSERT INTO location (city, state, zipcode) VALUES ('Dallas', 'tx', '75010');
INSERT INTO location (city, state, zipcode) VALUES ('Madison', 'WI', '53704');

INSERT INTO building(loc_id, address) values (1,'123 Main St.'),
(1,'456 Wall St.'),
(1,'789 First St.'),
(2,'123 Dog St.'),
(2,'456 Cat St.'),
(2,'789 Bird St.'),
(3,'123 Elm St.'),
(3,'456 Maple St.'),
(3,'678 Oak St.');

INSERT INTO room(bld_id, capacity, room_name, type) values
(1, 20, 'Classroom', 'CLASSROOM'),
(1, 20, 'Zoom', 'ONLINE'),
(2, 20, 'Classroom', 'CLASSROOM'),
(2, 20, 'Zoom', 'ONLINE'),
(3, 20, 'Classroom', 'CLASSROOM'),
(3, 20, 'Zoom', 'ONLINE'),
(4, 20, 'Classroom', 'CLASSROOM'),
(4, 20, 'Zoom', 'ONLINE'),
(5, 20, 'Classroom', 'CLASSROOM'),
(5, 20, 'Zoom', 'ONLINE'),
(6, 20, 'Classroom', 'CLASSROOM'),
(6, 20, 'Zoom', 'ONLINE'),
(7, 20, 'Classroom', 'CLASSROOM'),
(7, 20, 'Zoom', 'ONLINE'),
(8, 20, 'Classroom', 'CLASSROOM'),
(8, 20, 'Zoom', 'ONLINE'),
(9, 20, 'Classroom', 'CLASSROOM'),
(9, 20, 'Zoom', 'ONLINE')
