-- Insert users
INSERT INTO users (id, email, for_change, is_enabled, name, password, role, surname) VALUES (1, 'captain1@example.com', false, true, 'Captain', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 1, 'Smith');
INSERT INTO users (id, email, for_change, is_enabled, name, password, role, surname) VALUES (2, 'firefighter1@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Johnson');
INSERT INTO users (id, email, for_change, is_enabled, name, password, role, surname) VALUES (3, 'firefighter2@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Williams');
INSERT INTO users (id, email, for_change, is_enabled, name, password, role, surname) VALUES (4, 'firefighter3@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Brown');
INSERT INTO users (id, email, for_change, is_enabled, name, password, role, surname) VALUES (5, 'captain2@example.com', false, true, 'Captain', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 1, 'Jones');
INSERT INTO users (id, email, for_change, is_enabled, name, password, role, surname) VALUES (6, 'firefighter4@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Garcia');
INSERT INTO users (id, email, for_change, is_enabled, name, password, role, surname) VALUES (7, 'firefighter5@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Martinez');
INSERT INTO users (id, email, for_change, is_enabled, name, password, role, surname) VALUES (8, 'chief1@example.com', false, true, 'Chief', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 2, 'Kelly');

INSERT INTO fire_company (id, captain_id) VALUES (1, 1);
INSERT INTO fire_company (id, captain_id) VALUES (2, 5);

INSERT INTO firecompany_firefighters (firecompany_id, user_id) VALUES(1, 2);
INSERT INTO firecompany_firefighters (firecompany_id, user_id) VALUES(1, 3);
INSERT INTO firecompany_firefighters (firecompany_id, user_id) VALUES(1, 4);
INSERT INTO firecompany_firefighters (firecompany_id, user_id) VALUES(2, 6);
INSERT INTO firecompany_firefighters (firecompany_id, user_id) VALUES(2, 7);

