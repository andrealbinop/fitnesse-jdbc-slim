fitnesse-jdbc-slim [![Build Status](https://travis-ci.org/andreptb/fitnesse-jdbc-slim.svg)](https://travis-ci.org/andreptb/fitnesse-jdbc-slim) [![Coverage Status](https://coveralls.io/repos/andreptb/fitnesse-jdbc-slim/badge.svg)](https://coveralls.io/r/andreptb/fitnesse-jdbc-slim) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.andreptb/fitnesse-jdbc-slim/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.andreptb/fitnesse-jdbc-slim/)
==============

[FitNesse](https://github.com/unclebob/fitnesse) JDBC fixture in [slim format](http://www.fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM). Allows running SQL commands on multiple database connections. This project is licensed under [MIT](LICENSE).

#### Installation

* This module and spring dependencies must be in [FitNesse classpath](http://www.fitnesse.org/FitNesse.FullReferenceGuide.UserGuide.WritingAcceptanceTests.ClassPath). You can download the jar from [here](https://oss.sonatype.org/content/groups/public/com/github/andreptb/fitnesse-jdbc-slim/0.0.2/fitnesse-jdbc-slim-0.0.2.jar) or with [maven](https://github.com/lvonk/fitnesse-maven-classpath) (see below).
* The jdbc driver which the fixture will be used to connect also must be on [FitNesse](https://github.com/unclebob/fitnesse) classpath.

```xml
<dependency>
  <groupId>com.github.andreptb</groupId>
  <artifactId>fitnesse-jdbc-slim</artifactId>
  <version>0.0.2</version>
</dependency>
```

####  Sample:
```
|import|
|com.github.andreptb.fitnesse| 
 
|library|
|jdbc fixture|

|script|
|connect jdbc on | ${DATABASE_NAME} | with url | ${DATABASE_URL} | and driver | ${DRIVER_CLASS_NAME} | and username | ${DATABASE_USERNAME} | and password | ${DATABASE_PASSWORD} | # registers the ${DATABASE_NAME} database, must be the done before running SQL commands 
|run in | ${DATABASE_NAME} | CREATE TABLE USER (ID BIGINT IDENTITY, NAME VARCHAR(255) NOT NULL, PASSWORD VARCHAR(255) NOT NULL) | # runs the command CREATE TABLE, only fails if an exception occurs.
|run in | ${DATABASE_NAME} | INSERT INTO USER (NAME, PASSWORD) VALUES ('user1', 'password1') | # runs the command INSERT, only fails if an exception occurs.
|$usernameId= | query in | ${DATABASE_NAME} | SELECT ID FROM USER WHERE NAME = 'user1' | # runs a SELECT command, assigns the value of the first column of the first row to the variable $usernameId. If was an UPDATE command, would return the updated rows count.
```
