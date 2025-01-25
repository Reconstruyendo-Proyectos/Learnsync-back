-- CREACION DE LAS TABLAS

CREATE TABLE Category (
                          id_category SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          description VARCHAR(255) NOT NULL
);

-- INSERTANDO VALORES PARA LAS TABLAS

INSERT INTO Category (name, description) VALUES
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
