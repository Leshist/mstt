create database auth;
use auth;

CREATE TABLE playerauth
(
id INTEGER AUTO_INCREMENT,
email TEXT,
password TEXT,
uuid TEXT,
PRIMARY KEY (id)
) COMMENT='player auth table';