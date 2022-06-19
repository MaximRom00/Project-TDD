DROP TABLE IF EXISTS AUTHOR;

CREATE TABLE AUTHOR (
                        id INT AUTO_INCREMENT  PRIMARY KEY,
                        firstName VARCHAR(250) NOT NULL,
                        lastName VARCHAR(250) NOT NULL
);

INSERT INTO AUTHOR (firstName, lastName) VALUES ('Fyodor', 'Dostoevsky');
INSERT INTO AUTHOR (firstName, lastName) VALUES ('George', 'Orwell');
INSERT INTO AUTHOR (firstName, lastName) VALUES ('Antoine', 'Saint-Exupery');
INSERT INTO AUTHOR (firstName, lastName) VALUES ('John', 'Steinbeck');
INSERT INTO AUTHOR (firstName, lastName) VALUES ('Marcel', 'Proust');

DROP TABLE IF EXISTS BOOK;

CREATE TABLE BOOK (
                      id INT AUTO_INCREMENT  PRIMARY KEY,
                      name VARCHAR(250) NOT NULL,
                      AUTHOR_ID int
);

INSERT INTO BOOK (name, AUTHOR_ID) VALUES ('Idiot', 1);
INSERT INTO BOOK (name, AUTHOR_ID) VALUES ('1984', 2);
INSERT INTO BOOK (name, AUTHOR_ID) VALUES ('The Little Prince', 3);
INSERT INTO BOOK (name, AUTHOR_ID) VALUES ('The Grapes of Wrath', 4);
INSERT INTO BOOK (name, AUTHOR_ID) VALUES ('In Search of Lost Time', 5);


