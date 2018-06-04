CREATE DATABASE IF NOT EXISTS softwareacademy;
USE softwareacademy;

CREATE USER IF NOT EXISTS 'user_softwareacademy'@'localhost'
  IDENTIFIED BY 'softwareacademy123';

GRANT USAGE ON *.* TO 'user_softwareacademy'@'localhost'
IDENTIFIED BY 'softwareacademy123';
GRANT ALL PRIVILEGES ON softwareacademy.* TO 'user_softwareacademy'@'localhost';

FLUSH PRIVILEGES;

DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student` (
  `id`           INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`         VARCHAR(20)      NOT NULL DEFAULT '',
  `city`         VARCHAR(20)               DEFAULT '',
  `password`     VARCHAR(20)      NOT NULL DEFAULT '',
  `birthday`     DATE,
  `averagegrade` DOUBLE,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `studentgrade`;
CREATE TABLE IF NOT EXISTS `studentgrade` (
  `id`        INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `value`     DOUBLE           NOT NULL,
  `date`      DATE             NOT NULL,
  `studentid` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


INSERT INTO `student` (`name`, `city`, `password`, `birthday`, `averagegrade`)
VALUES
  ('Jan', 'Gdańsk', 'jan123', '1987-01-01', 5),
  ('Anna', 'Sopot', 'anna123', '1985-02-03', 0),
  ('Piotr', 'Gdańsk', 'piotr123', '1996-03-01', 0);

INSERT INTO `studentgrade` (value, date, studentid) VALUES (5, '2017-12-12', 1);


DROP TABLE IF EXISTS `animal`;
CREATE TABLE IF NOT EXISTS `animal` (
  `id`   INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20)      NOT NULL,
  `type` VARCHAR(20)      NOT NULL,
  `age`  INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

INSERT INTO `animal` (`name`, `type`, `age`)
VALUES
  ('Azor', 'Pies', '10');

DROP TABLE IF EXISTS `example_users`;
CREATE TABLE `example_users` (
  `id`   INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  name TEXT    NOT NULL,
  password TEXT    NOT NULL,
  age  INTEGER NOT NULL,
  PRIMARY KEY (`id`)
)
 ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

INSERT INTO example_users (name, password, age)
VALUES
  ('Jan', '123', 20);