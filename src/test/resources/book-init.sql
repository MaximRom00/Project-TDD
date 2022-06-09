DROP TABLE IF EXISTS BOOK;

CREATE TABLE BOOK (
                      id INT AUTO_INCREMENT  PRIMARY KEY,
                      name VARCHAR(250) NOT NULL
);

INSERT INTO BOOK (name) VALUES ('Idiot');
INSERT INTO BOOK (name) VALUES ('1984');
INSERT INTO BOOK (name) VALUES ('The Little Prince');
INSERT INTO BOOK (name) VALUES ('The Grapes of Wrath');
INSERT INTO BOOK (name) VALUES ('In Search of Lost Time');