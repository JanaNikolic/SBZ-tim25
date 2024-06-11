-- Insert users
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('captain1@example.com', false, true, 'Captain', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 1, 'Smith');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('firefighter1@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Johnson');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('firefighter2@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Williams');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('firefighter3@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Brown');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('captain2@example.com', false, true, 'Captain', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 1, 'Jones');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('firefighter4@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Garcia');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('firefighter5@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Martinez');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('chief1@example.com', false, true, 'Chief', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 2, 'Kelly');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('captain3@example.com', false, true, 'Captain', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 1, 'Jones');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('captain4@example.com', false, true, 'Captain', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 1, 'Jones');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('firefighter6@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Martinez');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('firefighter7@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Martinez');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('firefighter8@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Martinez');
INSERT INTO users (email, for_change, is_enabled, name, password, role, surname) VALUES ('firefighter9@example.com', false, true, 'Firefighter', '$2a$10$4gcH3Vpgee624QLZrY99E.9uzTCUmc3nsTsbfinyBm808oIEGBZWS', 0, 'Martinez');

INSERT INTO fire_company (captain_id) VALUES ((SELECT id FROM users WHERE email = 'captain1@example.com'));
INSERT INTO fire_company (captain_id) VALUES ((SELECT id FROM users WHERE email = 'captain2@example.com'));

INSERT INTO firecompany_firefighters (firecompany_id, user_id) VALUES(1, (SELECT id FROM users WHERE email = 'firefighter1@example.com'));
INSERT INTO firecompany_firefighters (firecompany_id, user_id) VALUES(1, (SELECT id FROM users WHERE email = 'firefighter2@example.com'));
INSERT INTO firecompany_firefighters (firecompany_id, user_id) VALUES(1, (SELECT id FROM users WHERE email = 'firefighter3@example.com'));
INSERT INTO firecompany_firefighters (firecompany_id, user_id) VALUES(2, (SELECT id FROM users WHERE email = 'firefighter4@example.com'));
INSERT INTO firecompany_firefighters (firecompany_id, user_id) VALUES(2, (SELECT id FROM users WHERE email = 'firefighter5@example.com'));
