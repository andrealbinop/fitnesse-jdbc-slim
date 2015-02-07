fitnesse-jdbc-slim
==============

[FitNesse](https://github.com/unclebob/fitnesse) JDBC fixture in [slim format](http://www.fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM). Allows running SQL commands on multiple database connections. This project is licensed under [GNU 3.0](LICENSE).

### Pre-requisites

* This module and spring dependencies must in [FitNesse classpath](http://www.fitnesse.org/FitNesse.FullReferenceGuide.UserGuide.WritingAcceptanceTests.ClassPath). You can download all necessary jars from [here](link) or with [maven](https://github.com/lvonk/fitnesse-maven-classpath).
* The jdbc driver which the fixture will be used to connect also must be on [FitNesse](https://github.com/unclebob/fitnesse) classpath.

### Setup library:

|import|
|org.andreptb.fitnesse|

|library|
|jdbc fixture|

###  Sample:

|script|
| connect jdbc on | ${DATABASE_NAME} | with url | ${DATABASE_URL} | and driver | ${DRIVER_CLASS_NAME | and username | ${DATABASE_USERNAME} | and password | ${DATABASE_PASSWORD} |
| execute in database | ${DB} | CREATE TABLE USER (ID BIGINT IDENTITY, NAME VARCHAR(255) NOT NULL, PASSWORD VARCHAR(255) NOT NULL) |
| execute in database | ${DB} | INSERT INTO USER (NAME, PASSWORD) VALUES ('user1', 'password1') |
| $usernameId= | execute in database | ${DB} | SELECT ID FROM USER WHERE NAME = 'user1' |







