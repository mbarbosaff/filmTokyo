INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_USER');

INSERT INTO user (username, password, email, name, surname, active, creation_date)
VALUES ('tokioschool', '$2a$10$mUptUKJ.2F40BU.PeS/gnOPstwBOudA71zzOK6/mVPxLzrfgy/qty', 'admin@tokioschool.com', 'Tokio', 'School', 1, CURDATE());

INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id FROM user u, role r WHERE u.username = 'tokioschool' AND r.name = 'ROLE_ADMIN';