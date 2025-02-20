-- CREACION DE LAS TABLAS

-- CATEGORY

CREATE TABLE categories (
                            id_category SERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            description VARCHAR(255) NOT NULL
);

-- TOPIC

CREATE TABLE topics (
                        id_topic SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE,
                        description VARCHAR(255) NOT NULL,
                        slug VARCHAR(255) NOT NULL UNIQUE,
                        creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        id_category INT NOT NULL,
                        FOREIGN KEY (id_category) REFERENCES categories(id_category)
);

-- ROLE

CREATE TABLE roles (
                       id_role SERIAL PRIMARY KEY,
                       role_name VARCHAR(50) NOT NULL UNIQUE
);

-- USER

CREATE TABLE users (
                       id_user SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255),
                       creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       enable BOOLEAN NOT NULL,
                       banned BOOLEAN NOT NULL,
                       ban_date TIMESTAMP,
                       points INT NOT NULL,
                       profile_photo VARCHAR(255),
                       id_role INT NOT NULL,
                       FOREIGN KEY (id_role) REFERENCES roles(id_role)
);

-- CONFIRMATION TOKEN

CREATE TABLE confirmation_tokens (
                                     id_token SERIAL PRIMARY KEY,
                                     token VARCHAR(255) NOT NULL UNIQUE,
                                     expiration_date TIMESTAMP NOT NULL,
                                     activation_date TIMESTAMP,
                                     id_user INT NOT NULL,
                                     FOREIGN KEY (id_user) REFERENCES users(id_user)
);

-- THREAD

CREATE TABLE threads (
                         id_thread SERIAL PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         message TEXT NOT NULL,
                         creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         id_topic INT NOT NULL,
                         id_user INT NOT NULL,
                         FOREIGN KEY (id_topic) REFERENCES topics(id_topic),
                         FOREIGN KEY (id_user) REFERENCES users(id_user)
);

-- COMMENT

CREATE TABLE comments (
                          id_comment SERIAL PRIMARY KEY,
                          message TEXT NOT NULL,
                          creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          id_thread INT NOT NULL,
                          id_user INT NOT NULL,
                          FOREIGN KEY (id_thread) REFERENCES threads(id_thread),
                          FOREIGN KEY (id_user) REFERENCES users(id_user)
);

-- INSERTANDO VALORES PARA LAS TABLAS

-- CATEGORY

INSERT INTO categories (name, description) VALUES
                                               ('Technology', 'All about technology'),
                                               ('Science', 'Scientific discoveries and research'),
                                               ('Art', 'Artistic expressions and creativity'),
                                               ('Literature', 'Books and literary works'),
                                               ('Music', 'Musical genres and artists'),
                                               ('Health', 'Well-being and medical topics'),
                                               ('Travel', 'Travel destinations and tips'),
                                               ('Education', 'Learning and educational resources'),
                                               ('Sports', 'All about sports and activities'),
                                               ('Food', 'Culinary delights and recipes');

-- TOPIC

INSERT INTO topics (name, description, slug, id_category) VALUES
                                                              ('Introduction to Java', 'A beginner''s guide to Java programming', 'introduction-to-java', 1),
                                                              ('Advanced Java', 'Deep dive into Java programming', 'advanced-java', 1),
                                                              ('Spring Framework', 'Introduction to Spring Framework', 'spring-framework', 1),
                                                              ('Machine Learning Basics', 'An introduction to machine learning concepts', 'machine-learning-basics', 2),
                                                              ('Deep Learning', 'Understanding deep learning algorithms', 'deep-learning', 2),
                                                              ('Physics Fundamentals', 'Basic concepts in physics', 'physics-fundamentals', 2),
                                                              ('Impressionist Art', 'Exploring the world of Impressionism', 'impressionist-art', 3),
                                                              ('Modern Art', 'A look into modern art movements', 'modern-art', 3),
                                                              ('Classical Literature', 'Great works of classical literature', 'classical-literature', 4),
                                                              ('Contemporary Novels', 'Analysis of contemporary novels', 'contemporary-novels', 4),
                                                              ('Rock Music History', 'The history of rock music', 'rock-music-history', 5),
                                                              ('Classical Music', 'Exploring classical music compositions', 'classical-music', 5),
                                                              ('Healthy Eating', 'Guide to healthy eating habits', 'healthy-eating', 6),
                                                              ('Travel Tips', 'Tips for international travel', 'travel-tips', 7),
                                                              ('Online Learning Resources', 'Best resources for online learning', 'online-learning-resources', 8);

-- ROLE

INSERT INTO roles (role_name) VALUES
                                  ('ADMIN'),
                                  ('STUDENT');

-- USER

INSERT INTO users (username, email, password, creation_date, enable, banned, ban_date, points, id_role) VALUES
                                                                                                            ('jluyo', 'jluyoc1@upao.edu.pe', '$2a$10$459Kv.wQEQVP8YOETbkS7.KEm16iNW8k.v.2pI/XD1qol3dbd7ml6', '2023-01-01T00:00:00', true, false, NULL, 100, NULL, 1),
                                                                                                            ('activate_user', 'user2@example.com', 'activate', NOW(), false, false, NULL, 200, NULL, 2),
                                                                                                            ('user3', 'user3@example.com', 'password3', '2023-01-03T00:00:00', false, false, NULL, 150, NULL, 1),
                                                                                                            ('user4', 'user4@example.com', 'password4', '2023-01-04T00:00:00', true, false, NULL, 300, NULL, 2),
                                                                                                            ('user5', 'user5@example.com', 'password5', '2023-01-05T00:00:00', true, true, '2024-01-05T00:00:00', 250, NULL, 1),
                                                                                                            ('user6', 'user6@example.com', 'password6', '2023-01-06T00:00:00', true, false, NULL, 120, NULL, 2),
                                                                                                            ('user7', 'user7@example.com', 'password7', '2023-01-07T00:00:00', true, false, NULL, 180, NULL, 1),
                                                                                                            ('user8', 'user8@example.com', 'password8', '2023-01-08T00:00:00', true, true, '2024-01-08T00:00:00', 220, NULL, 2),
                                                                                                            ('user9', 'user9@example.com', 'password9', '2023-01-09T00:00:00', true, false, NULL, 170, NULL, 1),
                                                                                                            ('user10', 'user10@example.com', 'password10', '2023-01-10T00:00:00', true, false, NULL, 190, NULL, 2),
                                                                                                            ('user11', 'user11@example.com', 'password11', '2023-01-11T00:00:00', true, true, '2024-01-11T00:00:00', 140, NULL, 1),
                                                                                                            ('user12', 'user12@example.com', 'password12', '2023-01-12T00:00:00', true, false, NULL, 160, NULL, 2),
                                                                                                            ('user13', 'user13@example.com', 'password13', '2023-01-13T00:00:00', true, false, NULL, 210, NULL, 1),
                                                                                                            ('user14', 'user14@example.com', 'password14', '2023-01-14T00:00:00', true, false, NULL, 230, NULL, 2),
                                                                                                            ('user15', 'user15@example.com', 'password15', '2023-01-15T00:00:00', true, false, NULL, 280, NULL, 1);

-- CONFIRMATION TOKEN

INSERT INTO confirmation_tokens (token, expiration_date, activation_date, id_user) VALUES
                                                                                       ('550e8400-e29b-41d4-a716-446655440000', '2023-01-01T00:10:00', '2023-01-01T00:05:00', 1),
                                                                                       ('550e8400-e29b-41d4-a716-446655440001', TIMESTAMPADD(MINUTE, 10, NOW()), null, 2),
                                                                                       ('550e8400-e29b-41d4-a716-446655440002', '2023-01-03T00:10:00', NULL, 3),
                                                                                       ('550e8400-e29b-41d4-a716-446655440003', '2023-01-04T00:10:00', '2023-01-04T00:05:00', 4),
                                                                                       ('550e8400-e29b-41d4-a716-446655440004', '2023-01-05T00:10:00', '2023-01-05T00:05:00', 5),
                                                                                       ('550e8400-e29b-41d4-a716-446655440005', '2023-01-06T00:10:00', '2023-01-06T00:05:00', 6),
                                                                                       ('550e8400-e29b-41d4-a716-446655440006', '2023-01-07T00:10:00', '2023-01-07T00:05:00', 7),
                                                                                       ('550e8400-e29b-41d4-a716-446655440007', '2023-01-08T00:10:00', '2023-01-08T00:05:00', 8),
                                                                                       ('550e8400-e29b-41d4-a716-446655440008', '2023-01-09T00:10:00', '2023-01-09T00:05:00', 9),
                                                                                       ('550e8400-e29b-41d4-a716-446655440009', '2023-01-10T00:10:00', '2023-01-10T00:05:00', 10),
                                                                                       ('550e8400-e29b-41d4-a716-446655440010', '2023-01-11T00:10:00', '2023-01-11T00:05:00', 11),
                                                                                       ('550e8400-e29b-41d4-a716-446655440011', '2023-01-12T00:10:00', '2023-01-12T00:05:00', 12),
                                                                                       ('550e8400-e29b-41d4-a716-446655440012', '2023-01-13T00:10:00', '2023-01-13T00:05:00', 13),
                                                                                       ('550e8400-e29b-41d4-a716-446655440013', '2023-01-14T00:10:00', '2023-01-14T00:05:00', 14),
                                                                                       ('550e8400-e29b-41d4-a716-446655440014', '2023-01-15T00:10:00', '2023-01-15T00:05:00', 15);

-- THREAD

INSERT INTO threads (title, message, id_topic, id_user) VALUES
                                                            ('Introduction to Java: Getting Started', 'This thread is for beginners starting with Java.', 1, 1),
                                                            ('Java Collections Framework', 'Discussion on the different collections available in Java.', 1, 2),
                                                            ('Concurrency in Java', 'Let''s talk about concurrency and multithreading in Java.', 1, 3),
                                                            ('Advanced Java Topics', 'Deep dive into advanced Java topics.', 2, 1),
                                                            ('Java Performance Tuning', 'Tips and tricks for optimizing Java code.', 2, 4),
                                                            ('Spring Boot Basics', 'An introduction to Spring Boot framework.', 3, 1),
                                                            ('Spring Boot with Docker', 'How to containerize Spring Boot applications using Docker.', 3, 2),
                                                            ('Machine Learning 101', 'Beginner''s guide to machine learning.', 4, 5),
                                                            ('Supervised vs Unsupervised Learning', 'Discussion on different types of machine learning.', 4, 6),
                                                            ('Neural Networks', 'Basics of neural networks and deep learning.', 5, 5),
                                                            ('Deep Learning Libraries', 'Overview of popular deep learning libraries.', 5, 7),
                                                            ('Quantum Mechanics Basics', 'Introduction to quantum mechanics.', 6, 8),
                                                            ('Classical Mechanics vs Quantum Mechanics', 'Discussion on the differences between classical and quantum mechanics.', 6, 9),
                                                            ('Impressionism: An Overview', 'Exploring the world of Impressionist art.', 7, 10),
                                                            ('Modern Art Movements', 'A look into various modern art movements.', 8, 10),
                                                            ('Literary Classics', 'Discussing great works of classical literature.', 9, 11),
                                                            ('Contemporary Literature', 'Analysis of contemporary novels and works.', 10, 11),
                                                            ('History of Rock Music', 'The evolution of rock music.', 11, 12),
                                                            ('Famous Composers in Classical Music', 'Discussion on famous composers and their works.', 12, 12),
                                                            ('Healthy Diets', 'Guide to maintaining a healthy diet.', 13, 13),
                                                            ('Traveling on a Budget', 'Tips for traveling without breaking the bank.', 14, 14),
                                                            ('Best Online Learning Platforms', 'Review of the best platforms for online learning.', 15, 15),
                                                            ('Java Debugging Techniques', 'Effective debugging techniques in Java.', 1, 4),
                                                            ('Microservices with Spring Boot', 'Building microservices using Spring Boot.', 3, 1),
                                                            ('AI Ethics', 'Discussion on the ethical considerations in AI.', 4, 5),
                                                            ('Quantum Computing', 'Basics of quantum computing and its applications.', 6, 8),
                                                            ('Cubism: Art Movement', 'Exploring Cubism and its impact on art.', 8, 10),
                                                            ('Postmodern Literature', 'Analysis of postmodern literary works.', 10, 11),
                                                            ('Jazz Music History', 'The history and evolution of jazz music.', 11, 12),
                                                            ('Home Workouts', 'Effective workout routines you can do at home.', 13, 13),
                                                            ('Solo Travel Tips', 'Tips and advice for solo travelers.', 14, 14);


-- COMMENT

INSERT INTO comments (message, creation_date, id_thread, id_user) VALUES
                                                                      ('This is comment 1', '2023-01-01T01:00:00', 1, 1),
                                                                      ('This is comment 2', '2023-01-01T02:00:00', 2, 2),
                                                                      ('This is comment 3', '2023-01-01T03:00:00', 3, 3),
                                                                      ('This is comment 4', '2023-01-01T04:00:00', 4, 4),
                                                                      ('This is comment 5', '2023-01-01T05:00:00', 5, 5),
                                                                      ('This is comment 6', '2023-01-01T06:00:00', 6, 6),
                                                                      ('This is comment 7', '2023-01-01T07:00:00', 7, 7),
                                                                      ('This is comment 8', '2023-01-01T08:00:00', 8, 8),
                                                                      ('This is comment 9', '2023-01-01T09:00:00', 9, 9),
                                                                      ('This is comment 10', '2023-01-01T10:00:00', 10, 10),
                                                                      ('This is comment 11', '2023-01-01T11:00:00', 11, 11),
                                                                      ('This is comment 12', '2023-01-01T12:00:00', 12, 12),
                                                                      ('This is comment 13', '2023-01-01T13:00:00', 13, 13),
                                                                      ('This is comment 14', '2023-01-01T14:00:00', 14, 14),
                                                                      ('This is comment 15', '2023-01-01T15:00:00', 15, 15),
                                                                      ('This is comment 16', '2023-01-02T01:00:00', 1, 1),
                                                                      ('This is comment 17', '2023-01-02T02:00:00', 2, 2),
                                                                      ('This is comment 18', '2023-01-02T03:00:00', 3, 3),
                                                                      ('This is comment 19', '2023-01-02T04:00:00', 4, 4),
                                                                      ('This is comment 20', '2023-01-02T05:00:00', 5, 5),
                                                                      ('This is comment 21', '2023-01-02T06:00:00', 6, 6),
                                                                      ('This is comment 22', '2023-01-02T07:00:00', 7, 7),
                                                                      ('This is comment 23', '2023-01-02T08:00:00', 8, 8),
                                                                      ('This is comment 24', '2023-01-02T09:00:00', 9, 9),
                                                                      ('This is comment 25', '2023-01-02T10:00:00', 10, 10),
                                                                      ('This is comment 26', '2023-01-02T11:00:00', 11, 11),
                                                                      ('This is comment 27', '2023-01-02T12:00:00', 12, 12),
                                                                      ('This is comment 28', '2023-01-02T13:00:00', 13, 13),
                                                                      ('This is comment 29', '2023-01-02T14:00:00', 14, 14),
                                                                      ('This is comment 30', '2023-01-02T15:00:00', 15, 15),
                                                                      ('This is comment 31', '2023-01-03T01:00:00', 1, 1),
                                                                      ('This is comment 32', '2023-01-03T02:00:00', 2, 2),
                                                                      ('This is comment 33', '2023-01-03T03:00:00', 3, 3),
                                                                      ('This is comment 34', '2023-01-03T04:00:00', 4, 4),
                                                                      ('This is comment 35', '2023-01-03T05:00:00', 5, 5),
                                                                      ('This is comment 36', '2023-01-03T06:00:00', 6, 6),
                                                                      ('This is comment 37', '2023-01-03T07:00:00', 7, 7),
                                                                      ('This is comment 38', '2023-01-03T08:00:00', 8, 8),
                                                                      ('This is comment 39', '2023-01-03T09:00:00', 9, 9),
                                                                      ('This is comment 40', '2023-01-03T10:00:00', 10, 10),
                                                                      ('This is comment 41', '2023-01-03T11:00:00', 11, 11),
                                                                      ('This is comment 42', '2023-01-03T12:00:00', 12, 12),
                                                                      ('This is comment 43', '2023-01-03T13:00:00', 13, 13),
                                                                      ('This is comment 44', '2023-01-03T14:00:00', 14, 14),
                                                                      ('This is comment 45', '2023-01-03T15:00:00', 15, 15),
                                                                      ('This is comment 46', '2023-01-04T01:00:00', 1, 1),
                                                                      ('This is comment 47', '2023-01-04T02:00:00', 2, 2),
                                                                      ('This is comment 48', '2023-01-04T03:00:00', 3, 3),
                                                                      ('This is comment 49', '2023-01-04T04:00:00', 4, 4),
                                                                      ('This is comment 50', '2023-01-04T05:00:00', 5, 5),
                                                                      ('This is comment 51', '2023-01-04T06:00:00', 6, 6),
                                                                      ('This is comment 52', '2023-01-04T07:00:00', 7, 7),
                                                                      ('This is comment 53', '2023-01-04T08:00:00', 8, 8),
                                                                      ('This is comment 54', '2023-01-04T09:00:00', 9, 9),
                                                                      ('This is comment 55', '2023-01-04T10:00:00', 10, 10),
                                                                      ('This is comment 56', '2023-01-04T11:00:00', 11, 11),
                                                                      ('This is comment 57', '2023-01-04T12:00:00', 12, 12),
                                                                      ('This is comment 58', '2023-01-04T13:00:00', 13, 13),
                                                                      ('This is comment 59', '2023-01-04T14:00:00', 14, 14),
                                                                      ('This is comment 60', '2023-01-04T15:00:00', 15, 15),
                                                                      ('This is comment 61', '2023-01-05T01:00:00', 1, 1),
                                                                      ('This is comment 62', '2023-01-05T02:00:00', 2, 2),
                                                                      ('This is comment 63', '2023-01-05T03:00:00', 3, 3),
                                                                      ('This is comment 64', '2023-01-05T04:00:00', 4, 4),
                                                                      ('This is comment 65', '2023-01-05T05:00:00', 5, 5),
                                                                      ('This is comment 66', '2023-01-05T06:00:00', 6, 6),
                                                                      ('This is comment 67', '2023-01-05T07:00:00', 7, 7),
                                                                      ('This is comment 68', '2023-01-05T08:00:00', 8, 8),
                                                                      ('This is comment 69', '2023-01-05T09:00:00', 9, 9),
                                                                      ('This is comment 70', '2023-01-05T10:00:00', 10, 10)