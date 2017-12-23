DROP TABLE IF EXISTS student;
CREATE TABLE student (
  id           SERIAL PRIMARY KEY,
  name         TEXT NOT NULL DEFAULT '',
  city         TEXT          DEFAULT '',
  password     TEXT NOT NULL DEFAULT '',
  birthday     DATE,
  averagegrade NUMERIC(3, 2)
);

DROP TABLE IF EXISTS studentgrade;
CREATE TABLE studentgrade (
  id        SERIAL PRIMARY KEY,
  VALUE     INTEGER NOT NULL,
  DATE      DATE          NOT NULL,
  studentid INTEGER       NOT NULL,
  CHECK (VALUE > 1),
  CHECK (VALUE < 6)
);

INSERT INTO student (name, city, password, birthday, averagegrade)
VALUES
  ('Jan', 'Gdańsk', 'jan123', '1987-01-01', 5),
  ('Anna', 'Sopot', 'anna123', '1985-02-03', 0),
  ('Piotr', 'Gdańsk', 'piotr123', '1996-03-01', 0);

INSERT INTO studentgrade (value, date, studentid) VALUES
  (5, '2017-12-12', 1);


DROP TABLE IF EXISTS animal;
CREATE TABLE animal (
  id   SERIAL PRIMARY KEY,
  name TEXT    NOT NULL,
  type TEXT    NOT NULL,
  age  INTEGER NOT NULL

);

INSERT INTO animal (NAME, TYPE, age)
VALUES
  ('Azor', 'Pies', '10');