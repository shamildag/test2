-- INSERT INTO app_role (id, role_name, description) VALUES (1, 'STANDARD_USER', 'Standard User - Has permission to download ');
-- INSERT INTO app_role (id, role_name, description) VALUES (2, 'DEVELOPER', 'Developer - Has permission to perform upload task');
-- USER
-- non-encrypted password: jwtpass
INSERT INTO user (id, email, pass, name, dev) VALUES (1, 'Shama@mail.ru', '', 'Shama', 'DEVELOPER');
INSERT INTO user (id, email, pass, name, dev) VALUES (2, 'Stas@mail.ru', '', 'Stas', 'DEVELOPER');
INSERT INTO user (id, email, pass, name, dev) VALUES (3, 'Maga@mail.ru', '', 'Maga', 'USER');

