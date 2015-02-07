fitnesse-jdbc-slim [![Build Status](https://travis-ci.org/andreptb/fitnesse-jdbc-slim.svg)](https://travis-ci.org/andreptb/fitnesse-jdbc-slim) [![Coverage Status](https://coveralls.io/repos/andreptb/fitnesse-jdbc-slim/badge.svg)](https://coveralls.io/r/andreptb/fitnesse-jdbc-slim)
==============

[FitNesse](https://github.com/unclebob/fitnesse) JDBC fixture in [slim format](http://www.fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM). Allows running SQL commands on multiple database connections. This project is licensed under [GNU 3.0](LICENSE).

#### Installation

* This module and spring dependencies must in [FitNesse classpath](http://www.fitnesse.org/FitNesse.FullReferenceGuide.UserGuide.WritingAcceptanceTests.ClassPath). You can download all necessary jars from [here](link) or with [maven](https://github.com/lvonk/fitnesse-maven-classpath).
* The jdbc driver which the fixture will be used to connect also must be on [FitNesse](https://github.com/unclebob/fitnesse) classpath.

####  Sample:
```
  |import|
  |org.andreptb.fitnesse|
  
  |library|
  |jdbc fixture|

  |script|
  | connect jdbc on | ${DATABASE_NAME} | with url | ${DATABASE_URL} | and driver | ${DRIVER_CLASS_NAME | and username | ${DATABASE_USERNAME} | and password | ${DATABASE_PASSWORD} |
  | execute in database | ${DB} | CREATE TABLE USER (ID BIGINT IDENTITY, NAME VARCHAR(255) NOT NULL, PASSWORD VARCHAR(255) NOT NULL) |
  | execute in database | ${DB} | INSERT INTO USER (NAME, PASSWORD) VALUES ('user1', 'password1') |
  | $usernameId= | execute in database | ${DB} | SELECT ID FROM USER WHERE NAME = 'user1' |
```






