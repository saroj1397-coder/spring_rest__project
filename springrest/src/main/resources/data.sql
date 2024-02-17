-- I dont see any uses of this here I will check why I created this as 
--already repository layer is there

CREATE TABLE Course (
    id NUMBER PRIMARY KEY,
    title VARCHAR2(50),
    description VARCHAR2(50)
);

INSERT INTO Course (id, title, description)
VALUES (1, 'Course 1', 'Description 1');

INSERT INTO Course (id, title, description)
VALUES (2, 'Course 2', 'Description 2');
